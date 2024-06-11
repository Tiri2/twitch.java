package com.ryifestudios.test;

import com.ryifestudios.twitch.Chat;
import com.ryifestudios.twitch.ChatAuthentication;
import com.ryifestudios.twitch.configuration.AuthConfiguration;
import com.ryifestudios.twitch.configuration.Configuration;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setChannel("tiri2");

        ArrayList<String> scopes = new ArrayList<>();
        scopes.add("chat:edit");
        scopes.add("chat:read");
        scopes.add("channel:moderate");

        AuthConfiguration authConfiguration = new AuthConfiguration("r1mln6qcd2m5xvgx4u6wlvgyy33c5u", "hk708ycws2swg1s2x8is5klqiqjazy", scopes);
        Chat chat = new Chat(configuration, new ChatAuthentication(authConfiguration));


    }

}
