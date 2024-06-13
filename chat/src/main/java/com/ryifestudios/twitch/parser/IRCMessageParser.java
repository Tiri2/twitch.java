package com.ryifestudios.twitch.parser;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class IRCMessageParser {

    @Getter
    @Setter
    public static class ParsedMessage {

        private Map<String, Object> tags;
        private Source source;
        private Command command;
        private String parameters;

        public ParsedMessage() {
            this.tags = null;
            this.source = null;
            this.command = null;
            this.parameters = null;
        }

        @Override
        public String toString() {
            return STR."ParsedMessage{tags=\{tags}, source=\{source}, command=\{command}, parameters='\{parameters}'}";
        }
    }

    @Getter
    @Setter
    public static class Source {
        private String nick;
        private String host;

        public Source(String nick, String host) {
            this.nick = nick;
            this.host = host;
        }

        @Override
        public String toString() {
            return STR."Source{nick='\{nick}', host='\{host}'}";
        }
    }

    @Getter
    @Setter
    public static class Command {
        private String command;
        private String channel;
        private Boolean isCapRequestEnabled;
        private String botCommand;
        private String botCommandParams;

        public Command() {
            this.command = null;
            this.channel = null;
            this.isCapRequestEnabled = null;
            this.botCommand = null;
            this.botCommandParams = null;
        }

        @Override
        public String toString() {
            return STR."Command{command='\{command}', channel='\{channel}', isCapRequestEnabled=\{isCapRequestEnabled}, botCommand='\{botCommand}', botCommandParams='\{botCommandParams}'}";
        }
    }

    /**
     * Parse a websocket message to the object
     * @param message
     * @return
     */
    public static ParsedMessage parseMessage(String message) {
        ParsedMessage parsedMessage = new ParsedMessage();

        int idx = 0;

        String rawTagsComponent = null;
        String rawSourceComponent = null;
        String rawParametersComponent = null;

        if (message.charAt(idx) == '@') {
            int endIdx = message.indexOf(' ');
            rawTagsComponent = message.substring(1, endIdx);
            idx = endIdx + 1;
        }

        if (message.charAt(idx) == ':') {
            idx += 1;
            int endIdx = message.indexOf(' ', idx);
            rawSourceComponent = message.substring(idx, endIdx);
            idx = endIdx + 1;
        }

        int endIdx = message.indexOf(':', idx);
        if (endIdx == -1) {
            endIdx = message.length();
        }

        String rawCommandComponent = message.substring(idx, endIdx).trim();

        if (endIdx != message.length()) {
            idx = endIdx + 1;
            rawParametersComponent = message.substring(idx);
        }

        parsedMessage.setCommand(parseCommand(rawCommandComponent));

        if (parsedMessage.command == null) {
            System.out.println("DEBUG: return null");
            return null;
        } else {
            if (rawTagsComponent != null) {
                parsedMessage.tags = parseTags(rawTagsComponent);
            }

            parsedMessage.source = parseSource(rawSourceComponent);

            parsedMessage.parameters = rawParametersComponent;
            if (rawParametersComponent != null && rawParametersComponent.charAt(0) == '!') {
                parsedMessage.command = parseParameters(rawParametersComponent, parsedMessage.command);
            }
        }

        return parsedMessage;
    }

    public static Map<String, Object> parseTags(String tags) {
        Map<String, Object> dictParsedTags = new HashMap<>();

        String[] parsedTags = tags.split(";");

        for (String tag : parsedTags) {
            String[] parsedTag = tag.split("=");
            String tagValue = parsedTag.length > 1 && !parsedTag[1].isEmpty() ? parsedTag[1] : null;

            switch (parsedTag[0]) {
                case "badges":
                case "badge-info":
                    if (tagValue != null) {
                        Map<String, String> dict = new HashMap<>();
                        String[] badges = tagValue.split(",");
                        for (String pair : badges) {
                            String[] badgeParts = pair.split("/");
                            dict.put(badgeParts[0], badgeParts[1]);
                        }
                        dictParsedTags.put(parsedTag[0], dict);
                    } else {
                        dictParsedTags.put(parsedTag[0], null);
                    }
                    break;
                case "emotes":
                    if (tagValue != null) {
                        Map<String, List<Map<String, String>>> dictEmotes = new HashMap<>();
                        String[] emotes = tagValue.split("/");
                        for (String emote : emotes) {
                            String[] emoteParts = emote.split(":");
                            List<Map<String, String>> textPositions = new ArrayList<>();
                            String[] positions = emoteParts[1].split(",");
                            for (String position : positions) {
                                String[] positionParts = position.split("-");
                                Map<String, String> pos = new HashMap<>();
                                pos.put("startPosition", positionParts[0]);
                                pos.put("endPosition", positionParts[1]);
                                textPositions.add(pos);
                            }
                            dictEmotes.put(emoteParts[0], textPositions);
                        }
                        dictParsedTags.put(parsedTag[0], dictEmotes);
                    } else {
                        dictParsedTags.put(parsedTag[0], null);
                    }
                    break;
                case "emote-sets":
                    List<String> emoteSetIds = Arrays.asList(tagValue.split(","));
                    dictParsedTags.put(parsedTag[0], emoteSetIds);
                    break;
                default:
                    if (!"client-nonce".equals(parsedTag[0]) && !"flags".equals(parsedTag[0])) {
                        dictParsedTags.put(parsedTag[0], tagValue);
                    }
                    break;
            }
        }

        return dictParsedTags;
    }

    public static Command parseCommand(String rawCommandComponent) {
        Command parsedCommand = new Command();
        String[] commandParts = rawCommandComponent.split(" ");

        switch (commandParts[0]) {
            case "PRIVMSG", "001", "GLOBALUSERSTATE", "USERSTATE", "ROOMSTATE", "HOSTTARGET", "CLEARCHAT", "NOTICE", "PART", "JOIN":
                parsedCommand.command = commandParts[0];
                parsedCommand.channel = commandParts[1];
                break;
            case "PING":
                parsedCommand.command = commandParts[0];
                break;
            case "CAP":
                parsedCommand.command = commandParts[0];
                parsedCommand.isCapRequestEnabled = "ACK".equals(commandParts[2]);
                break;

            case "RECONNECT":
                System.out.println("The Twitch IRC server is about to terminate the connection for maintenance.");
                parsedCommand.command = commandParts[0];
                break;
            case "421":
                System.out.println(STR."Unsupported IRC command: \{commandParts[2]}");
                return null;
            case "002":
            case "003":
            case "004":
            case "353":
            case "366":
            case "372":
            case "375":
            case "376":
                System.out.println(STR."numeric message: \{commandParts[0]}");
                return null;
            default:
                System.out.println(STR."\nUnexpected command: \{commandParts[0]}\n");
                return null;
        }

        return parsedCommand;
    }

    public static Source parseSource(String rawSourceComponent) {
        if (rawSourceComponent == null) {
            return null;
        } else {
            String[] sourceParts = rawSourceComponent.split("!");
            return new Source(sourceParts.length == 2 ? sourceParts[0] : null, sourceParts.length == 2 ? sourceParts[1] : sourceParts[0]);
        }
    }

    public static Command parseParameters(String rawParametersComponent, Command command) {
        String commandParts = rawParametersComponent.substring(1).trim();
        int paramsIdx = commandParts.indexOf(' ');

        if (paramsIdx == -1) {
            command.botCommand = commandParts;
        } else {
            command.botCommand = commandParts.substring(0, paramsIdx);
            command.botCommandParams = commandParts.substring(paramsIdx).trim();
        }

        return command;
    }
}
