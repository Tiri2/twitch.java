package com.ryifestudios.twitch.events.impl.commands;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.commands.models.Command;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

/**
 * This event is called, if are error is concurred on command execution
 */
@Getter
public class CommandErrorEvent extends Event {

    /**
     * The command, where the error concurred
     */
    private final Command command;
    /**
     * Args that was passed while executing
     */
    private final String[] arguments;
    /**
     * {@link BasisCommand} Annotation to call {@link com.ryifestudios.twitch.annotations.commands.ArgumentAno} for getting the args
     */
    private final BasisCommand basisCommand;
    /**
     * Reason why this event was called
     */
    private final Reason reason;

    public CommandErrorEvent(CommandContext ctx, Command command, String[] arguments, BasisCommand basisCommand, Reason reason) {
        super(ctx, false);
        this.command = command;
        this.arguments = arguments;
        this.basisCommand = basisCommand;
        this.reason = reason;
    }

    /**
     * Reasons why it came to a error
     */
    public enum Reason{
        /**
         * Error concurred while executing the basis method
         */
        EXECUTE_BASISMETHOD,
        /**
         * Args are missing
         */
        ARGS_MISSING,
        /**
         * Args of a sub command is missing
         */
        ARGS_MISSING_OF_SUBCMD,
        /**
         * Error concurred while executing a sub command
         */
        EXECUTE_SUBCMD,
        /**
         * Error concurred while calling constructor
         */
        INSTANCE_CREATION_ERROR
    }

}
