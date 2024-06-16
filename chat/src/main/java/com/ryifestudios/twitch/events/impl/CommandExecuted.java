package com.ryifestudios.twitch.events.impl;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.commands.models.Command;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

@Getter
public class CommandExecuted extends Event {

    private final Command command;
    private final String[] arguments;

    public CommandExecuted(CommandContext commandContext, Command command, String[] arguments) {
        super(commandContext, false);
        this.command = command;
        this.arguments = arguments;
    }
}
