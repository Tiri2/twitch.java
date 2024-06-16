package com.ryifestudios.twitch;


import com.ryifestudios.twitch.commands.CommandHandler;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.configuration.Configuration;
import com.ryifestudios.twitch.events.EventHandler;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

/**
 *
 */
public class Chat {


    private final Logger logger = LogManager.getLogger();


    /**
     * Websocket client variable
     */
    @Getter
    private final WSClient client;

    /**
     * This class handles everything about commands
     */
    @Getter
    private final CommandHandler commandHandler;

    /**
     * Everything about event
     */
    @Getter
    private final EventHandler eventHandler;

    private final Timer timer;

    /**
     * Constructor for {@link Chat}
     * @param config specific Chat settings like which channel to connect and the nick name
     * @param authConfig which is used to store the required data like client Id or client Secret
     */
    public Chat(Configuration config, AuthConfiguration authConfig) {
        this.commandHandler = new CommandHandler();
        this.eventHandler = new EventHandler();

        timer = new Timer();
        client = new WSClient(config, commandHandler);

        ChatAuthentication authentication = new ChatAuthentication(authConfig, client::connect);

        client.setAuth(authentication);

        logger.info("chat started");

    }

}
