package com.ryifestudios.twitch.events.impl.irc;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import com.ryifestudios.twitch.parser.IRCMessageParser;
import lombok.Getter;

@Getter
public class NoticeEvent extends Event {

    private final IRCMessageParser.ParsedMessage message;

    public NoticeEvent(CommandContext commandContext, IRCMessageParser.ParsedMessage message) {
        super(commandContext, false);
        this.message = message;
    }
}
