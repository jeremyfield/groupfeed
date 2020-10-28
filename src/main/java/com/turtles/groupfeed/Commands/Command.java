package com.turtles.groupfeed.Commands;

import org.javacord.api.event.message.MessageCreateEvent;

import java.util.List;

public interface Command {

    void runCommand(MessageCreateEvent event, List<String> args);
}
