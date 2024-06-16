package com.ryifestudios.twitch.events.impl.irc;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import com.ryifestudios.twitch.parser.IRCMessageParser;
import lombok.Getter;

/**
 * This event is called, when the websocket send a notice method
 */
@Getter
public class NoticeEvent extends Event {

    /**
     * Parsed message to handle this event
     */
    private final IRCMessageParser.ParsedMessage message;

    public NoticeEvent(CommandContext commandContext, IRCMessageParser.ParsedMessage message) {
        super(commandContext, false);
        this.message = message;
    }
}
