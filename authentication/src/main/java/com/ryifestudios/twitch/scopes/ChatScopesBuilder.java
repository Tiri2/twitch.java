package com.ryifestudios.twitch.scopes;

import org.apache.logging.log4j.LogManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ChatScopesBuilder {

    private final ArrayList<String> scopes;

    public ChatScopesBuilder(){
        scopes = new ArrayList<>();
    }

    public ChatScopesBuilder withChannelBot(){
        scopes.add("channel:bot");
        return this;
    }

    public ChatScopesBuilder withChannelModerate(){
        scopes.add("channel:moderate");
        return this;
    }

    public ChatScopesBuilder withChatEdit(){
        scopes.add("chat:edit");
        return this;
    }

    public ChatScopesBuilder withChatRead(){
        scopes.add("chat:read");
        return this;
    }


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

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
