package com.ryifestudios.test;

import com.ryifestudios.twitch.Chat;
import com.ryifestudios.twitch.ChatAuthentication;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.configuration.Configuration;
import com.ryifestudios.twitch.scopes.ChatScopesBuilder;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setChannel("tiri2");

        AuthConfiguration authConfiguration = AuthConfiguration.builder()
                .clientId("r1mln6qcd2m5xvgx4u6wlvgyy33c5u")
                .redirectUri("http://localhost:1000/callback")
                .scopes(new ChatScopesBuilder().withChatRead().withChatEdit().withChannelModerate().build()).build();
        Chat chat = new Chat(configuration, new ChatAuthentication(authConfiguration, 1000));


    }

}
