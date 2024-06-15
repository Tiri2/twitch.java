package com.ryifestudios.test;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;
import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.models.User;

@Command(name = "def", description = "fortnite")
public class DEFCommand {

    @BasisCommand()
    public void test(CommandContext ctx){
        System.out.println("generate user");
        User u = ctx.getUser();
        System.out.println("user generated");

        ctx.reply(STR."@\{u.getDisplayName()} has entered giveaway");
    }

}
