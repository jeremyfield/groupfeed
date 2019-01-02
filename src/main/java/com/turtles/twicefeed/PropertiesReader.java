package com.turtles.twicefeed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    public static String getDiscordToken() throws IOException {
        Properties discordProps = new Properties();
        InputStream inputStream = new FileInputStream("discord.properties");
        discordProps.load(inputStream);
        String discordToken = discordProps.getProperty("token");
        if(inputStream != null) {
            inputStream.close();
        }
        return discordToken;
    }
}
