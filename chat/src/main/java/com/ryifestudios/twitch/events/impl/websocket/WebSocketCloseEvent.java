package com.ryifestudios.twitch.events.impl.websocket;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

@Getter
public class WebSocketCloseEvent extends Event {

    private final int i;
    private final String s;
    private final boolean b;

    public WebSocketCloseEvent(CommandContext commandContext, int i, String s, boolean b) {
        super(commandContext, false);
        this.i = i;
        this.s = s;
        this.b = b;
    }
}
