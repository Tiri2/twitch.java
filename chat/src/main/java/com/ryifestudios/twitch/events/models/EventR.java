package com.ryifestudios.twitch.events.models;

import com.ryifestudios.twitch.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventR {

    private Method method;
    private Class<?> clazz;
    private Class<? extends Event> eventClass;

    @Override
    public String toString() {
        return STR."EventR{method=\{method}, clazz=\{clazz}, eventClass=\{eventClass}\{'}'}";
    }
}
