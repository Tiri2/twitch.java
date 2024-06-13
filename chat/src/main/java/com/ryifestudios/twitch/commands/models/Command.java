package com.ryifestudios.twitch.commands.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

@Getter
@Setter
@AllArgsConstructor
public class Command {

    private String name;
    private String description;
    private Method basisMethod;
    private HashMap<String, SubCommand> subCommands;

    private Class<?> clazz;

    public Command() {
        subCommands = new HashMap<>();
    }

    @Override
    public String toString() {
        return STR."Command{name='\{name}', description='\{description}', basisMethod=\{basisMethod}, subCommands=\{subCommands}}";
    }
}
