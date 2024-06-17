package com.ryifestudios.twitch.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.ryifestudios.twitch.ChatAuthentication;
import com.ryifestudios.twitch.models.AccessToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

public class GetNewAccessToken extends TimerTask {

    private final Logger logger = LogManager.getLogger();

    private final ChatAuthentication auth;

    public GetNewAccessToken(ChatAuthentication auth) {
        this.auth = auth;
    }

    @Override
    public void run() {
        logger.debug("GetNewAccessToken is called");
        AccessToken a = auth.accessToken();

        // A could be null
        if(a == null) {
            return;
        }

        JsonNode tokenValidNode = auth.validateToken(a.getAccessToken());

        if(tokenValidNode != null && tokenValidNode.get("status") == null){
            logger.info("token is valid");
            return;
        }

        logger.info("request new access token");

        JsonNode requestedTokenNode = auth.requestNewTokenWithRefreshToken(a.getRefreshToken());

        if(requestedTokenNode == null || requestedTokenNode.get("status") != null){
            logger.error("Refresh token is invalid - please authenticate new");
            // TODO: request new authenticate
            return;
        }

        a.setAccessToken(requestedTokenNode.get("access_token").asText());
        a.setRefreshToken(requestedTokenNode.get("refresh_token").asText());
        a.setExpiresIn(0);

        logger.info("use new requested access token");

    }
}
