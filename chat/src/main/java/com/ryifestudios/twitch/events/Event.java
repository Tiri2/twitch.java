package com.ryifestudios.twitch.events;

public abstract class Event {

    private String id;
    private String description;

    public abstract void execute();


    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
