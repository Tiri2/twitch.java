package com.ryifestudios.twitch;


import com.ryifestudios.twitch.commands.CommandHandler;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.configuration.Configuration;
import com.ryifestudios.twitch.events.EventHandler;
import com.ryifestudios.twitch.schedular.SchedulerHandler;
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
     * This class handles everything about schedulers
     */
    @Getter
    private final SchedulerHandler schedulerHandler;

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
     * This class is used to bundle all needed things for the twitch service.
     * Bundled is: <ul>
     *     <li>{@link CommandHandler}</li>
     *     <li>{@link EventHandler}</li>
     *     <li>{@link SchedulerHandler}</li>
     *     <li>{@link WSClient}</li>
     * </ul>
     * @param config specific Chat settings like which channel to connect and the nickname
     * @param authConfig which is used to store the required data like client Id or client Secret
     */
    public Chat(Configuration config, AuthConfiguration authConfig) {
        this.eventHandler = new EventHandler();
        this.commandHandler = new CommandHandler(eventHandler);
        this.schedulerHandler = new SchedulerHandler();

        client = new WSClient(config, commandHandler, eventHandler);
        client.setAuth(new ChatAuthentication(authConfig, client::connect));

        webApi = new WebApi(c -> {}, 1100, eventHandler, commandHandler);
        webApi.start();

        schedulerHandler.setWsClient(client);
        schedulerHandler.start();

        logger.info("chat started");
    }

}
