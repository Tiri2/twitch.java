package com.ryifestudios.twitch.web.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryifestudios.twitch.HandlerExecutor;
import com.ryifestudios.twitch.Utils;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.models.AccessToken;
import com.ryifestudios.twitch.storage.StorageManager;
import com.ryifestudios.twitch.storage.TokenStorageManager;
import com.ryifestudios.twitch.web.responses.AuthorizationResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CallbackHandler implements Handler {

    private final Logger logger = LogManager.getLogger();

    private final AuthorizationResponse response;
    private final AuthConfiguration config;
    private final TokenStorageManager tokenStorage;

    private final HandlerExecutor executor;

    public CallbackHandler(AuthorizationResponse r, AuthConfiguration config, HandlerExecutor executor, TokenStorageManager tokenStorage) {
        this.response = r;
        this.config = config;
        this.executor = executor;
        this.tokenStorage = tokenStorage;
    }

    @Override
    public void handle(@NotNull Context context) {

        HttpPost post = new HttpPost("https://id.twitch.tv/oauth2/token");
        AccessToken accessToken = new AccessToken();
        ObjectMapper mapper = new ObjectMapper();
        List<NameValuePair> params = new ArrayList<>();

        response.setAuthorizedCode(context.queryParam("code"));
        logger.info("Code for authentication got");

        // Set the form params for the post
        params.add(new BasicNameValuePair("client_id", config.clientId()));
        params.add(new BasicNameValuePair("client_secret", config.clientSecret()));
        params.add(new BasicNameValuePair("code", response.getAuthorizedCode()));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("redirect_uri", config.redirectUri()));

        try {
            logger.info("Trying to fetch the access token");

            // Set the entity and execute the post
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse res = HttpClients.createDefault().execute(post);

            String raw = EntityUtils.toString(res.getEntity(), "UTF-8");
            JsonNode node = mapper.readTree(raw);

            System.out.println(raw); // TODO remove sout entry

            // Set the data to the model for handling in other classes
            accessToken.setAccessToken(node.get("access_token").asText());
            accessToken.setRefreshToken(node.get("refresh_token").asText());
            accessToken.setExpiresIn(node.get("expires_in").asInt());
            accessToken.setTokenType(node.get("token_type").asText());
            accessToken.setScopes(Utils.stringToArrayList(node.get("scope").asText()));
            response.setAccessToken(accessToken);

            executor.execute();
            tokenStorage.add(accessToken);
            logger.info("Successfully fetched access token");
        } catch (IOException e) {
            logger.error("Oops, access token fetching was failed");
            logger.catching(e);
        }

        // Send feedback back to the user
        context.result("Got the access token.");
    }

}
