package com.ryifestudios.twitch.examples;

import com.ryifestudios.twitch.Chat;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.configuration.Configuration;
import com.ryifestudios.twitch.scopes.ChatScopesBuilder;

public class Main {

    public static void main(String[] args) {

        AuthConfiguration authConfiguration = AuthConfiguration.builder()
                .clientId("r1mln6qcd2m5xvgx4u6wlvgyy33c5u")
                .redirectUri("http://localhost:1000/callback")
                .clientSecret("3dcwh6gub60althyaxs1x3dkisogg0") // 12.6 15:40
                .clientName("ryifebot")
                .webPort(1000)
                .scopes(new ChatScopesBuilder().withChatRead().withChatEdit().withChannelModerate().build()).build();


        new Chat(new Configuration("tiri2", "tiri2"), authConfiguration);
    }
}