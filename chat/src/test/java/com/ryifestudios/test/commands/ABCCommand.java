package com.ryifestudios.test.commands;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;
import com.ryifestudios.twitch.annotations.commands.SubCommand;
import com.ryifestudios.twitch.commands.CommandContext;


@Command(name = "abc", description = "THIS IS THE AWESOME ABC COMMAND")
public class ABCCommand  {

    @BasisCommand()
    public void call(CommandContext ctx){
        ctx.send("abc command called");
    }

    @SubCommand(name = "tel")
    public void tel(CommandContext ctx){
        System.out.println("tel");
        ctx.send(STR."Ich liebe @\{ctx.getUser().getDisplayName()}");
    }

    @SubCommand(name="shutdown")
    public void shutdown(CommandContext ctx){
        ctx.reply("ok");
        System.exit(1000);
    }

}
