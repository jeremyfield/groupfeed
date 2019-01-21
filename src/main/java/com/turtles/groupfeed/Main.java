package com.turtles.groupfeed;

import com.turtles.groupfeed.Properties.DiscordPropertiesReader;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class Main {
    public static void main( String[] args ) {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        GroupFeedListener groupFeedListener = new GroupFeedListener();

        FilterQuery filterQuery = new FilterQuery();
        filterQuery.follow(FansiteIdUtils.getFansiteIdsAsLongArray());
        twitterStream.addListener(groupFeedListener);

        String discordToken = DiscordPropertiesReader.getDiscordToken();
        GroupFeedBot groupFeedBot = new GroupFeedBot(discordToken, twitterStream);
        twitterStream.filter(filterQuery);
    }
}
