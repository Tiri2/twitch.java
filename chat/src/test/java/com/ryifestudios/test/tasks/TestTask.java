package com.ryifestudios.test.tasks;

import com.ryifestudios.twitch.annotations.schedular.Task;
import com.ryifestudios.twitch.schedular.TaskContext;

public class TestTask {

    @Task(value = 60, delay = 100)
    public void run(TaskContext ctx){
        System.out.println("run");
//        ctx.send("WERBUNG! Für nur 015i913096136€ könnt ihr euch im Shop eine Niere kaufen!");
    }

    @Task(30)
    public void run2(TaskContext ctx){
        ctx.send("Every 30 Seconds");
    }

}
