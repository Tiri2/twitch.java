package com.ryifestudios.test;

import com.ryifestudios.twitch.Chat;
import com.ryifestudios.twitch.ChatAuthentication;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.configuration.Configuration;
import com.ryifestudios.twitch.scopes.ChatScopesBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Main {

    static {
        File file = new File("log4j2.xml");
        System.setProperty("log4j2.configurationFile", file.toURI().toString());
    }

    private static Logger logger;

    public static void main(String[] args) {

        logger = LogManager.getLogger();

        logger.info("Hallo welt");

        Configuration configuration = new Configuration();

        configuration.setChannel("tiri2"); // tiri2 id : no

        AuthConfiguration authConfiguration = AuthConfiguration.builder()
                .clientId("r1mln6qcd2m5xvgx4u6wlvgyy33c5u")
                .redirectUri("http://localhost:1000/callback")
                .clientSecret("3dcwh6gub60althyaxs1x3dkisogg0") // 12.6 15:40
                .clientName("ryifebot")
                .webPort(1000)
                .scopes(new ChatScopesBuilder().withChatRead().withChatEdit().withChannelModerate().build()).build();

        Chat chat = new Chat(configuration, authConfiguration);
    }

}
