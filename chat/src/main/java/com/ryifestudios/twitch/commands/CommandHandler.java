package com.ryifestudios.twitch.commands;

import com.ryifestudios.twitch.annotations.commands.ArgumentAno;
import com.ryifestudios.twitch.annotations.commands.BasisCommand;
import com.ryifestudios.twitch.commands.models.Argument;
import com.ryifestudios.twitch.commands.models.Command;
import com.ryifestudios.twitch.commands.models.SubCommand;
import com.ryifestudios.twitch.events.EventHandler;
import com.ryifestudios.twitch.events.impl.commands.CommandErrorEvent;
import com.ryifestudios.twitch.events.impl.commands.CommandExecutedEvent;
import com.ryifestudios.twitch.events.impl.commands.CommandNotFoundEvent;
import com.ryifestudios.twitch.exceptions.ArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Everything about commands
 */
public class CommandHandler {

    Logger logger = LogManager.getLogger("commandHandler");

    private final HashMap<String, Command> commands;
    private final HashMap<String, String> aliases;

    private final EventHandler eh;

    public CommandHandler(EventHandler eventHandler) {
        this.eh = eventHandler;

        commands = new HashMap<>();
        aliases = new HashMap<>();

        findAllCommands();
    }

    /**
     * Find all Commands by using the {@link com.ryifestudios.twitch.annotations.commands.Command} Annotation
     */
    private void findAllCommands(){
        Reflections typesAnnotatedRef = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()).setScanners(Scanners.TypesAnnotated));

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

            // Fill all aliases of the current command in the hashmap 'aliases'
            for (String a : commandAnnotation.aliases()){

                // Logic of checking if an alias already exists
                if(aliases.containsKey(a)){
                    logger.warn("Alias {} on command {} already exists", a, commandAnnotation.name());
                    continue;
                }

                aliases.put(a, commandAnnotation.name());
            }

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

            // Check if the params are higher than 1
            if(basisMethod.getParameterCount() < 1){
                logger.error("Method {} don't have enough parameters. Is CommandContext missing?", basisMethod.toGenericString());
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

            // Get Reflection for this current class (c)
            Reflections methodsAnnotatedRef = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClass(c)).setScanners(Scanners.MethodsAnnotated));
            // Get all commands annotated by SubCommand and filter with equals this current class
            Set<Method> sCommands = methodsAnnotatedRef.getMethodsAnnotatedWith(com.ryifestudios.twitch.annotations.commands.SubCommand.class).stream()
                    .filter(m -> m.getDeclaringClass().equals(c)).collect(Collectors.toSet());


            sCommands.forEach(sub -> {
                SubCommand subCmd = new SubCommand();
                LinkedList<com.ryifestudios.twitch.commands.models.Argument> arguments = new LinkedList<>();

                com.ryifestudios.twitch.annotations.commands.SubCommand subAno = sub.getAnnotation(com.ryifestudios.twitch.annotations.commands.SubCommand.class);
                subCmd.setName(subAno.name());
                subCmd.setDescription(subAno.description());

                // Check if the params are higher than 1
                if(sub.getParameterCount() < 1){
                    logger.error("SubCommand Method {} don't have enough parameters. Is CommandContext missing?", sub.toGenericString());
                    return;
                }

                // Check if the first parameter is the class CommandContext
                if(sub.getParameters()[0].getType() != CommandContext.class){
                    logger.error("Method {} don't have the Class {} as it's first parameter!", sub.toGenericString(), CommandContext.class.getTypeName());
                    return;
                }

                // Fill the Arguments
                ArgumentAno[] argsAno = subAno.arguments();
                for (ArgumentAno argument : argsAno) {
                    Argument arg = new Argument();
                    arg.setName(argument.name());
                    arg.setDescription(argument.description());
                    arguments.add(arg);
                }

                subCmd.setMethod(sub);
                subCmd.setArguments(arguments);
                subCommands.put(subCmd.getName(), subCmd);

            });

            cmd.setSubCommands(subCommands);

            // Add the command to the list - where the cmd name, is the key
            commands.put(cmd.getName(), cmd);

        });
    }

    /**
     * Execute a Command
     * <p>If not subCommand with this name found, then handle this as it's a argument</p>
     * @param commandName command that were executed
     * @param args args for the command
     */
    public void execute(CommandContext ctx, String commandName, String[] args) {
        if(commandName == null) return;

        Command cmd;
        String alias = searchCommandForAliasA(commandName);

        cmd = commands.get(Objects.requireNonNullElse(alias, commandName));

        Object instance;

        if(cmd == null){
            eh.callEvent(new CommandNotFoundEvent(ctx, commandName, args));
            return;
        }

        BasisCommand basisCommand = cmd.getBasisMethod().getAnnotation(BasisCommand.class);

        // Init the object to execute
        try {
            instance = cmd.getClazz().getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            eh.callEvent(new CommandErrorEvent(ctx, cmd, args, basisCommand, CommandErrorEvent.Reason.INSTANCE_CREATION_ERROR));
            logger.catching(e);
            return;
        }

        // If no args or subcommand is entered, invoke the basis method
        if(args.length == 0){
            // Check if the base has any arguments
            if(basisCommand.arguments().length >= 1){
//                ctx.reply(STR."\{basisCommand.arguments().length} Argument(s) are missing.");
                eh.callEvent(new CommandErrorEvent(ctx, cmd, args, basisCommand, CommandErrorEvent.Reason.ARGS_MISSING));
            }else{
                try {
                    System.out.println("Execute 1");
                    executeBasisMethod(cmd, args, instance, ctx);
                } catch (InvocationTargetException | IllegalAccessException | ArgumentException e) {
                    eh.callEvent(new CommandErrorEvent(ctx, cmd, args, basisCommand, CommandErrorEvent.Reason.EXECUTE_BASISMETHOD));
                }
            }
            return;
        }

        if(args[0].isEmpty() || args[0].isBlank()){
            System.out.println("Execute args 0");
            eh.callEvent(new CommandErrorEvent(ctx, cmd, args, basisCommand, CommandErrorEvent.Reason.ARGS_MISSING));
            return;
        }

        // get the sub command based on arg[0]
        SubCommand subCmd = cmd.getSubCommands().get(args[0]);

        // if no sub cmd found, execute the basis method with parameters
        if(subCmd == null){
            try {
                executeBasisMethod(cmd, args, instance, ctx);
            } catch (IllegalAccessException | InvocationTargetException | ArgumentException e) {
                eh.callEvent(new CommandErrorEvent(ctx, cmd, args, basisCommand, CommandErrorEvent.Reason.EXECUTE_BASISMETHOD));
            }

            return;
        }

        // Check if the arguments size of the sub cmd is bigger than the actual args in the message (-1 because sub cmd is in this array)
        if(subCmd.getArguments().size() > args.length - 1 || args[0].isBlank()){
//            ctx.reply(STR."\{subCmd.getArguments().size() - (args.length - 1)} Arguments are missing");
            eh.callEvent(new CommandErrorEvent(ctx, cmd, args, basisCommand, CommandErrorEvent.Reason.ARGS_MISSING_OF_SUBCMD));
            return;
        }

        // Set the values of the arguments based on the array args
        for (int i = 0; i < subCmd.getArguments().size(); i++) {
            Argument arg = subCmd.getArguments().get(i);
            arg.setValue(args[i + 1]);
            subCmd.getArguments().set(i, arg);
        }

        try {
            subCmd.getMethod().invoke(instance, ctx, getArgs(subCmd.getArguments()));
        }
        catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            eh.callEvent(new CommandErrorEvent(ctx, cmd, args, basisCommand, CommandErrorEvent.Reason.EXECUTE_SUBCMD));
        }

        eh.callEvent(new CommandExecutedEvent(ctx, cmd, args));
    }

    private Argument[] getArgs(LinkedList<Argument> args){
        Argument[] arguments = new Argument[args.size()];
        for (int i = 0; i < args.size(); i++) {
            arguments[i] = args.get(i);
        }
        return arguments;
    }

    private void executeBasisMethod(Command cmd, String[] args, Object o, CommandContext ctx) throws InvocationTargetException, IllegalAccessException, ArgumentException {
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
            // If no args entered, then execute it without args
            if(arguments.length == 0 || args[0].isBlank())
                cmd.getBasisMethod().invoke(o, ctx);

            // args entered, execute it with args
            cmd.getBasisMethod().invoke(o, ctx, arguments);
        }
        catch (IllegalArgumentException e){
            throw new ArgumentException(e.getMessage());
        }
    }

    /**
     * Get all Commands
     * @return all commands
     */
    public HashMap<String, Command> commands(){
        return commands;
    }

    private String searchCommandForAliasA(String a){
        if(aliases.containsKey(a)){
            return aliases.get(a);
        }
        return null;
    }
}
