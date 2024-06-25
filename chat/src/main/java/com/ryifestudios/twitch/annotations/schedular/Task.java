package com.ryifestudios.twitch.annotations.schedular;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Task {

    /**
     * Every x seconds it's going to be executed
     * @return the interval in seconds
     */
    int value();

    int delay() default 0;

}
