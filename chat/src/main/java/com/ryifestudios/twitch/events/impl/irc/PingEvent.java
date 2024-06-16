package com.ryifestudios.twitch.events.impl.irc;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import com.ryifestudios.twitch.parser.IRCMessageParser;
import lombok.Getter;

/**
 * This Event is called, when a ping came from twitch
 */
@Getter
public class PingEvent extends Event {

    /**
     * Parsed message to handle this event
     */
    private final IRCMessageParser.ParsedMessage message;

    public PingEvent(CommandContext commandContext, IRCMessageParser.ParsedMessage message) {
        super(commandContext, false);
        this.message = message;
    }
}
