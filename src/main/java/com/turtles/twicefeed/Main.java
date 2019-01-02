package com.turtles.twicefeed;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.io.IOException;

public class Main {
    public static void main( String[] args ) {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        GroupFeedListener groupFeedListener = new GroupFeedListener();

        FilterQuery filterQuery = new FilterQuery();
        long[] userIds;
        try {
            userIds = Accounts.getUserIds();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        filterQuery.follow(userIds);
        twitterStream.addListener(groupFeedListener);

        String discordToken = PropertiesReader.getDiscordToken();
        GroupFeedBot groupFeedBot = new GroupFeedBot(discordToken, twitterStream);
        twitterStream.filter(filterQuery);
    }
}
