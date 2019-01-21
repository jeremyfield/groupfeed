package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.FansiteIds;
import com.turtles.groupfeed.GroupFeedBot;
import com.turtles.groupfeed.TwitterUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.User;

import java.util.List;

public class UnfollowCommand implements Command {

    private TwitterStream twitterStream;

    public UnfollowCommand(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if(!CommandUtils.isContributor(event)) {
            return;
        }

        if(!args.isEmpty()) {
            GroupFeedBot.sendMessage(event.getChannel(), "Unfollowing... please wait a moment.");
            List<Long> currentFansiteIds = FansiteIds.getFansiteIds();
            for(String screenName : args) {
                String formattedName = TwitterUtils.formatScreenName(screenName);
                try {
                    User fansite = TwitterUtils.getUserByScreenName(screenName);
                    if(currentFansiteIds.contains(fansite.getId())) {
                        FansiteIds.removeFansite(fansite.getId());
                        GroupFeedBot.sendMessage(event.getChannel(), "Unfollowing " + formattedName + "... :white_check_mark:");
                    } else {
                        GroupFeedBot.sendMessage(event.getChannel(), formattedName + " is already not followed :x:");
                    }
                } catch (TwitterException e) {
                    GroupFeedBot.sendMessage(event.getChannel(), "Cannot find " + formattedName + " on Twitter :exclamation:");
                }
            }
            TwitterUtils.restartStream(twitterStream);
            GroupFeedBot.sendMessage(event.getChannel(), "Twitter stream restarted with removed fansites.");
        } else {
            GroupFeedBot.sendMessage(event.getChannel(), "Please specify fansite(s) to unfollow.");
        }
    }
}