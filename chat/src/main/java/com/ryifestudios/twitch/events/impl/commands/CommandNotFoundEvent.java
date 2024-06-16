package com.ryifestudios.twitch.events.impl.commands;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

/**
 * This event is called, if no command was found
 */
@Getter
public class CommandNotFoundEvent extends Event {

    /**
     * Command Name that failed
     */
    private final String commandName;
    /**
     * Arguments that was entered in
     */
    private final String[] args;

    public CommandNotFoundEvent(CommandContext commandContext, String commandName, String[] args) {
        super(commandContext, false);
        this.commandName = commandName;
        this.args = args;
    }
}
