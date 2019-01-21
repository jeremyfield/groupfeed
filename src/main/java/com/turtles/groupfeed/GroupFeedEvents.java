package com.turtles.groupfeed;

import com.turtles.groupfeed.Properties.DiscordPropertiesReader;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;
import sx.blah.discord.handle.obj.IRole;
import twitter4j.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class GroupFeedEvents {

    TwitterStream twitterStream;
    Twitter twitter = new TwitterFactory().getInstance();
    Instant createdAt;

    public GroupFeedEvents(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
        createdAt = Instant.now();
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContent();
        if(message.startsWith("!uptime")) {
            GroupFeedBot.sendMessage(event.getChannel(), "Group Feed has been online for: " + Duration.between(createdAt, Instant.now()));
        } else if(message.startsWith("!commands")) {
            handleCommandsCommand(event);
        } else if(message.startsWith("!follow ")) {
            handleFollowCommand(event);
        } else if(message.startsWith("!unfollow ")) {
            handleUnfollowCommand(event);
        } else if(message.startsWith("!refresh")) {
            handleRefreshCommand(event);
        } else if(message.startsWith("!check")) {
            handleCheckCommand(event);
        } else if(message.startsWith("!post")) {
            handlePostCommand(event);
        }
    }

    @EventSubscriber
    public void onReady(ReadyEvent readyEvent) {
        //TODO: lower priority: what the bot does when ready
    }

    @EventSubscriber
    public void onDisconnect(DisconnectedEvent event) {
        //TODO: lower priority: what the bot does when disconnected
    }

    private boolean isContributor(MessageReceivedEvent event) {
        if(event.getAuthor().getLongID() == DiscordPropertiesReader.getBotOwnerId()) {
            return true;
        }

        List<IRole> roles = event.getAuthor().getRolesForGuild(event.getGuild());
        return roles.stream()
                .map(IRole::getName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase("contributor"));
    }

    private void handleCommandsCommand(MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("```" + System.lineSeparator())
                .append("!uptime" + System.lineSeparator())
                .append("!follow <account>" + System.lineSeparator())
                .append("!unfollow <account>" + System.lineSeparator())
                .append("!fansites" + System.lineSeparator())
                .append("!refresh" + System.lineSeparator())
                .append("!check <account>" + System.lineSeparator())
                .append("!post <tweetID> <member name>" + System.lineSeparator())
                .append("```");
        GroupFeedBot.sendMessage(event.getChannel(), builder.toString());
    }

    private void handleFollowCommand(MessageReceivedEvent event) {
        if(!isContributor(event)) {
            return;
        }

        String[] params = event.getMessage().getContent().trim().split(" ");
        if(params.length < 2) {
            GroupFeedBot.sendMessage(event.getChannel(), "Please specify the account you want to follow.");
            return;
        }

        try {
            User fansite = TwitterUtils.getUserByScreenName(params[1]);
            List<Long> currentFansiteIds = FansiteIds.getFansiteIds();
            if(currentFansiteIds.contains(fansite.getId())) {
                GroupFeedBot.sendMessage(event.getChannel(), "That account is already followed.");
            } else {
                GroupFeedBot.sendMessage(event.getChannel(), "Following... please wait a moment.");
                FansiteIds.addFansite(fansite.getId());
                restartStream();
                GroupFeedBot.sendMessage(event.getChannel(), "Account followed. Twitter stream restarted.");

            }
        } catch (TwitterException e) {
            GroupFeedBot.sendMessage(event.getChannel(), "Cannot find " + params[1] + " on Twitter.");
            System.err.println("Cannot find user " + params[1] + " to follow.");
        }
    }

    private void handleUnfollowCommand(MessageReceivedEvent event) {
        if(!isContributor(event)) {
            return;
        }

        String[] params = event.getMessage().getContent().trim().split(" ");
        if(params.length < 2) {
            GroupFeedBot.sendMessage(event.getChannel(), "Please specify the account you want to follow.");
            return;
        }

        try {
            User fansite = TwitterUtils.getUserByScreenName(params[1]);
            List<Long> currentFansiteIds = FansiteIds.getFansiteIds();
            if(!currentFansiteIds.contains(fansite.getId())) {
                GroupFeedBot.sendMessage(event.getChannel(), "Account already not followed.");
            } else {
                GroupFeedBot.sendMessage(event.getChannel(), "Unfollowing... please wait a moment.");
                FansiteIds.removeFansite(fansite.getId());
                restartStream();
                GroupFeedBot.sendMessage(event.getChannel(), "Account unfollowed. Twitter stream restarted.");
            }
        } catch (TwitterException e) {
            GroupFeedBot.sendMessage(event.getChannel(), "Cannot find " + params[1] + " on Twitter.");
            System.err.println("Cannot find user " + params[1] + " to unfollow.");
        }
    }

    private void handleCheckCommand(MessageReceivedEvent event) {
        String[] params = event.getMessage().getContent().trim().split(" ");
        if(params.length < 2) {
            GroupFeedBot.sendMessage(event.getChannel(), "Please specify the account you want to check.");
            return;
        }

        String accountToCheck = params[1];
        try {
            User fansiteToCheck = TwitterUtils.getUserByScreenName(params[1]);
            List<Long> currentFansiteIds = FansiteIds.getFansiteIds();
            if(currentFansiteIds.contains(fansiteToCheck.getId())) {
                GroupFeedBot.sendMessage(event.getChannel(), "`" + accountToCheck + "` is already followed.");
            } else {
                GroupFeedBot.sendMessage(event.getChannel(), "`" + accountToCheck + "` is not being followed.");
            }
        } catch (TwitterException e) {
            GroupFeedBot.sendMessage(event.getChannel(), "Cannot find " + params[1] + " on Twitter.");
            System.err.println("Cannot find user " + params[1] + " on Twitter.");
        }
    }

    private void handlePostCommand(MessageReceivedEvent event) {
        if(!isContributor(event)) {
            return;
        }

        String[] params = event.getMessage().getContent().trim().split(" ");
        if(params.length < 3) {
            GroupFeedBot.sendMessage(event.getChannel(), "Please specify the tweet ID and the member channel to post to.");
            return;
        }

        long tweetID = Long.parseLong(params[1]);
        try {
            FansiteStatus fansiteStatus = new FansiteStatus(twitter.showStatus(tweetID));
            if(params[2].equalsIgnoreCase("ot")) {
                GroupFeedBot.sendMessageToGroupChannel(fansiteStatus.toString());
            } else {
                GroupFeedBot.sendMessage(new IdolMember(params[2].toLowerCase()), fansiteStatus.toString());
            }
            GroupFeedBot.sendMessage(event.getChannel(), "message has been posted.");
        } catch (TwitterException e) {
            GroupFeedBot.sendMessage(event.getChannel(), "That is not a valid tweet id.");
            e.printStackTrace();
        }
    }

    private void handleRefreshCommand(MessageReceivedEvent event) {
        if(!isContributor(event)) {
            return;
        }

        restartStream();
        GroupFeedBot.sendMessage(event.getChannel(), "Twitter stream restarted.");
    }

    private void restartStream() {
        FilterQuery filterQuery = new FilterQuery();
        long[] userIds = FansiteIds.getFansiteIdsAsLongArray();
        filterQuery.follow(userIds);
        twitterStream.cleanUp();
        twitterStream.filter(filterQuery);
    }
}
