package com.turtles.groupfeed;

import java.util.Set;

public class IdolGroup {

    private Set<String> identifiers;
    private Set<Long> channelIds;

    public IdolGroup() {
        this.identifiers = PropertiesReader.getGroupIdentifiers();
        this.channelIds = PropertiesReader.getGroupChannelIds();
    }

    public Set<String> getIdentifiers() {
        return identifiers;
    }

    public Set<Long> getChannelIds() {
        return channelIds;
    }
}
