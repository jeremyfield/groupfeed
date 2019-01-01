package com.turtles.twicefeed;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.io.IOException;

public class Main {
    public static void main( String[] args ) {
        if(args.length < 1) {
            System.err.println("Please enter the discord bot token as the first argument.");
            return;
        }

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        TwiceFeedListener twiceFeedListener = new TwiceFeedListener();

        FilterQuery filterQuery = new FilterQuery();
        long[] userIds;
        try {
            userIds = Accounts.getUserIds();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        filterQuery.follow(userIds);
        twitterStream.addListener(twiceFeedListener);

        TwiceFeedBot twiceFeedBot = new TwiceFeedBot(args[0], twitterStream);
        twitterStream.filter(filterQuery);
    }
}
