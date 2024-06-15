package com.ryifestudios.test.commands;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;
import com.ryifestudios.twitch.annotations.commands.SubCommand;
import com.ryifestudios.twitch.commands.CommandContext;

import java.lang.annotation.Annotation;

@Command(name = "abc", description = "fortnite")
public class ABCCommand  {

    @BasisCommand()
    public void call(CommandContext ctx){
        System.out.println("called");

        ctx.reply("abc command called");
    }

    @SubCommand(name = "tel")
    public void tel(){
        System.out.println("tel");
    }
}
