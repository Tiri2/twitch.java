package com.ryifestudios.twitch.commands;

import java.lang.reflect.Method;
import java.util.LinkedList;

public class Command {

    private String name;

    private String description;

    private Method basisMethod;

    private Class<?> clazz;

    private LinkedList<SubCommand> subCommands;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Method getBasisMethod() {
        return basisMethod;
    }

    public void setBasisMethod(Method basisMethod) {
        this.basisMethod = basisMethod;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
