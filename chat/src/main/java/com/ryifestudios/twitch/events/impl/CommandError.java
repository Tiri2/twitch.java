package com.ryifestudios.twitch.events.impl;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.commands.models.Command;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

@Getter
public class CommandError extends Event {

    private final Command command;
    private final String[] arguments;
    private final BasisCommand basisCommand;
    private final Reason reason;

    public CommandError(CommandContext ctx, Command command, String[] arguments, BasisCommand basisCommand, Reason reason) {
        super(ctx, false);
        this.command = command;
        this.arguments = arguments;
        this.basisCommand = basisCommand;
        this.reason = reason;
    }

    public enum Reason{
        EXECUTE_BASISMETHOD, ARGS_MISSING, ARGS_MISSING_OF_SUBCMD, EXECUTE_SUBCMD, INSTANCE_CREATION_ERROR
    }

}
