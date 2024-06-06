package com.ryifestudios.twitch.commands;

import java.lang.reflect.Method;

public class SubCommand {

    String name;
    String description;
    Class<?> clazz;
    Method method;

    public SubCommand(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

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

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
