package com.ryifestudios.twitch.commands;

import com.ryifestudios.twitch.WSClient;
import com.ryifestudios.twitch.models.User;

import java.util.Map;

/**
 * This class is used, to communicate with the twitch chat.
 */
public class CommandContext {

    private final WSClient client;
    private final Map<String, Object> tags;

    /**
     * Create a new Command Content
     * @param client the websocket client, to send messages
     * @param tags tags used for getting information like the message id
     */
    public CommandContext(WSClient client, Map<String, Object> tags) {
        this.client = client;
        this.tags = tags;
    }

    /**
     * Send a message to the twitch chat
     * @param text the message to send
     */
    public void send(String text){
        client.send(STR."PRIVMSG #\{client.config().getChannel()} :\{text}");
    }

    /**
     * Reply to a twitch message that was sent
     * @param text text to response
     */
    public void reply(String text){
        client.send(STR."@reply-parent-msg-id=\{tags.get("id")} PRIVMSG #\{client.config().getChannel()} :\{text}");
    }

    public User getUser(){
        return new User();
    }

}
