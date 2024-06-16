package com.ryifestudios.twitch.events.impl.irc;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import com.ryifestudios.twitch.parser.IRCMessageParser;
import lombok.Getter;

@Getter
public class PartEvent extends Event {

    private final IRCMessageParser.ParsedMessage parsedMessage;

    public PartEvent(CommandContext commandContext, IRCMessageParser.ParsedMessage parsedMessage) {
        super(commandContext, false);
        this.parsedMessage = parsedMessage;
    }
}
