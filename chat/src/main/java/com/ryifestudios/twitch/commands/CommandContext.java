package com.ryifestudios.twitch.commands;

import com.ryifestudios.twitch.WSClient;
import com.ryifestudios.twitch.models.User;
import lombok.Getter;

import java.util.Map;

/**
 * This class is used, to communicate with the twitch chat.
 */
public class CommandContext {

    private final WSClient client;
    private final Map<String, Object> tags;

    /**
     * -- GETTER --
     *  Get a user to display his personal infos like display name, if he is mod and so on
     *
     * @return the user
     */
    @Getter
    private final User user;

    /**
     * Create a new Command Content
     * @param client the websocket client, to send messages
     * @param tags tags used for getting information like the message id
     */
    public CommandContext(WSClient client, Map<String, Object> tags) {
        this.client = client;
        this.tags = tags;

        if(tags != null)
            user = new User(Integer.parseInt(tags.get("user-id").toString()), tags.get("display-name").toString(), tags.get("color").toString(),
                Boolean.parseBoolean(tags.get("mod").toString()),
                Boolean.parseBoolean(tags.get("subscriber").toString()), Boolean.parseBoolean(tags.get("turbo").toString()),
                Integer.parseInt(tags.get("room-id").toString()), Boolean.parseBoolean(tags.get("first-msg").toString()));
        else user = null;

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

}
