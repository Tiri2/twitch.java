package com.ryifestudios.twitch.events.impl.irc;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

/**
 * This event is called, when a message was sent
 * <p>User who send the message is available via {@link CommandContext}{@code .getUser()}</p>
 */
@Getter
public class MessageEvent extends Event {

    /**
     * sent message of a user
     */
    private final String message;

    public MessageEvent(CommandContext commandContext, String message) {
        super(commandContext, false);
        this.message = message;
    }
}
