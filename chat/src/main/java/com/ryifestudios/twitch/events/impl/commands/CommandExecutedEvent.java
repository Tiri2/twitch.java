package com.ryifestudios.twitch.events.impl.commands;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.commands.models.Command;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

/**
 * This Event is called, when a command is successfully executed
 */
@Getter
public class CommandExecutedEvent extends Event {

    /**
     * The executed command
     */
    private final Command command;
    /**
     * args that was entered in
     */
    private final String[] arguments;

    public CommandExecutedEvent(CommandContext commandContext, Command command, String[] arguments) {
        super(commandContext, false);
        this.command = command;
        this.arguments = arguments;
    }
}
