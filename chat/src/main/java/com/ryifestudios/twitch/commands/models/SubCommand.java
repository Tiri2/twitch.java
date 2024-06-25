package com.ryifestudios.twitch.commands.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.LinkedList;

@Getter
@Setter
@AllArgsConstructor
public class SubCommand {

    private String name;
    private String description;
    private Method method;
    private LinkedList<Argument> arguments;

    public SubCommand() {
        arguments = new LinkedList<>();
    }
}
