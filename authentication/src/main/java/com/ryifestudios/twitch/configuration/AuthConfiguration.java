package com.ryifestudios.twitch.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class AuthConfiguration {

    private String clientId;
    private String redirectUri;
    private ArrayList<String> scopes;


}
