package com.ryifestudios.twitch.events.impl.websocket;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.events.Event;
import lombok.Getter;

@Getter
public class WebSocketErrorEvent extends Event {

    private final Exception e;

    public WebSocketErrorEvent(CommandContext commandContext, Exception e) {
        super(commandContext, false);
        this.e = e;
    }
}
