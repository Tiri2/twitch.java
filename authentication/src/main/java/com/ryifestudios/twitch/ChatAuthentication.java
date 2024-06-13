package com.ryifestudios.twitch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.models.AccessToken;
import com.ryifestudios.twitch.storage.TokenStorageManager;
import com.ryifestudios.twitch.storage.StorageItem;
import com.ryifestudios.twitch.storage.StorageManager;
import com.ryifestudios.twitch.web.handlers.CallbackHandler;
import com.ryifestudios.twitch.web.responses.AuthorizationResponse;
import io.javalin.Javalin;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Timer;

import static java.lang.StringTemplate.STR;

@Accessors(fluent = true)
public class ChatAuthentication {

    Logger logger = LogManager.getLogger("Credentials");

    @Getter
    private final AuthConfiguration authConfig;

    @Getter
    private AuthorizationResponse response;

    private TokenStorageManager tokenStorage;

    private final Timer timer;

    public ChatAuthentication(AuthConfiguration authConfiguration, HandlerExecutor executor) {
        this.authConfig = authConfiguration;

        response = new AuthorizationResponse();
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
        for(AccessToken a : tokenStorage.getTokens()){
            try{
                if(!validateToken(a.getAccessToken())){
                    System.out.println(STR."Token is not valid \{a.getAccessToken()}");
                    logger.info("token {} is not valid", a.getAccessToken());
                    tokenStorage.remove(a);
                    continue;
                }

                response.setAccessToken(a);
                executor.execute();
                return;
            }catch (ClassCastException e){
                logger.catching(e);
            }

        }

        requestAuthorization(executor);


    }

    public void requestAuthorization(HandlerExecutor executor){

        String url = STR."https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=\{authConfig.clientId()}&redirect_uri=\{authConfig.redirectUri()}&scope=\{authConfig.scopes()}\n";

        logger.warn("Please authenticate your app with your twitch account. use {}", url);
        System.out.println(url);

        Javalin app = Javalin.create();

        app.get("/callback", new CallbackHandler(response, authConfig, executor, tokenStorage));

        app.start(authConfig.webPort());

    }

    private void getAccessToken(){

    }

    public void useRefreshTokenToGetANewAccessToken(){

    }

    private boolean validateToken(String accessToken){
        HttpGet get = new HttpGet("https://id.twitch.tv/oauth2/validate");
        ObjectMapper mapper = new ObjectMapper();

        get.setHeader("Authorization", STR."Bearer \{accessToken}");

        try {
            // Set the entity and execute the post
            HttpResponse res = HttpClients.createDefault().execute(get);

            String raw = EntityUtils.toString(res.getEntity(), "UTF-8");

            System.out.println(raw); // TODO remove sout entry

            JsonNode node = mapper.readTree(raw);
            if(node.get("status") == null){
                return true;
            }
        } catch (IOException e) {
            logger.error("Oops, access token fetching was failed");
            logger.catching(e);
        }
        return false;
    }

}
