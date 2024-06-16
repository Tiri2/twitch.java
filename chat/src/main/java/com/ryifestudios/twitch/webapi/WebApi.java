package com.ryifestudios.twitch.webapi;

import com.ryifestudios.twitch.commands.CommandHandler;
import com.ryifestudios.twitch.events.EventHandler;
import com.ryifestudios.twitch.webapi.routes.GetCommands;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class WebApi {

    @Getter
    @Setter
    private int port;

    @Getter
    private Javalin app;

    private final EventHandler eventHandler;
    private final CommandHandler commandHandler;

    public WebApi(Consumer<JavalinConfig> javalinConfig, int port, EventHandler eh, CommandHandler ch) {
        this.port = port;
        this.eventHandler = eh;
        this.commandHandler = ch;

        app = Javalin.create(javalinConfig);

    }

    public void start(){
        app.get("commands", new GetCommands(commandHandler));

        app.start(port);
    }
}
