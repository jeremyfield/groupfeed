package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.Properties.DiscordPropertiesReader;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.*;

public class CommandHandler implements MessageCreateListener {

    private Map<String, Command> commandMap;
    private String commandPrefix;

    public CommandHandler() {
        commandMap = new HashMap<>();
        commandPrefix = DiscordPropertiesReader.getCommandPrefix();
        if(commandPrefix == null) {
            commandPrefix = "!";
        }
    }

    public void addCommand(String commandTrigger, Command command) {
        commandMap.put(commandTrigger, command);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        String[] argArray = event.getMessageContent().split(" ");
        if(argArray.length == 0 || !argArray[0].startsWith(this.commandPrefix)) {
            return;
        }

        String commandString = argArray[0].substring(1).toLowerCase();
        List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
        argsList.remove(0);
        if(commandMap.containsKey(commandString)) {
            commandMap.get(commandString).runCommand(event, argsList);
        }
    }
}
