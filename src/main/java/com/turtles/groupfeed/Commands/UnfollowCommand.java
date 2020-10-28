package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.FansiteIds;
import com.turtles.groupfeed.TwitterUtils;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
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
    public void runCommand(MessageCreateEvent event, List<String> args) {
        if(!CommandUtils.isContributor(event)) {
            return;
        }

        TextChannel textChannel = event.getMessage().getChannel();

        if(!args.isEmpty()) {
            textChannel.sendMessage("Unfollowing... please wait a moment.");
            List<Long> currentFansiteIds = FansiteIds.getFansiteIds();
            for(String screenName : args) {
                String formattedName = TwitterUtils.formatScreenName(screenName);
                try {
                    User fansite = TwitterUtils.getUserByScreenName(screenName);
                    if(currentFansiteIds.contains(fansite.getId())) {
                        FansiteIds.removeFansite(fansite.getId());
                        textChannel.sendMessage("Unfollowing " + formattedName + "... :white_check_mark:");
                    } else {
                        textChannel.sendMessage(formattedName + " is already not followed :x:");
                    }
                } catch (TwitterException e) {
                    textChannel.sendMessage("Cannot find " + formattedName + " on Twitter :exclamation:");
                }
            }
            TwitterUtils.restartStream(twitterStream);
            textChannel.sendMessage("Twitter stream restarted with removed fansites.");
        } else {
            textChannel.sendMessage("Please specify fansite(s) to unfollow.");
        }
    }
}
