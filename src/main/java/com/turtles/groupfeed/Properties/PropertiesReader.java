package com.turtles.groupfeed.Properties;

import com.turtles.groupfeed.Constants.PropertiesConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class PropertiesReader {

    public static Set<Long> getLongSetFromProp(String propFile, String propName) {
        return new HashSet<>(getLongListFromProp(propFile, propName));
    }

    public static List<Long> getLongListFromProp(String propFile, String propName) {
        String value = getStringFromProp(propFile, propName);
        return Arrays.stream(value.split(PropertiesConstants.LIST_DELIMITER))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public static long getLongFromProp(String propFile, String propName) {
        String value = getStringFromProp(propFile, propName);
        return Long.valueOf(value);
    }

    public static Set<String> getStringSetFromProp(String propFile, String propName) {
        return new HashSet<>(getStringListFromProp(propFile, propName));
    }

    public static List<String> getStringListFromProp(String propFile, String propName) {
        String value = getStringFromProp(propFile, propName);
        return Arrays.asList(value.split(PropertiesConstants.LIST_DELIMITER));
    }

    public static String getStringFromProp(String propFile, String propName) {
        Properties properties = new Properties();
        String value = null;
        try(InputStream inputStream = new FileInputStream(propFile + PropertiesConstants.PROPERTIES)) {
            properties.load(inputStream);
            value = properties.getProperty(propName);
        } catch (IOException e) {
            System.err.println("Could not read from " + propFile + PropertiesConstants.PROPERTIES);
            e.printStackTrace();
        }
        return value;
    }
}
