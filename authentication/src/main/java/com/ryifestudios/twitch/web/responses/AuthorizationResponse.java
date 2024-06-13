package com.ryifestudios.twitch.web.responses;

import com.ryifestudios.twitch.models.AccessToken;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;


/**
 *
 */
@Setter
public class AuthorizationResponse{

    /**
     * Variable for checking if we got the code or not
     */
    @Getter
    private boolean pending;

    private AccessToken accessToken;

    @Getter
    private String authorizedCode;


    public AuthorizationResponse() {
        pending = true;
    }

    public AccessToken accessToken(){
        return accessToken;
    }
}
