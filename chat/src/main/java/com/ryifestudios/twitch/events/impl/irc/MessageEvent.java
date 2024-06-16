package com.ryifestudios.twitch.events.impl.irc;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

@Getter
public class MessageEvent extends Event {

    private final String message;

    public MessageEvent(CommandContext commandContext, String message) {
        super(commandContext, false);
        this.message = message;
    }
}
