package com.ryifestudios.twitch.commands;

import com.ryifestudios.twitch.WSClient;
import com.ryifestudios.twitch.annotations.commands.ArgumentAno;
import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.commands.models.Argument;
import com.ryifestudios.twitch.commands.models.Command;
import com.ryifestudios.twitch.commands.models.SubCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Everything about commands
 */
public class CommandHandler {

    Logger logger = LogManager.getLogger("commandHandler");

    private final HashMap<String, Command> commands;

    public CommandHandler() {
        commands = new HashMap<>();

        findAllCommands();
    }

    /**
     * Find all Commands by using the {@link com.ryifestudios.twitch.annotations.commands.Command} Annotation
     */
    private void findAllCommands(){
        Reflections typesAnnotatedRef = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()).setScanners(Scanners.TypesAnnotated));
        Reflections methodsAnnotatedRef = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()).setScanners(Scanners.MethodsAnnotated));

        Set<Class<?>> cmds = typesAnnotatedRef.getTypesAnnotatedWith(com.ryifestudios.twitch.annotations.commands.Command.class);

        cmds.forEach(c -> {

            // Create the command object to set the data and save it in the list
            Command cmd = new Command();
            HashMap<String, SubCommand> subCommands = new HashMap<>();

            // set important infos
            com.ryifestudios.twitch.annotations.commands.Command commandAnnotation = c.getAnnotation(com.ryifestudios.twitch.annotations.commands.Command.class);
            cmd.setName(commandAnnotation.name());
            cmd.setDescription(commandAnnotation.description());
            cmd.setClazz(c);


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

            // If there's more than 1 element in the list, log warn
            if(tempMethods.size() > 1){
                logger.warn("You have two Methods with {} annotated - using {}", BasisCommand.class.getName(), basisMethod.toGenericString());
            }

            // TODO: maybe thrown a exception for logger.error things

            // Check if the params are higher than 1 TODO: improve params
            if(basisMethod.getParameterCount() < 1){
                logger.error("Method {} don't have enough parameters. Is CommandContext missing?", basisMethod.toGenericString());
                System.out.println("error - 0 params");
                return;
            }

            // Check if the first parameter is the class CommandContext
            if(basisMethod.getParameters()[0].getType() != CommandContext.class){
                logger.error("Method {} don't have the Class {} as it's first parameter!", basisMethod.toGenericString(), CommandContext.class.getTypeName());
                return;
            }

            // Then finally set the basis method in cmd
            cmd.setBasisMethod(tempMethods.getFirst());

            /*
            ##########################################

                        SubCommands Method

            ##########################################
             */

            Set<Method> sCommands = methodsAnnotatedRef.getMethodsAnnotatedWith(com.ryifestudios.twitch.annotations.commands.SubCommand.class);

            sCommands.forEach(sub -> {
                SubCommand subCmd = new SubCommand();
                LinkedList<com.ryifestudios.twitch.commands.models.Argument> arguments = new LinkedList<>();

                com.ryifestudios.twitch.annotations.commands.SubCommand subAno = sub.getAnnotation(com.ryifestudios.twitch.annotations.commands.SubCommand.class);
                subCmd.setName(subAno.name());
                subCmd.setDescription(subAno.description());

                // Fill the Arguments
                ArgumentAno[] argsAno = subAno.arguments();
                for (ArgumentAno argument : argsAno) {
                    Argument arg = new Argument();
                    arg.setName(argument.name());
                    arg.setDescription(argument.description());
                    arguments.add(arg);
                }

                subCmd.setArguments(arguments);
                subCommands.put(subCmd.getName(), subCmd);

            });

            cmd.setSubCommands(subCommands);

            // Add the command to the list - where the cmd name, is the key
            commands.put(cmd.getName(), cmd);

        });
    }

    /**
     *
     *
     * If not subCommand with this name found, then handle this as it's a argument
     * @param commandName command that were executed
     * @param args args for the command
     */
    public void execute(String commandName, String[] args, CommandContext ctx){
        if(commandName == null) return;

        Command cmd = commands.get(commandName);

        Object o;

        if(cmd == null){
            ctx.send(STR."Command \{commandName} not found");
            return;
        }

        // Init the object to execute
        try {
            o = cmd.getClazz().getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e); // TODO: better error handling
        }

        // If no args or subcommand is entered, invoke the basis method
        if(args.length == 0){
            BasisCommand basisCommand = cmd.getBasisMethod().getAnnotation(BasisCommand.class);
            if(basisCommand.arguments().length >= 1){
                ctx.reply(STR."\{basisCommand.arguments().length} Argument(s) are missing."); // Todo fire event instead send hard coded message
            }
            return;
        }

        // get the sub command based on arg[0]
        SubCommand subCmd = cmd.getSubCommands().get(args[0]);

        // if no sub cmd found, execute the basis method with parameters
        if(subCmd == null){
            try {
                executeBasisMethod(cmd, args, o, ctx);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e); // TODO: better error handling
            }

            return;
        }

        // Set the values of the arguments based on the array args
        for (int i = 1; i < subCmd.getArguments().size(); i++) {
            Argument arg = subCmd.getArguments().get(i);
            arg.setValue(args[i]);
            subCmd.getArguments().set(i, arg);
        }

        try {
            subCmd.getMethod().invoke(o, ctx, subCmd.getArguments());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    private void executeBasisMethod(Command cmd, String[] args, Object o, CommandContext ctx) throws InvocationTargetException, IllegalAccessException {
        BasisCommand basisCommand = cmd.getBasisMethod().getAnnotation(BasisCommand.class);

        Argument[] arguments = new Argument[basisCommand.arguments().length];

        for (int i = 0; i < basisCommand.arguments().length; i++) {
            ArgumentAno ano = basisCommand.arguments()[i];
            Argument arg = new Argument();
            arg.setValue(args[i]);
            arg.setName(ano.name());
            arg.setDescription(ano.description());
            arguments[i] = arg;
        }

        try{
            cmd.getBasisMethod().invoke(o, ctx, arguments);
        }catch (IllegalArgumentException e){
            logger.catching(e);
        }
    }


    public HashMap<String, Command> commands(){
        return commands;
    }
}
