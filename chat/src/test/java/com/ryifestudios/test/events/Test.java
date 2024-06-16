package com.ryifestudios.test.events;

import com.ryifestudios.twitch.annotations.event.Event;
import com.ryifestudios.twitch.events.impl.commands.CommandErrorEvent;
import com.ryifestudios.twitch.events.impl.commands.CommandExecutedEvent;
import com.ryifestudios.twitch.events.impl.commands.CommandNotFoundEvent;

public class Test {

    @Event
    public void onCommandError(CommandErrorEvent error){
//        error.ctx().reply("error concurred");
        System.out.println("called test onCommandError");
    }

    @Event
    public void onCommandNotFound(CommandNotFoundEvent c){
        System.out.println("command not found");

        c.ctx().reply(STR."Command \{c.getCommandName()} not found");
    }

    @Event
    public void executed(CommandExecutedEvent c){
        System.out.println(STR."\{c.getCommand()} executed");
    }


}
