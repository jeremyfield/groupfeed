package com.turtles.groupfeed;

import com.turtles.groupfeed.Properties.MembersPropertiesReader;

import java.util.Set;

public class IdolGroup {

    private Set<String> identifiers;
    private Set<Long> channelIds;

    public IdolGroup() {
        this.identifiers = MembersPropertiesReader.getGroupIdentifiers();
        this.channelIds = MembersPropertiesReader.getGroupChannelIds();
    }

    public Set<String> getIdentifiers() {
        return identifiers;
    }

    public Set<Long> getChannelIds() {
        return channelIds;
    }
}
