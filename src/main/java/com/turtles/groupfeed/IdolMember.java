package com.turtles.groupfeed;

import com.turtles.groupfeed.Properties.MembersPropertiesReader;

import java.util.Set;

public class IdolMember {

    private String name;
    private Set<String> identifiers;
    private Set<Long> channelIds;

    public IdolMember(String name) {
        this.name = name;
        this.identifiers = MembersPropertiesReader.getMemberIdentifiers(name);
        this.channelIds = MembersPropertiesReader.getMemberChannelIds(name);
    }

    public String getName() {
        return name;
    }

    public Set<String> getIdentifiers() {
        return identifiers;
    }

    public Set<Long> getChannelIds() {
        return channelIds;
    }
}
