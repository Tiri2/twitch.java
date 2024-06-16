package com.ryifestudios.twitch.events;

import com.ryifestudios.twitch.events.models.EventR;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EventHandler {

    private final Logger logger = LogManager.getLogger();

    private final LinkedList<EventR> events;

    public EventHandler() {

        this.events = new LinkedList<>();

        fillEvents();
    }

    private void fillEvents(){
        Reflections ref = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()).setScanners(Scanners.MethodsAnnotated));
        Set<Method> e = ref.getMethodsAnnotatedWith(com.ryifestudios.twitch.annotations.event.Event.class);

        logger.debug(e);

        e.forEach(m -> {
            EventR eventR = new EventR();

            eventR.setClazz(m.getDeclaringClass());
            eventR.setMethod(m);

            if(m.getParameterCount() < 1){
                logger.error("Event {} has not enough parameters", m.toGenericString());
                return;
            }

            if(m.getParameters()[0].getType().getSuperclass() != Event.class){
                logger.error("Event {} has not superclass {} as it's first parameter", m.toGenericString(), Event.class.getName());
                return;
            }

            eventR.setEventClass(m.getParameters()[0].getType().asSubclass(Event.class));

            events.add(eventR);
        });

    }

    public void callEvent(Event event){
        List<EventR> eventsToExecute = events.stream()
                .filter(r -> r.getEventClass() == event.getClass()).toList();

        eventsToExecute.forEach(e -> {

            Object instance;
            Method m = e.getMethod();

            try {
                instance = e.getClazz().getConstructor().newInstance();
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                logger.catching(ex);
                return;
            }

            try {
                m.invoke(instance, event);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                logger.catching(ex);
            }


        });

    }

}
