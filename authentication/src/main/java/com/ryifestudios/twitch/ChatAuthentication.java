package com.ryifestudios.twitch;

import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.tasks.RequestAccessToken;
import com.ryifestudios.twitch.web.handlers.CallbackHandler;
import com.ryifestudios.twitch.web.responses.AuthorizationResponse;
import io.javalin.Javalin;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static java.lang.StringTemplate.STR;

@Accessors(fluent = true)
public class ChatAuthentication {

    Logger logger = LogManager.getLogger("Credentials");

    @Getter
    private final AuthConfiguration authConfig;

    @Getter
    private AuthorizationResponse response;

    private final Timer timer;

    public ChatAuthentication(AuthConfiguration authConfiguration, int webPort) {
        this.authConfig = authConfiguration;

        response = new AuthorizationResponse();
        timer = new Timer();

        start(webPort);
    }


    private void start(int port){
        requestAuthorization(port);

        // TODO: wenn response.IsPending false ist - dann hol den token
        // Einen timer benutzen zum checken wenn pending false ist

        timer.scheduleAtFixedRate(new RequestAccessToken(response, authConfig), 0, TimeUnit.SECONDS.toMillis(5));

    }

    public void requestAuthorization(int port){

        String url = STR."https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=\{authConfig.clientId()}&redirect_uri=\{authConfig.redirectUri()}&scope=\{authConfig.scopes()}\n";

        logger.warn("Please authenticate your app with your twitch account. use {}", url);
        System.out.println(url);

        Javalin app = Javalin.create();

        app.get("/callback", new CallbackHandler(response));

        app.start(port);

    }

    private void getAccessToken(){

    }

    public void useRefreshTokenToGetANewAccessToken(){

    }

}
