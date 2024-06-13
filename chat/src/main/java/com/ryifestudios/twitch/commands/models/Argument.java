package com.ryifestudios.twitch.commands.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Argument {

    private String name;
    private String description;
    private Object value;

    @Override
    public String toString() {
        return value.toString();
    }
}
