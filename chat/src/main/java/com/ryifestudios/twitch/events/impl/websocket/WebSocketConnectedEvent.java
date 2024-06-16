package com.ryifestudios.twitch.events.impl.websocket;

import com.ryifestudios.twitch.events.Event;
import lombok.Getter;
import org.java_websocket.handshake.ServerHandshake;

@Getter
public class WebSocketConnectedEvent extends Event {

    private final ServerHandshake serverHandshake;

    public WebSocketConnectedEvent(ServerHandshake serverHandshake) {
        super(null, false);
        this.serverHandshake = serverHandshake;
    }
}
