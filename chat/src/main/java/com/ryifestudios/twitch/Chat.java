package com.ryifestudios.twitch;


import com.ryifestudios.twitch.commands.CommandHandler;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.configuration.Configuration;
import lombok.Getter;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Chat {

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

    private final Timer timer;

    /**
     *
     * @param config authConfiguration which is used to store the required data like client Id or client Secret
     * @param authConfig
     */
    public Chat(Configuration config, AuthConfiguration authConfig) {

        this.commandHandler = new CommandHandler();

        ChatAuthentication authentication = new ChatAuthentication(authConfig, new HandlerExecutor() {
            @Override
            public void execute() {
                connectWS();
            }
        });

        timer = new Timer();
        client = new WSClient(config, authentication, commandHandler);

    }

    private void connectWS(){
        client.connect();
    }

}
