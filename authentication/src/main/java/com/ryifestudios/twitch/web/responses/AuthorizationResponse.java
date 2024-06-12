package com.ryifestudios.twitch.web.responses;

import com.ryifestudios.twitch.models.AccessToken;
import lombok.Getter;
import lombok.Setter;


/**
 *
 */
public class AuthorizationResponse {

    /**
     * Variable for checking if we got the code or not
     */
    @Getter
    @Setter
    private boolean pending;

    @Setter
    private AccessToken accessToken;

    @Setter
    @Getter
    private String authorizedCode;

    public AuthorizationResponse() {
        pending = true;
    }

    public AccessToken accessToken(){
        return accessToken;
    }

}
