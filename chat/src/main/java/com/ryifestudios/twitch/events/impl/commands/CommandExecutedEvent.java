package com.ryifestudios.twitch.events.impl.commands;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.commands.models.Command;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

@Getter
public class CommandExecutedEvent extends Event {

    private final Command command;
    private final String[] arguments;

    public CommandExecutedEvent(CommandContext commandContext, Command command, String[] arguments) {
        super(commandContext, false);
        this.command = command;
        this.arguments = arguments;
    }
}
