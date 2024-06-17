package com.ryifestudios.twitch.examples.commands;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;
import com.ryifestudios.twitch.commands.CommandContext;

@Command(name="discord", description = "show discord invite link")
public class DiscordCommand {

    @BasisCommand
    public void onDiscordCommand(CommandContext ctx) {
        ctx.reply("Our Discord is https://discord.gg/QNcVBqNExG");
    }

}
