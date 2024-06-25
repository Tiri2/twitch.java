package com.ryifestudios.twitch.schedular;

import com.ryifestudios.twitch.WSClient;

public class TaskContext {

    private final WSClient client;

    /**
     * Create a new Command Content
     * @param client the websocket client, to send messages
     */
    public TaskContext(WSClient client) {
        this.client = client;

    }

    /**
     * Send a message to the twitch chat
     * @param text the message to send
     */
    public void send(String text){
        client.send(STR."PRIVMSG #\{client.config().getChannel()} :\{text}");
    }


}
