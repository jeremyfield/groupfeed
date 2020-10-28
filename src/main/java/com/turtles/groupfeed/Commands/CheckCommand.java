package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.FansiteIds;
import com.turtles.groupfeed.TwitterUtils;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.List;

public class CheckCommand implements Command {

    @Override
    public void runCommand(MessageCreateEvent event, List<String> args) {
        TextChannel textChannel = event.getChannel();

        if(args.isEmpty()) {
            textChannel.sendMessage("Please specify account(s) you want to check.");
            return;
        }

        List<Long> currentFansiteIds = FansiteIds.getFansiteIds();
        for(String screenName : args) {
            String formattedName = TwitterUtils.formatScreenName(screenName);
            try {
                User fansiteToCheck = TwitterUtils.getUserByScreenName(screenName);
                if(currentFansiteIds.contains(fansiteToCheck.getId())) {
                    textChannel.sendMessage(formattedName + " is already followed :white_check_mark:");
                } else {
                    textChannel.sendMessage(formattedName + " is not followed :x:");
                }
            } catch (TwitterException e) {
                textChannel.sendMessage("Cannot find " + formattedName + " on Twitter :exclamation:");
            }
        }
    }
}
