package com.ryifestudios.twitch.schedular;

import com.ryifestudios.twitch.WSClient;
import com.ryifestudios.twitch.annotations.schedular.Task;
import com.ryifestudios.twitch.schedular.models.Scheduler;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SchedulerHandler {

    private static final Logger log = LogManager.getLogger(SchedulerHandler.class);
    private final LinkedList<Scheduler> schedulers;

    private final Timer timer;

    @Setter
    private WSClient wsClient;


    public SchedulerHandler() {
        schedulers = new LinkedList<>();
        timer = new Timer();

        fillSchedulers();
    }

    private void fillSchedulers() {
        Reflections methodsAnnoRef = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()).setScanners(Scanners.MethodsAnnotated));
        Set<Method> methods = methodsAnnoRef.getMethodsAnnotatedWith(Task.class);

        methods.forEach(m -> {

            // Check if the params are higher than 1
            if(m.getParameterCount() < 1){
                log.error("Method {} don't have enough parameters. Is CommandContext missing?", m.toGenericString());
                return;
            }

            // Check if the first parameter is the class CommandContext
            if(m.getParameters()[0].getType() != TaskContext.class){
                log.error("Method {} don't have the Class {} as it's first parameter!", m.toGenericString(), TaskContext.class.getTypeName());
                return;
            }

            Scheduler scheduler = new Scheduler();
            scheduler.setMethod(m);
            scheduler.setClazz(m.getDeclaringClass());
            scheduler.setInterval(m.getAnnotation(Task.class).value());

            try {
                scheduler.setInstance(m.getDeclaringClass().getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                log.error(STR."Error while instantiating schedular \{scheduler}", e);
            }



            schedulers.add(scheduler);
        });
    }

    /**
     * Start the executing of all tasks
     */
    public void start(){

        TaskContext ctx = new TaskContext(wsClient);

        schedulers.forEach(e -> {
            timer.scheduleAtFixedRate(new TaskExecution(e, e.getInstance(), ctx), e.getDelay(), TimeUnit.MILLISECONDS.convert(e.getInterval(), TimeUnit.SECONDS));
            log.debug("create timer for {}", e);
        });

    }

    /**
     * Timer task which invoke the method
     */
    private static class TaskExecution extends TimerTask {

        private final Scheduler element;
        private final Object instance;
        private final TaskContext ctx;

        public TaskExecution(Scheduler element, Object instance, TaskContext ctx) {
            this.element = element;
            this.instance = instance;
            this.ctx = ctx;
        }

        @Override
        public void run() {

            if(instance == null) return;



            try {
                element.getMethod().invoke(instance, ctx);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.catching(e);
            }
        }
    }

}
