package com.ryifestudios.twitch.events.impl.commands;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

@Getter
public class CommandNotFoundEvent extends Event {

    private final String commandName;
    private final String[] args;

    public CommandNotFoundEvent(CommandContext commandContext, String commandName, String[] args) {
        super(commandContext, false);
        this.commandName = commandName;
        this.args = args;
    }
}
