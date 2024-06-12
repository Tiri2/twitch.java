package com.ryifestudios.twitch.scopes;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 *
 * <p>A helper class for easier building scopes for the chatbot </p>
 *
 * Related things:
 * <ul>
 *     <li>
 *         <a href="https://dev.twitch.tv/docs/authentication/scopes/#chat-and-pubsub-scopes">Twitch API</a>
 *     </li>
 * </ul>
 */
@Getter
public class ChatScopesBuilder {

    private final ArrayList<String> scopes;

    public ChatScopesBuilder(){
        scopes = new ArrayList<>();
    }

    /**
     * Add the channel:bot scope
     */
    public ChatScopesBuilder withChannelBot(){
        scopes.add("channel:bot");
        return this;
    }

    /**
     * Add the channel:moderate scope
     */
    public ChatScopesBuilder withChannelModerate(){
        scopes.add("channel:moderate");
        return this;
    }

    /**
     * Add the chat:edit scope
     */
    public ChatScopesBuilder withChatEdit(){
        scopes.add("chat:edit");
        return this;
    }

    /**
     * Add the chat:read scope
     */
    public ChatScopesBuilder withChatRead(){
        scopes.add("chat:read");
        return this;
    }

    /**
     * Build the Scopes
     * @return a String formated as a query string
     */
    public String build(){
        StringBuilder sb = new StringBuilder();

        scopes.forEach(s -> {
            try {
                sb.append("+");
                sb.append(encodeValue(s));
            } catch (UnsupportedEncodingException e) {
                LogManager.getLogger("ChatScopesBuilder").catching(e);
            }
        });

        return sb.deleteCharAt(0).toString();
    }

    /**
     * Helper method to encode the scopes
     * @param value single scope in the list
     * @return the encoded String
     */
    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
