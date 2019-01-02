package com.turtles.groupfeed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PropertiesReader {

    public static String getDiscordToken() {
        return getStringFromProp("discord", "token");
    }

    public static List<String> getMemberNames() {
        return getStringListFromProp("members", "memberNames");
    }

    public static List<String> getGroupIdentifiers() {
        return getStringListFromProp("members", "ot.identifiers");
    }

    public static long getGroupChannelID() {
        return getLongFromProp("members", "ot.channels");
    }

    public static long getMemberChannelID(String  memberName) {
        return getLongFromProp("members", memberName + ".channels");
    }

    public static List<String> getMemberIdentifiers(String memberName) {
        return getStringListFromProp("members", memberName + ".identifiers");
    }

    private static long getLongFromProp(String propFile, String propName) {
        String value = getStringFromProp(propFile, propName);
        return Long.valueOf(value);
    }

    private static List<String> getStringListFromProp(String propFile, String propName) {
        String value = getStringFromProp(propFile, propName);
        return Arrays.asList(value.split(","));
    }

    private static String getStringFromProp(String propFile, String propName) {
        Properties properties = new Properties();
        String value = null;
        try(InputStream inputStream = new FileInputStream(propFile + ".properties")) {
            properties.load(inputStream);
            value = properties.getProperty(propName);
        } catch (IOException e) {
            System.err.println("getStringFromProp: could not read " + propFile + ".properties file.");
            e.printStackTrace();
        }
        return value;
    }
}
