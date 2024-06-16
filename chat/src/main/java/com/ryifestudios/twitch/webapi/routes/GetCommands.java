package com.ryifestudios.twitch.webapi.routes;

import com.ryifestudios.twitch.commands.CommandHandler;
import com.ryifestudios.twitch.commands.models.Argument;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetCommands implements Handler {

    private final CommandHandler commandHandler;

    public GetCommands(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {

        JSONArray commands = new JSONArray();

        commandHandler.commands().forEach((name, cmd) -> {
            JSONObject js = new JSONObject();
            js.put("name", name);
            js.put("description", cmd.getDescription());

            JSONArray subCmds = new JSONArray();

            cmd.getSubCommands().forEach((sName, sCmd) -> {
                JSONObject sJs = new JSONObject();
                sJs.put("name", sName);
                sJs.put("description", sCmd.getDescription());
                JSONArray args = new JSONArray();

                for (Argument argument : sCmd.getArguments()) {
                    JSONObject argJson = new JSONObject();

                    argJson.put("name", argument.getName());
                    argJson.put("description", argument.getDescription());

                    args.put(argJson);
                }

                subCmds.put(sJs);
            });

            js.put("subCommands", subCmds);
            commands.put(js);
        });


        context.json(commands.toString());

    }



}
