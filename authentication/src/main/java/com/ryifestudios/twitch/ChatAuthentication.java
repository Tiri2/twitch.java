package com.ryifestudios.twitch;

import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.web.handlers.CallbackHandler;
import com.ryifestudios.twitch.web.responses.AuthorizationResponse;
import io.javalin.Javalin;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.lang.StringTemplate.STR;

@Accessors(fluent = true)
public class ChatAuthentication {

    Logger logger = LogManager.getLogger("Credentials");

    @Getter
    private final AuthConfiguration authConfig;

    @Getter
    private AuthorizationResponse response;

    private final Timer timer;

    public ChatAuthentication(AuthConfiguration authConfiguration, HandlerExecutor executor) {
        this.authConfig = authConfiguration;

        response = new AuthorizationResponse();
        timer = new Timer();

        start(executor);
    }


    private void start(HandlerExecutor executor){
        requestAuthorization(executor);

        // TODO: wenn response.IsPending false ist - dann hol den token
        // Einen timer benutzen zum checken wenn pending false ist


    }

    public void requestAuthorization(HandlerExecutor executor){

        String url = STR."https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=\{authConfig.clientId()}&redirect_uri=\{authConfig.redirectUri()}&scope=\{authConfig.scopes()}\n";

        logger.warn("Please authenticate your app with your twitch account. use {}", url);
        System.out.println(url);

        Javalin app = Javalin.create();

        app.get("/callback", new CallbackHandler(response, authConfig, executor));

        app.start(authConfig.webPort());

    }

    private void getAccessToken(){

    }

    public void useRefreshTokenToGetANewAccessToken(){

    }

}
