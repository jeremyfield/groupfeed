package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.FansiteIds;
import com.turtles.groupfeed.GroupFeedBot;
import com.turtles.groupfeed.TwitterUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.User;

import java.util.List;

public class FollowCommand implements Command {

    private TwitterStream twitterStream;

    public FollowCommand(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if(!CommandUtils.isContributor(event)) {
            return;
        }

        if(!args.isEmpty()) {
            List<Long> currentFansiteIds = FansiteIds.getFansiteIds();
            for(String screenName : args) {
                try {
                    User fansite = TwitterUtils.getUserByScreenName(screenName);
                    if(currentFansiteIds.contains(fansite.getId())) {
                        GroupFeedBot.sendMessage(event.getChannel(), screenName + " is already followed.");
                    } else {
                        GroupFeedBot.sendMessage(event.getChannel(), "Following... please wait a moment.");
                        FansiteIds.addFansite(fansite.getId());
                        TwitterUtils.restartStream(twitterStream);
                        GroupFeedBot.sendMessage(event.getChannel(), "Account followed. Twitter stream restarted.");
                    }
                } catch (TwitterException e) {
                    GroupFeedBot.sendMessage(event.getChannel(), "Cannot find " + screenName + " on Twitter.");
                }
            }
        } else {
            GroupFeedBot.sendMessage(event.getChannel(), "Please specify fansite(s) to follow.");
        }
    }
}
