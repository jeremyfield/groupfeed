package com.turtles.groupfeed.Properties;

public class DiscordPropertiesReader {

    public static String getDiscordToken() {
        return PropertiesReader.getStringFromProp("discord", "token");
    }

    public static long getBotOwnerId() {
        return PropertiesReader.getLongFromProp("discord", "ownerId");
    }
}
