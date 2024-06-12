package com.ryifestudios.twitch;

import com.ryifestudios.twitch.configuration.Configuration;
import com.ryifestudios.twitch.parser.IRCMessageParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Date;

public class WSClient extends org.java_websocket.client.WebSocketClient {

    private final Logger logger = LogManager.getLogger("WS");

    private final Configuration config;

    private final ChatAuthentication auth;


    private final String channel;


    public WSClient(Configuration configuration, ChatAuthentication authentication) {
        super(URI.create("ws://irc-ws.chat.twitch.tv:80"));
        this.config = configuration;
        this.auth = authentication;

        channel = configuration.getChannel();
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

    }

    @Override
    public void onMessage(String ircMessage) {
        System.out.println(ircMessage);
        if ("utf8".equals(ircMessage)) {
            String rawIrcMessage = ircMessage.trim();
            System.out.println(STR."Message received (\{new Date()}): '\{rawIrcMessage}'\n");

            String[] messages = rawIrcMessage.split("\r\n"); // The IRC message may contain one or more messages.
            for (String message : messages) {
                IRCMessageParser.ParsedMessage parsedMessage = IRCMessageParser.parseMessage(message);

                if (parsedMessage != null) {
                    switch (parsedMessage.command.command) {
                        case "PRIVMSG":
                            if ("move".equals(parsedMessage.command.botCommand)) {


                            } else if ("moveoff".equals(parsedMessage.command.botCommand)) {

                            }
                            break;
                        case "PING":
                            this.send(STR."PONG \{parsedMessage.parameters}");
                            break;
                        case "001":
                            this.send(STR."JOIN \{channel}");
                            break;
                        case "JOIN":
//                            this.send("PRIVMSG " + channel + " :" + moveMessage);
                            this.send("PRIVMSG " + channel + " :Channel joint");

                            break;
                        case "PART":
                            System.out.println("The channel must have banned (/ban) the bot.");
                            this.close();
                            break;
                        case "NOTICE":
                            if ("Login authentication failed".equals(parsedMessage.parameters)) {
                                System.out.println(STR."Authentication failed; left \{channel}");

                                this.send(STR."PART \{channel}");

                            } else if ("You don’t have permission to perform that action".equals(parsedMessage.parameters)) {
                                System.out.println(STR."No permission. Check if the access token is still valid. Left \{channel}");
                                send(STR."PART \{channel}");

                            }
                            break;
                        default:
                            // Ignore all other IRC messages.
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println(STR."i=\{i}, s=\{s} b=\{b}");
    }

    @Override
    public void onError(Exception e) {
        logger.catching(e);
    }
}
