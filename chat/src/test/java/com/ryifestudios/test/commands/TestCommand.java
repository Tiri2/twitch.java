package com.ryifestudios.test.commands;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;
import com.ryifestudios.twitch.commands.CommandContext;

@Command(name = "test", description = "fortnite")
public class TestCommand {

    @BasisCommand()
    public void handle(CommandContext ctx, String data){
        System.out.println("called");
    }

}
