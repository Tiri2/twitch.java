package com.ryifestudios.twitch.schedular.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Scheduler {

    private int interval;
    private int delay;
    private Class<?> clazz;
    private Method method;
    private Object instance;

    @Override
    public String toString() {
        return STR."Scheduler{interval=\{interval}, delay=\{delay}, clazz=\{clazz}, method=\{method}, instance=\{instance}}";
    }
}
