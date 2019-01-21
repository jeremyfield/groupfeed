package com.turtles.groupfeed.Commands;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.*;

public class CommandHandler {

    private static Map<String, Command> commandMap;

    public CommandHandler() {
        commandMap = new HashMap<>();
    }

    public void addCommand(String commandTrigger, Command command) {
        commandMap.put(commandTrigger, command);
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] argArray = event.getMessage().getContent().split(" ");
        if(argArray.length == 0 || !argArray[0].startsWith("!")) {
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
