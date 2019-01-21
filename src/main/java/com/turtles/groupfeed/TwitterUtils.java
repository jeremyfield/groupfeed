package com.turtles.groupfeed;

import twitter4j.*;

public class TwitterUtils {

    private static Twitter twitter = TwitterFactory.getSingleton();

    public static User getUserById(long id) throws TwitterException {
        return twitter.showUser(id);
    }

    public static User getUserByScreenName(String screenName) throws TwitterException {
        return twitter.showUser(screenName);
    }

    public static Status getStatusById(long id) throws TwitterException {
        return twitter.showStatus(id);
    }

    public static void restartStream(TwitterStream twitterStream) {
        twitterStream.cleanUp();
        twitterStream.filter(new FilterQuery().follow(FansiteIds.getFansiteIdsAsLongArray()));
    }
}
