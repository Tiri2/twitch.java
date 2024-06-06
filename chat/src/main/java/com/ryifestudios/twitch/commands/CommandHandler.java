package com.ryifestudios.twitch.commands;

import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class CommandHandler {

    Logger logger = LogManager.getLogger("commandHandler");

    private final HashMap<String, Command> commands;

    public CommandHandler(String prefix) {
        commands = new HashMap<>();

        findAllCommands(prefix);
    }

    /**
     * Find all Commands by using the {@link com.ryifestudios.twitch.annotations.commands.Command} Annotation
     */
    private void findAllCommands(String prefix){
        Reflections typesAnnotatedRef = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(prefix)).setScanners(Scanners.TypesAnnotated));
//        Reflections methodsAnnotatedRef = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(prefix)).setScanners(Scanners.MethodsAnnotated));

        Set<Class<?>> cmds = typesAnnotatedRef.getTypesAnnotatedWith(com.ryifestudios.twitch.annotations.commands.Command.class);

        cmds.forEach(c -> {

            Command cmd = new Command();
            // set the class for the command
            cmd.setClazz(c);

            // set important infos
            com.ryifestudios.twitch.annotations.commands.Command commandAnnotation = c.getAnnotation(com.ryifestudios.twitch.annotations.commands.Command.class);
            cmd.setName(commandAnnotation.name());
            cmd.setDescription(commandAnnotation.description());


            /*
            ##########################################

                        BasisCommand Method

            ##########################################
             */

            // Add all Methods annotated with BasisCommand to the list tempMethods
            LinkedList<Method> tempMethods = new LinkedList<>();
            Arrays.stream(c.getMethods()).forEach(m -> {
                if(m.isAnnotationPresent(BasisCommand.class)){
                    tempMethods.add(m);
                }
            });

            Method basisMethod = tempMethods.getFirst();

            // If there's more than 1 element in the list, log a warn
            if(tempMethods.size() > 1){
                logger.warn("You have two Methods with {} annotated - using the first one {}", BasisCommand.class.getName(), basisMethod.getName());
            }

            // Check if the params are higher than 1 TODO: improve params
            if(basisMethod.getParameterCount() < 1){
                logger.error("Method {} don't have enough parameters. Is CommandContext missing?", STR."\{basisMethod.getClass().getName()}.\{basisMethod.getName()}");
                System.out.println("error - 0 params");
                return; // foreach instead of continue
            }

            // Then finally set the basis method in cmd
            cmd.setBasisMethod(tempMethods.getFirst());

            System.out.println("passed " + STR."\{basisMethod.getClass().getName()}.\{basisMethod.getName()}");

            // Add the command to the list - where the cmd name, is the key
            commands.put(cmd.getName(), cmd);

        });
    }

    public HashMap<String, Command> commands(){
        return commands;
    }
}
