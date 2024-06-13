package com.ryifestudios.twitch.configuration;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
public class AuthConfiguration {

    private String clientId;
    private String clientName;
    private String clientSecret;
    private String redirectUri;
    private String scopes;

    private int webPort;

}
