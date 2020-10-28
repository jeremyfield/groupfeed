package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.*;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import twitter4j.TwitterException;

import java.util.List;

public class PostCommand implements Command {

    @Override
    public void runCommand(MessageCreateEvent event, List<String> args) {
        if(!CommandUtils.isContributor(event)) {
            return;
        }

        TextChannel textChannel = event.getMessage().getChannel();

        if(args.size() < 2) {
            textChannel.sendMessage("Please specify the tweet ID(s) first and the member name last.");
            return;
        }

        for(int i = 0; i < args.size() - 1; i++) {
            try {
                FansiteStatus fansiteStatus = new FansiteStatus(TwitterUtils.getStatusById(Long.parseLong(args.get(i))));
                if(args.get(args.size() - 1).equalsIgnoreCase("ot")) {
                    GroupFeedBot.sendMessageToGroupChannel(fansiteStatus.toString());
                } else {
                    GroupFeedBot.sendMessage(new IdolMember(args.get(args.size() - 1).toLowerCase()), fansiteStatus.toString());
                }
                textChannel.sendMessage(args.get(i) + " has been posted :white_check_mark:");
            } catch (TwitterException e) {
                textChannel.sendMessage(args.get(i) + " is not a valid tweet ID :exclamation:");
            }
        }
    }
}
