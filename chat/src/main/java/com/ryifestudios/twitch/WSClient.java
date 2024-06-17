package com.ryifestudios.twitch;

import com.ryifestudios.twitch.commands.CommandContext;
import com.ryifestudios.twitch.commands.CommandHandler;
import com.ryifestudios.twitch.configuration.Configuration;
import com.ryifestudios.twitch.events.EventHandler;
import com.ryifestudios.twitch.events.impl.irc.MessageEvent;
import com.ryifestudios.twitch.events.impl.irc.NoticeEvent;
import com.ryifestudios.twitch.events.impl.irc.PartEvent;
import com.ryifestudios.twitch.events.impl.irc.PingEvent;
import com.ryifestudios.twitch.events.impl.websocket.WebSocketCloseEvent;
import com.ryifestudios.twitch.events.impl.websocket.WebSocketConnectedEvent;
import com.ryifestudios.twitch.events.impl.websocket.WebSocketErrorEvent;
import com.ryifestudios.twitch.parser.IRCMessageParser;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;

public class WSClient extends org.java_websocket.client.WebSocketClient {

    private final Logger logger = LogManager.getLogger("WS");

    @Getter
    @Accessors(fluent = true)
    private final Configuration config;

    @Setter
    private ChatAuthentication auth;

    private final CommandHandler commandHandler;
    private final EventHandler eventHandler;


    public WSClient(Configuration configuration, CommandHandler cmdHandler, EventHandler eventHandler) {
        super(URI.create("ws://irc-ws.chat.twitch.tv:80"));
        this.config = configuration;
        this.commandHandler = cmdHandler;
        this.eventHandler = eventHandler;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("websocket client started");

        this.send("CAP REQ :twitch.tv/tags twitch.tv/commands");
        this.send(STR."PASS oauth:\{auth.accessToken().getAccessToken()}");
        this.send(STR."NICK \{config.getNick()}");
        this.send(STR."JOIN #\{config.getChannel()}");

        logger.info("sent pass, nick and join message to the server");

        eventHandler.callEvent(new WebSocketConnectedEvent(serverHandshake));
    }

    @Override
    public void onMessage(String ircMessage) {
        String rawIrcMessage = ircMessage.trim();
        String[] messages = rawIrcMessage.split("\r\n"); // The IRC message may contain one or more messages.
        for (String message : messages) {
            IRCMessageParser.ParsedMessage parsedMessage = IRCMessageParser.parseMessage(message);
            logger.debug(STR."Parsed Message: \{parsedMessage}");

            if (parsedMessage != null) {
                switch (parsedMessage.getCommand().getMethod()) {
                    case "PRIVMSG":

                        // If a command is entered in chat, execute it in command handler
                        if(parsedMessage.getCommand().getBotCommand() != null)
                            commandHandler.execute(new CommandContext(this, parsedMessage.getTags()), parsedMessage.getCommand().getBotCommand(), parsedMessage.getCommand().getBotCommandParams());

                        // call message event
                        eventHandler.callEvent(new MessageEvent(new CommandContext(this, parsedMessage.getTags()), parsedMessage.getParameters()));
                        break;
                    case "PING":
                        eventHandler.callEvent(new PingEvent(new CommandContext(this, parsedMessage.getTags()), parsedMessage));
                        logger.info("pinged");
                        this.send(STR."PONG \{parsedMessage.getParameters()}");
                        break;
                    case "001":
                        this.send(STR."JOIN \{config.getChannel()}");
                        break;
                    case "JOIN":
                        System.out.println("Joined chat");
                        logger.info(STR."joined chat for \{config.getChannel()}");
                        break;
                    case "PART":
                        eventHandler.callEvent(new PartEvent(new CommandContext(this, parsedMessage.getTags()), parsedMessage));

                        logger.info("The channel must have banned (/ban) the bot.");
                        this.close();
                        break;
                    case "NOTICE":
                        eventHandler.callEvent(new NoticeEvent(new CommandContext(this, parsedMessage.getTags()), parsedMessage));

                        if ("Login authentication failed".equals(parsedMessage.getParameters())) {
                            logger.info(STR."Authentication failed; left \{config.getChannel()}");

                            this.send(STR."PART \{config.getChannel()}");

                        } else if ("You don’t have permission to perform that action".equals(parsedMessage.getParameters())) {
                            logger.info(STR."No permission. Check if the access token is still valid. Left \{config.getChannel()}");
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
        eventHandler.callEvent(new WebSocketCloseEvent(new CommandContext(this, new HashMap<>()), i, s, b));
        this.reconnect();
    }

    @Override
    public void onError(Exception e) {
        eventHandler.callEvent(new WebSocketErrorEvent(new CommandContext(this, new HashMap<>()), e));
        logger.catching(e);
    }

}
