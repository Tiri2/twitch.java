package com.ryifestudios.twitch.events.impl;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

@Getter
public class CommandNotFound extends Event {

    private final String commandName;
    private final String[] args;

    public CommandNotFound(CommandContext commandContext, String commandName, String[] args) {
        super(commandContext, false);
        this.commandName = commandName;
        this.args = args;
    }
}
