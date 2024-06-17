package com.ryifestudios.twitch.examples.commands;

import com.ryifestudios.twitch.annotations.commands.ArgumentAno;
import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.annotations.commands.Command;
import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.commands.models.Argument;

import java.util.Random;

@Command(name = "coinflip", description = "flip a coin :<|")
public class CoinsflipCommand {

    private final Random random;

    public CoinsflipCommand() {
        random = new Random();
    }

    @BasisCommand(arguments = {
            @ArgumentAno(name = "bet", description = "bet if head or tail")
    })
    public void onCoinsflip(CommandContext ctx, Argument[] args) {
        int v = random.nextInt(10);

        boolean head = v > 5;

        if(args[0].getValue().equals("head") && head){
            ctx.reply("You have won");
            return;
        }

        ctx.reply("You haven't won");
        System.out.println(head);
    }

}
