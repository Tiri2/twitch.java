package com.ryifestudios.twitch.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.LinkedList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    private String name;

    private String description;

    private Method basisMethod;

    private Class<?> clazz;

    private LinkedList<SubCommand> subCommands;
}
