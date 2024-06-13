package com.ryifestudios.test.commands;

import com.ryifestudios.twitch.annotations.commands.ArgumentAno;
import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;
import com.ryifestudios.twitch.annotations.commands.SubCommand;
import com.ryifestudios.twitch.commands.CommandContext;

@Command(name = "test", description = "fortnite")
public class TestCommand {

    @BasisCommand()
    public void handle(CommandContext ctx){
        System.out.println("called");
    }

    @SubCommand(name = "set", description = "set the test case", arguments = {
            @ArgumentAno(name = "case", description = "the case")
    })
    public void set(CommandContext ctx){
        ctx.send();
    }

}
