package com.ryifestudios.twitch.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryifestudios.twitch.Utils;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.models.AccessToken;
import com.ryifestudios.twitch.web.responses.AuthorizationResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class RequestAccessToken extends TimerTask {

    private final AuthorizationResponse response;
    private final AuthConfiguration config;

    private final HttpClient httpClient;

    public RequestAccessToken(AuthorizationResponse response, AuthConfiguration config) {
        this.response = response;
        this.config = config;

        httpClient = HttpClients.createDefault();
    }

    @Override
    public void run() {
        System.out.println("executed");

        if(response.isPending()){
           return;
        }

        try{
            HttpPost post = new HttpPost("https://id.twitch.tv/oauth2/token");

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("client_id", config.clientId()));
            params.add(new BasicNameValuePair("client_secret", config.clientSecret()));
            params.add(new BasicNameValuePair("code", response.getAuthorizedCode()));
            params.add(new BasicNameValuePair("grant_type", "authorization_code"));
            params.add(new BasicNameValuePair("redirect_uri", config.redirectUri()));

            post.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse res = httpClient.execute(post);

            AccessToken accessToken = new AccessToken();
            ObjectMapper mapper = new ObjectMapper();

            String raw = EntityUtils.toString(res.getEntity(), "UTF-8");

            System.out.println(raw);

            JsonNode node = mapper.readTree(raw);

            accessToken.setAccessToken(node.get("access_token").asText());
            accessToken.setRefreshToken(node.get("refresh_token").asText());
            accessToken.setExpiresIn(node.get("expires_in").asInt());
            accessToken.setTokenType(node.get("token_type").asText());
            accessToken.setScopes(Utils.stringToArrayList(node.get("scope").asText()));

            response.setAccessToken(accessToken);

            this.cancel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
