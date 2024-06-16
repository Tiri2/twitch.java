package com.ryifestudios.twitch;


import com.ryifestudios.twitch.commands.CommandHandler;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.configuration.Configuration;
import com.ryifestudios.twitch.events.EventHandler;
import com.ryifestudios.twitch.webapi.WebApi;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    /**
     * Use to get information's about this bot
     */
    private final WebApi webApi;

    /**
     * Constructor for {@link Chat}
     * @param config specific Chat settings like which channel to connect and the nickname
     * @param authConfig which is used to store the required data like client Id or client Secret
     */
    public Chat(Configuration config, AuthConfiguration authConfig) {
        this.eventHandler = new EventHandler();
        this.commandHandler = new CommandHandler(eventHandler);

        client = new WSClient(config, commandHandler, eventHandler);
        client.setAuth(new ChatAuthentication(authConfig, client::connect));

        webApi = new WebApi(c -> {}, 1100, eventHandler, commandHandler);
        webApi.start();

        logger.info("chat started");
    }

}
