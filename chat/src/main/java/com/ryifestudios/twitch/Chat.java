package com.ryifestudios.twitch;


import com.ryifestudios.twitch.configuration.Configuration;

public class Chat {

    WSClient client;

    public Chat(Configuration config, ChatAuthentication authentication) {

        client = new WSClient(config, authentication);



    }


}
