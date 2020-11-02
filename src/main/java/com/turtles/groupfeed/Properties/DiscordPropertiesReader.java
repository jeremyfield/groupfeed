package com.turtles.groupfeed.Properties;

import com.turtles.groupfeed.Constants.PropertiesConstants;

public class DiscordPropertiesReader {

    public static String getDiscordToken() {
        return PropertiesReader.getStringFromProp(PropertiesConstants.DISCORD, PropertiesConstants.TOKEN);
    }

    public static long getBotOwnerId() {
        return PropertiesReader.getLongFromProp(PropertiesConstants.DISCORD, PropertiesConstants.OWNER_ID);
    }

    public static String getCommandPrefix() {
        return PropertiesReader.getStringFromProp(PropertiesConstants.DISCORD, PropertiesConstants.PREFIX);
    }
}
