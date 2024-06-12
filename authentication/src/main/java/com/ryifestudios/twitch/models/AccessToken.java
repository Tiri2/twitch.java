package com.ryifestudios.twitch.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {

    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private ArrayList<String> scopes;
    private String tokenType;
}
