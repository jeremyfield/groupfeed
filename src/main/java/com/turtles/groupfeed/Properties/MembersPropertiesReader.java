package com.turtles.groupfeed.Properties;

import com.turtles.groupfeed.Constants.PropertiesConstants;

import java.util.List;
import java.util.Set;

public class MembersPropertiesReader {

    public static List<String> getMemberNames() {
        return PropertiesReader.getStringListFromProp(PropertiesConstants.MEMBERS, PropertiesConstants.MEMBER_NAMES);
    }

    public static Set<Long> getMemberChannelIds(String  memberName) {
        return PropertiesReader.getLongSetFromProp(PropertiesConstants.MEMBERS, memberName + PropertiesConstants.DOT_CHANNELS);
    }

    public static Set<String> getMemberIdentifiers(String memberName) {
        return PropertiesReader.getStringSetFromProp(PropertiesConstants.MEMBERS, memberName + PropertiesConstants.DOT_IDENTIFIERS);
    }

    public static Set<String> getGroupIdentifiers() {
        return PropertiesReader.getStringSetFromProp(PropertiesConstants.MEMBERS, PropertiesConstants.OT_IDENTIFIERS);
    }

    public static Set<Long> getGroupChannelIds() {
        return PropertiesReader.getLongSetFromProp(PropertiesConstants.MEMBERS, PropertiesConstants.OT_CHANNELS);
    }
}
