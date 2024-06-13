package com.ryifestudios.twitch.commands;

import com.ryifestudios.twitch.WSClient;

public class CommandContext {

    private WSClient client;

    public CommandContext(WSClient client) {
        this.client = client;
    }

    public void send(){
        client.send(STR."PRIVMSG #\{client.config().getChannel()} :test 123");
    }

    public void reply(){

    }

}
