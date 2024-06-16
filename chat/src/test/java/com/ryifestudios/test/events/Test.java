package com.ryifestudios.test.events;

import com.ryifestudios.twitch.annotations.event.Event;
import com.ryifestudios.twitch.events.impl.CommandError;
import com.ryifestudios.twitch.events.impl.CommandExecuted;
import com.ryifestudios.twitch.events.impl.CommandNotFound;

public class Test {

    @Event
    public void onCommandError(CommandError error){
//        error.ctx().reply("error concurred");
        System.out.println("called test onCommandError");
    }

    @Event
    public void onCommandNotFound(CommandNotFound c){
        System.out.println("command not found");

        c.ctx().reply(STR."Command \{c.getCommandName()} not found");
    }

    @Event
    public void executed(CommandExecuted c){
        System.out.println(STR."\{c.getCommand()} executed");
    }


}
