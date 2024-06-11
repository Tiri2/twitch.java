package com.ryifestudios.twitch;

import com.ryifestudios.twitch.configuration.AuthConfiguration;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Accessors(fluent = true)
public class ChatAuthentication {

    Logger logger = LogManager.getLogger("Credentials");

    private final AuthConfiguration config;

    public ChatAuthentication(AuthConfiguration authConfiguration) {
        this.config = authConfiguration;

        requestAuthorization();
    }


    public void requestAuthorization(){
        String url = STR."https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=\{config.clientId()}&redirect_uri=\{config.redirectUri()}&scope=\{replaceArrayListToQueryParams(config.scopes())}\n";



        logger.warn("Please authenticate your app with your twitch account. use {}", url);
        System.out.println(url);

    }

    private void getAccessToken(){

    }

    public void useRefreshTokenToGetANewAccessToken(){

    }


    private String replaceArrayListToQueryParams(ArrayList<String> p){
        String s = "";

        //TODO: replace the array list p to a query param string to use it above

        return s;
    }

}
