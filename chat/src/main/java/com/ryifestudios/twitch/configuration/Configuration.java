package com.ryifestudios.twitch.configuration;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    private String channel;
    private String nick;

}
