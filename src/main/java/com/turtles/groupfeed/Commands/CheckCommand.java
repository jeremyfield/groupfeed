package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.FansiteIds;
import com.turtles.groupfeed.GroupFeedBot;
import com.turtles.groupfeed.TwitterUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.List;

public class CheckCommand implements Command {

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if(args.isEmpty()) {
            GroupFeedBot.sendMessage(event.getChannel(), "Please specify account(s) you want to check.");
            return;
        }

        List<Long> currentFansiteIds = FansiteIds.getFansiteIds();
        for(String screenName : args) {
            try {
                User fansiteToCheck = TwitterUtils.getUserByScreenName(screenName);
                if(currentFansiteIds.contains(fansiteToCheck.getId())) {
                    GroupFeedBot.sendMessage(event.getChannel(), "`" + screenName + "` is already followed :white_check_mark:");
                } else {
                    GroupFeedBot.sendMessage(event.getChannel(), "`" + screenName + "` is not followed :x:");
                }
            } catch (TwitterException e) {
                GroupFeedBot.sendMessage(event.getChannel(), "Cannot find " + screenName + " on Twitter :exclamation:");
            }
        }
    }
}
