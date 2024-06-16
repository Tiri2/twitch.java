package com.ryifestudios.twitch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.models.AccessToken;
import com.ryifestudios.twitch.storage.TokenStorageManager;
import com.ryifestudios.twitch.tasks.GetNewAccessToken;
import com.ryifestudios.twitch.web.handlers.CallbackHandler;
import io.javalin.Javalin;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static java.lang.StringTemplate.STR;

@Accessors(fluent = true)
public class ChatAuthentication {

    Logger logger = LogManager.getLogger("Credentials");

    @Getter
    private final AuthConfiguration authConfig;

    @Getter
    @Setter
    private AccessToken accessToken;

    private final TokenStorageManager tokenStorage;

    private final Timer timer;

    public ChatAuthentication(AuthConfiguration authConfiguration, HandlerExecutor executor) {
        this.authConfig = authConfiguration;

        timer = new Timer();
        tokenStorage = new TokenStorageManager(true);

        start(executor);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                tokenStorage.save();
            } catch (IOException e) {
                logger.catching(e);
            }
        }));
    }


    private void start(HandlerExecutor executor){
        // Logic for checking if a access token is valid reusing them instead of reauthenticate each time
        for(AccessToken a : tokenStorage.getTokens()){
            try{
                JsonNode node = validateToken(a.getAccessToken());

                // TODO: check if refresh token is valid, and request a new access token, if this fail let the user reauthenticate

                // If there is a status, it means something is not right
                if(node == null || node.get("status") != null){

                    JsonNode r = requestNewTokenWithRefreshToken(a.getRefreshToken());

                    logger.info("Access token is not valid, use refresh token instead to create a new one");

                    if(r == null || r.get("status") != null){
                        System.out.println(STR."Access Token & refresh Token is not valid \{a.getAccessToken()}");
                        logger.info("Access Token ({}) & refresh Token ({}) is not valid {} is not valid", a.getAccessToken(), a.getRefreshToken());
                        tokenStorage.remove(a);
                        continue;
                    }

                    a.setAccessToken(r.get("access_token").asText());
                    a.setRefreshToken(r.get("refresh_token").asText());
                    logger.info("refreshing access token was successful! Use new access token");
                }

                // ik it's ugly
                if(node != null) a.setExpiresIn(node.get("expires_in").asInt());

                accessToken = a;
                executor.execute();
                return;
            }catch (ClassCastException e){
                logger.catching(e);
            }

        }

        requestAuthorization(executor);

        timer.scheduleAtFixedRate(new GetNewAccessToken(this), 0, TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES));
        logger.info("start finished");
    }

    private void requestAuthorization(HandlerExecutor executor){

        String url = STR."https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=\{authConfig.clientId()}&redirect_uri=\{authConfig.redirectUri()}&scope=\{authConfig.scopes()}\n";

        logger.warn("Please authenticate your app with your twitch account. use {}", url);
        System.out.println(url);

        Javalin app = Javalin.create();

        app.get("/callback", new CallbackHandler(this, authConfig, executor, tokenStorage));

        app.start(authConfig.webPort());

    }


    /**
     * Check if the specific access token is valid or not
     * @param accessToken access token to check
     * @return if the access token is valid
     */
    public JsonNode validateToken(String accessToken){
        HttpGet get = new HttpGet("https://id.twitch.tv/oauth2/validate");
        ObjectMapper mapper = new ObjectMapper();
        get.setHeader("Authorization", STR."Bearer \{accessToken}");

        try {
            // Set the entity and execute the post
            HttpResponse res = HttpClients.createDefault().execute(get);
            String raw = EntityUtils.toString(res.getEntity(), "UTF-8");
            return mapper.readTree(raw);
        } catch (IOException e) {
            logger.error("Oops, access token fetching was failed");
            logger.catching(e);
            return null;
        }
    }

    /**
     * Get a new Access Token with the refresh token
     * @param refreshToken refresh token for getting a new access token
     * @return new fetched access token
     */
    public JsonNode requestNewTokenWithRefreshToken(String refreshToken){
        try {
            HttpPost post = new HttpPost("https://id.twitch.tv/oauth2/token");
            ObjectMapper mapper = new ObjectMapper();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("client_id", authConfig.clientId()));
            params.add(new BasicNameValuePair("client_secret", authConfig.clientSecret()));
            params.add(new BasicNameValuePair("grant_type", "refresh_token"));
            params.add(new BasicNameValuePair("refresh_token", URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)));
            post.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response = HttpClients.createDefault().execute(post);
            String raw = EntityUtils.toString(response.getEntity(), "UTF-8");
            JsonNode node = mapper.readTree(raw);

            System.out.println(STR."RAW: \{raw}\nNode: \{node}");

            return node;

        } catch (IOException e) {
            logger.catching(e);
        }
        return null;
    }

}
