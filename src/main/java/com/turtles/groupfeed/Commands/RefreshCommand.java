package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.GroupFeedBot;
import com.turtles.groupfeed.TwitterUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import twitter4j.TwitterStream;

import java.util.List;

public class RefreshCommand implements Command {

    private TwitterStream twitterStream;

    public RefreshCommand(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if(CommandUtils.isContributor(event)) {
            TwitterUtils.restartStream(twitterStream);
            GroupFeedBot.sendMessage(event.getChannel(), "Twitter stream restarted.");
        }
    }
}
