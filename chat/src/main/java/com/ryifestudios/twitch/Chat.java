package com.ryifestudios.twitch;


import com.ryifestudios.twitch.commands.CommandHandler;
import com.ryifestudios.twitch.configuration.Configuration;
import lombok.Getter;

public class Chat {

    @Getter
    private final WSClient client;

    @Getter
    private final CommandHandler commandHandler;

    public Chat(Configuration config, ChatAuthentication authentication) {

        this.commandHandler = new CommandHandler();

        client = new WSClient(config, authentication);

    }


}
