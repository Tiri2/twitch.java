package com.ryifestudios.twitch;

import com.ryifestudios.twitch.commands.CommandHandler;
import com.ryifestudios.twitch.configuration.Configuration;
import com.ryifestudios.twitch.parser.IRCMessageParser;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Date;

public class WSClient extends org.java_websocket.client.WebSocketClient {

    private final Logger logger = LogManager.getLogger("WS");

    @Getter
    @Accessors(fluent = true)
    private final Configuration config;

    private final ChatAuthentication auth;

    private final CommandHandler commandHandler;


    public WSClient(Configuration configuration, ChatAuthentication authentication, CommandHandler cmdHandler) {
        super(URI.create("ws://irc-ws.chat.twitch.tv:80"));
        this.config = configuration;
        this.auth = authentication;
        this.commandHandler = cmdHandler;
    }

    public void connectClient() {
        // Connect to the websocket server
        this.connect();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

        System.out.println("Websocket Client connected");

        this.send(STR."PASS oauth:\{auth.response().accessToken().getAccessToken()}");
        this.send(STR."NICK \{auth.authConfig().clientName()}");
        this.send(STR."JOIN #\{config.getChannel()}");

    }

    @Override
    public void onMessage(String ircMessage) {
        System.out.println(STR."ircMessage: \{ircMessage}");

        String rawIrcMessage = ircMessage.trim();
        System.out.println(STR."Message received (\{new Date()}): '\{rawIrcMessage}'\n");

        String[] messages = rawIrcMessage.split("\r\n"); // The IRC message may contain one or more messages.
        for (String message : messages) {
            IRCMessageParser.ParsedMessage parsedMessage = IRCMessageParser.parseMessage(message);
            System.out.println(parsedMessage);

            if (parsedMessage != null) {
                switch (parsedMessage.getCommand().getCommand()) {
                    case "PRIVMSG":
                        commandHandler.execute(parsedMessage.getCommand().getBotCommand(), "", null, this);
                        break;
                    case "PING":
                        this.send(STR."PONG \{parsedMessage.getParameters()}");
                        break;
                    case "001":
                        this.send(STR."JOIN \{config.getChannel()}");
                        break;
                    case "JOIN":
//                            this.send("PRIVMSG " + config.getChannel() + " :" + moveMessage);
                        this.send(STR."PRIVMSG #\{config.getChannel()} :Channel joint");

                        break;
                    case "PART":
                        System.out.println("The channel must have banned (/ban) the bot.");
                        this.close();
                        break;
                    case "NOTICE":
                        if ("Login authentication failed".equals(parsedMessage.getParameters())) {
                            System.out.println(STR."Authentication failed; left \{config.getChannel()}");

                            this.send(STR."PART \{config.getChannel()}");

                        } else if ("You don’t have permission to perform that action".equals(parsedMessage.getParameters())) {
                            System.out.println(STR."No permission. Check if the access token is still valid. Left \{config.getChannel()}");
                            send(STR."PART \{config.getChannel()}");

                        }
                        break;
                    default:
                        // Ignore all other IRC messages.
                        break;
                }
            }
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Disconnected");
        System.out.println(STR."i=\{i}, s=\{s} b=\{b}");
        // TODO impl reconnection
    }

    @Override
    public void onError(Exception e) {
        logger.catching(e);
    }
}
