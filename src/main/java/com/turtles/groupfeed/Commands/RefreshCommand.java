package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.TwitterUtils;
import org.javacord.api.event.message.MessageCreateEvent;
import twitter4j.TwitterStream;

import java.util.List;

public class RefreshCommand implements Command {

    private TwitterStream twitterStream;

    public RefreshCommand(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    public void runCommand(MessageCreateEvent event, List<String> args) {
        if(CommandUtils.isContributor(event)) {
            TwitterUtils.restartStream(twitterStream);
            event.getMessage().getChannel().sendMessage("Twitter stream restarted.");
        }
    }
}
