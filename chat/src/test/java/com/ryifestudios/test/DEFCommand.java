package com.ryifestudios.test;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;
import com.ryifestudios.twitch.commands.CommandContext;

@Command(name = "def", description = "fortnite")
public class DEFCommand {

    @BasisCommand()
    public void test(CommandContext ctx){

    }

}
