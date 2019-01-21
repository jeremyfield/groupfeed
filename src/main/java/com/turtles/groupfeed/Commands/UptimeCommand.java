package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.GroupFeedBot;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class UptimeCommand implements Command {

    private Instant createdAt;

    public UptimeCommand() {
        createdAt = Instant.now();
    }

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        GroupFeedBot.sendMessage(event.getChannel(), "Group Feed has been online for: " + Duration.between(createdAt, Instant.now()));
    }
}
