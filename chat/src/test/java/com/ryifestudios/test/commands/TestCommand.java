package com.ryifestudios.test.commands;

import com.ryifestudios.twitch.annotations.commands.ArgumentAno;
import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;
import com.ryifestudios.twitch.annotations.commands.SubCommand;
import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.commands.models.Argument;
import com.ryifestudios.twitch.models.User;

@Command(name = "test", description = "fortnite")
public class TestCommand {

    @BasisCommand(arguments = {
            @ArgumentAno(name = "song", description = "the song u want to play")
    })
    public void handle(CommandContext ctx, Argument song){
        System.out.println("called");
        User u = ctx.getUser();

        ctx.send(STR."ok! System32 wird gelöscht song: \{song.getValue()}");
    }

    @SubCommand(name = "set", description = "set the test case", arguments = {
            @ArgumentAno(name = "case", description = "the case")
    })
    public void set(CommandContext ctx){
        ctx.send("set");
    }

}
