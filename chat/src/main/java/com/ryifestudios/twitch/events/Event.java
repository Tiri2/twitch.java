package com.ryifestudios.twitch.events;

import com.ryifestudios.twitch.commands.CommandContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
public abstract class Event {

    private CommandContext commandContext;

    @Setter
    @Getter
    private boolean canceled;

    public CommandContext ctx(){
        return commandContext;
    }

}
