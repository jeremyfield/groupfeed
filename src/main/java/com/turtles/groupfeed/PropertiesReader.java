package com.turtles.groupfeed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class PropertiesReader {

    public static String getDiscordToken() {
        return getStringFromProp("discord", "token");
    }

    public static List<String> getMemberNames() {
        return getStringListFromProp("members", "memberNames");
    }

    public static Set<String> getGroupIdentifiers() {
        return getStringSetFromProp("members", "ot.identifiers");
    }

    public static long getBotOwnerId() {
        return getLongFromProp("discord", "ownerId");
    }

    public static Set<Long> getGroupChannelIds() {
        return getLongSetFromProp("members", "ot.channels");
    }

    public static Set<Long> getMemberChannelIds(String  memberName) {
        return getLongSetFromProp("members", memberName + ".channels");
    }

    public static Set<String> getMemberIdentifiers(String memberName) {
        return getStringSetFromProp("members", memberName + ".identifiers");
    }

    private static Set<Long> getLongSetFromProp(String propFile, String propName) {
        return new HashSet<>(getLongListFromProp(propFile, propName));
    }

    private static List<Long> getLongListFromProp(String propFile, String propName) {
        String value = getStringFromProp(propFile, propName);
        return Arrays.stream(value.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    private static long getLongFromProp(String propFile, String propName) {
        String value = getStringFromProp(propFile, propName);
        return Long.valueOf(value);
    }

    private static Set<String> getStringSetFromProp(String propFile, String propName) {
        return new HashSet<>(getStringListFromProp(propFile, propName));
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
