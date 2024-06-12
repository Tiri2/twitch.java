package com.ryifestudios.twitch.configuration;

import com.ryifestudios.twitch.scopes.ChatScopesBuilder;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
public class AuthConfiguration {

    private String clientId;
    private String redirectUri;
    private String scopes;

}
