package com.turtles.groupfeed;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TwitterUtils {

    private static Twitter twitter = TwitterFactory.getSingleton();

    public static User getUserByScreenName(String screenName) throws TwitterException {
        return twitter.showUser(screenName);
    }
}
