package com.turtles.twicefeed;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;
import sx.blah.discord.handle.obj.IRole;
import twitter4j.FilterQuery;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TwiceFeedEvents {

    TwitterStream twitterStream;
    Twitter twitter = new TwitterFactory().getInstance();
    Instant createdAt;

    public TwiceFeedEvents(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
        createdAt = Instant.now();
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContent();
        if(message.startsWith("!uptime")) {
            TwiceFeedBot.sendMessage(event.getChannel(), "Twice Feed has been online for: " + Duration.between(createdAt, Instant.now()));
        } else if(message.startsWith("!commands")) {
            handleCommandsCommand(event);
        } else if(message.startsWith("!fansites")) {
            handleFansitesCommand(event);
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
        if(event.getAuthor().getLongID() == 184538928986980352L) {
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
        TwiceFeedBot.sendMessage(event.getChannel(), builder.toString());
    }

    private void handleFansitesCommand(MessageReceivedEvent event) {
        int partitionSize = 50;
        List<List<String>> partitions = new ArrayList<>();

        try {
            List<String> accounts = Accounts.getAccountsFollowed();
            TwiceFeedBot.sendMessage(event.getChannel(), accounts.size() + " accounts are followed: ");
            for(int i = 0; i < accounts.size(); i += partitionSize) {
                partitions.add(accounts.subList(i, Math.min(i + partitionSize, accounts.size())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(List<String> accounts : partitions) {
            StringBuilder builder = new StringBuilder();
            builder.append("```" + System.lineSeparator());
            builder.append(accounts.stream().collect(Collectors.joining(", ")));
            builder.append(System.lineSeparator() + "```");
            TwiceFeedBot.sendMessage(event.getChannel(), builder.toString());
        }
    }

    private void handleFollowCommand(MessageReceivedEvent event) {
        if(!isContributor(event)) {
            return;
        }

        String[] params = event.getMessage().getContent().trim().split(" ");
        if(params.length < 2) {
            TwiceFeedBot.sendMessage(event.getChannel(), "Please specify the account you want to follow.");
            return;
        }

        String accountToFollow = params[1];
        List<String> accounts;
        try {
            accounts = Accounts.getAccountsFollowed();
            if(accounts.contains(accountToFollow))
                TwiceFeedBot.sendMessage(event.getChannel(), "That account is already followed.");
            else {
                TwiceFeedBot.sendMessage(event.getChannel(), "Following... please wait a moment.");
                accounts.add(accountToFollow);
                Accounts.setAccountsFollowed(accounts);
                restartStream();
                TwiceFeedBot.sendMessage(event.getChannel(), "Account followed. Twitter stream restarted.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUnfollowCommand(MessageReceivedEvent event) {
        if(!isContributor(event)) {
            return;
        }

        String[] params = event.getMessage().getContent().trim().split(" ");
        if(params.length < 2) {
            TwiceFeedBot.sendMessage(event.getChannel(), "Please specify the account you want to follow.");
            return;
        }

        String accountToUnfollow = params[1];
        List<String> accounts;
        try {
            accounts = Accounts.getAccountsFollowed();
            if(accounts.remove(accountToUnfollow)) {
                TwiceFeedBot.sendMessage(event.getChannel(), "Unfollowing... please wait a moment.");
                Accounts.setAccountsFollowed(accounts);
                restartStream();
                TwiceFeedBot.sendMessage(event.getChannel(), "Account unfollowed. Twitter stream restarted.");
            }
            else
                TwiceFeedBot.sendMessage(event.getChannel(), "Account already not followed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCheckCommand(MessageReceivedEvent event) {
        String[] params = event.getMessage().getContent().trim().split(" ");
        if(params.length < 2) {
            TwiceFeedBot.sendMessage(event.getChannel(), "Please specify the account you want to check.");
            return;
        }

        String accountToCheck = params[1];
        try {
            List<String> accounts = Accounts.getAccountsFollowed();
            if(accounts.contains(accountToCheck)) {
                TwiceFeedBot.sendMessage(event.getChannel(), "`" + accountToCheck + "` is already followed.");
            }
            else {
                TwiceFeedBot.sendMessage(event.getChannel(), "`" + accountToCheck + "` is not being followed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePostCommand(MessageReceivedEvent event) {
        if(!isContributor(event)) {
            return;
        }

        String[] params = event.getMessage().getContent().trim().split(" ");
        if(params.length < 3) {
            TwiceFeedBot.sendMessage(event.getChannel(), "Please specify the tweet ID and the member channel to post to.");
            return;
        }

        long tweetID = Long.parseLong(params[1]);
        String member = params[2];
        try {
            String message = StatusParser.makeMessage(twitter.showStatus(tweetID));
            TwiceFeedBot.sendMessage(member, message);
            TwiceFeedBot.sendMessage(event.getChannel(), "message has been posted.");
        } catch (TwitterException e) {
            TwiceFeedBot.sendMessage(event.getChannel(), "That is not a valid tweet id.");
            e.printStackTrace();
        }
    }

    private void handleRefreshCommand(MessageReceivedEvent event) {
        if(!isContributor(event)) {
            return;
        }

        restartStream();
        TwiceFeedBot.sendMessage(event.getChannel(), "Twitter stream restarted.");
    }

    private void restartStream() {
        FilterQuery filterQuery = new FilterQuery();
        long[] userIds;
        try {
            userIds = Accounts.getUserIds();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        filterQuery.follow(userIds);
        twitterStream.cleanUp();
        twitterStream.filter(filterQuery);
    }
}
