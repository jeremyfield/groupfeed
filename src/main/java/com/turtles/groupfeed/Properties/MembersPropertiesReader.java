package com.turtles.groupfeed.Properties;

import java.util.List;
import java.util.Set;

public class MembersPropertiesReader {

    public static final String PROP_FILE = "members";

    public static List<String> getMemberNames() {
        return PropertiesReader.getStringListFromProp(PROP_FILE, "memberNames");
    }

    public static Set<Long> getMemberChannelIds(String  memberName) {
        return PropertiesReader.getLongSetFromProp(PROP_FILE, memberName + ".channels");
    }

    public static Set<String> getMemberIdentifiers(String memberName) {
        return PropertiesReader.getStringSetFromProp(PROP_FILE, memberName + ".identifiers");
    }

    public static Set<String> getGroupIdentifiers() {
        return PropertiesReader.getStringSetFromProp(PROP_FILE, "ot.identifiers");
    }

    public static Set<Long> getGroupChannelIds() {
        return PropertiesReader.getLongSetFromProp(PROP_FILE, "ot.channels");
    }
}
