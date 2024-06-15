package com.ryifestudios.twitch.annotations.commands;

public @interface ArgumentAno {

    String name();
    String description();

    boolean optional() default false;

}
