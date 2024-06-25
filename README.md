# Twitch.java

Simple implementation of the [twitch api](https://dev.twitch.tv/) in java

## Modules

This repo is divided into several modules.

* [Chat]() [[L](https://dev.twitch.tv/docs/irc/)] - Communicate with the twitch's irc chat
* [EventSub]() [[L](https://dev.twitch.tv/docs/eventsub/)] - Implementation soon
* [Authentication]() [[L](https://dev.twitch.tv/docs/authentication/)] - Authenticate with the twitch api

# Install

## Via pom.xml

> Note that freshly published versions may take some time to sync to all other maven repositories.

**Install all modules**
```
<properties>
    <twitch.java.version>{replace this wit current version number}</twitch.java.version>
</properties>

<dependencies>
    <dependency>
        <groupId>com.ryifestudios.twitch</groupId>
        <artifactId>chat</artifactId>
        <version>${twitch.java.version}</version>
    </dependency>
    <dependency>
        <groupId>com.ryifestudios.twitch</groupId>
        <artifactId>authentication</artifactId>
        <version>${twitch.java.version}</version>
    </dependency>
</dependencies>
```

**Install chat module**
```
<dependency>
    <groupId>com.ryifestudios.twitch</groupId>
    <artifactId>chat</artifactId>
    <version>{currentVersion}</version>
</dependency>
```

**Install authentication**
```
<dependency>
    <groupId>com.ryifestudios.twitch</groupId>
    <artifactId>authentication</artifactId>
    <version>{currentVersion}</version>
</dependency>
```

## from Source (unstable deveopment versions)

### Requirements

- git
- maven
- java 22
- installed and configured a primary gpg key

### Installation

```Shell
git clone https://github.com/Tiri2/twitch.java.git
cd twitch.java/
mvn install
```

# Wiki
A wiki will be written soon. Sorry :/

# Examples
U can see all the examples in the [examples](https://github.com/Tiri2/twitch.java/tree/master/examples) directory or repository¹.

1 = Available soon!

Here a few examples of creating a command with sub commands and listening on an event

## Commands

### Basis Method

Here is a small introduction how to make a command called 'coinflip'.
```java
import com.ryifestudios.twitch.annotations.commands.*; // Only ArgumentAno, BasisCommand and Command are needed
import com.ryifestudios.twitch.commands.CommandContext; // CommandContext is used to reply to a command or get information's like user
import com.ryifestudios.twitch.commands.models.Argument;

// This Annotation describe the command
@Command(name = "coinflip", description = "flip a coin :<|", aliases={"cf"})
public class CoinsflipCommand {

    // Just an instance of random, because we need a random number
    private final Random random;

    public CoinsflipCommand() {
        random = new Random();
    }

    // This annotation marks the basis method - this is executed, when no sub command is entered
    @BasisCommand(arguments = {
            @ArgumentAno(name = "bet", description = "bet if head or tail") // Argument of this base method
    })
    public void onCoinsflip(CommandContext ctx, Argument[] args) {
        int v = random.nextInt(10);

        // Say if the value is head (over 5)
        boolean head = v > 5;

        // Get the first argument's value and check if it's head and the bool head is true then ...
        if(args[0].getValue().equals("head") && head){
            ctx.reply("You have won");
            return;
        }

        ctx.reply("You haven't won");
    }

}
```

### Subcommands

Here is a small introduction to make a command with a sub command¹.
In this case we have two sub commands, called tel & shutdown. Both Sub Commands have the Annotation 
`@SubCommand` to declare the method that should be executed.

¹ = Sub Commands are basically under commands of a main command.

```java
import com.ryifestudios.twitch.annotations.commands.*; // Only SubCommand, BasisCommand and Command are needed
import com.ryifestudios.twitch.commands.CommandContext; // CommandContext is used to reply to a command or get information's like user

// This Annotation describe the command
@Command(name = "abc", description = "THIS IS THE AWESOME ABC COMMAND")
public class ABCCommand  {

    // This annotation marks the basis method - this is executed, when no valid sub command is entered
    @BasisCommand()
    public void call(CommandContext ctx){
        ctx.send("abc command called");
    }

    // This is a SubCommand, parameter name is required
    @SubCommand(name = "tel")
    public void tel(CommandContext ctx){
        System.out.println("tel");
        ctx.send(STR."@\{ctx.getUser().getDisplayName()} has entered this subcommand.");
    }

    // This is also a sub Command
    @SubCommand(name="shutdown")
    public void shutdown(CommandContext ctx){
        ctx.reply("Bot will now shutdown! Bye bye");
        System.exit(1000);
    }

}
```

## Events

Executing an event is easy like a 1 + 1, the only thing that is needed, is the Annotation `@Event` and the Event as a method parameter like so:

A list of all Events are available soon. Sorry for that!

```java
import com.ryifestudios.twitch.annotations.event.Event; // Required Event Annotation 

// Importing the Events u want
import com.ryifestudios.twitch.events.impl.commands.CommandErrorEvent;
import com.ryifestudios.twitch.events.impl.commands.CommandExecutedEvent;
import com.ryifestudios.twitch.events.impl.commands.CommandNotFoundEvent;

public class Test {

    /**
     * This Event is triggered when a Command failed while executing
     * @param error
     */
    @Event
    public void onCommandError(CommandErrorEvent error){
        error.ctx().reply("error concurred");
    }

    /**
     * The event is triggered if a command is not found
     * @param c
     */
    @Event
    public void onCommandNotFound(CommandNotFoundEvent c){
        c.ctx().reply(STR."Command \{c.getCommandName()} not found");
    }

    /**
     * This Event is triggered when a command is successfully executed
     * @param c
     */
    @Event
    public void executed(CommandExecutedEvent c){
        System.out.println(STR."\{c.getCommand()} executed");
    }
    
}
```

# [Donate me](https://paypal.me/tiri2)