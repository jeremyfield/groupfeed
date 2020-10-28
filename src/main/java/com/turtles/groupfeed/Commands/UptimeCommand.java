package com.turtles.groupfeed.Commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class UptimeCommand implements Command {

    private Instant createdAt;

    public UptimeCommand() {
        createdAt = Instant.now();
    }

    @Override
    public void runCommand(MessageCreateEvent event, List<String> args) {
        TextChannel textChannel = event.getChannel();
        textChannel.sendMessage("Group Feed has been online for: " + Duration.between(createdAt, Instant.now()));
    }
}
