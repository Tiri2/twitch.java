package com.ryifestudios.test.commands;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;

@Command(name = "test", description = "fortnite")
public class TestCommand {

    @BasisCommand()
    public void handle(){
        System.out.println("called");
    }

}
