package com.turtles.groupfeed;

import twitter4j.*;

import java.util.*;

public class GroupFeedListener implements StatusListener {

    private IdolGroup idolGroup;
    private Set<IdolMember> idolMembers;

    public GroupFeedListener() {
        idolGroup = new IdolGroup();
        idolMembers = new HashSet<>();
        PropertiesReader.getMemberNames().forEach(name -> idolMembers.add(new IdolMember(name)));
    }

    @Override
    public void onStatus(Status status) {
        FansiteStatus fansiteStatus = new FansiteStatus(status);
        if(fansiteStatus.isIgnorableStatus()) {
            return;
        }

        List<IdolMember> membersMentioned = fansiteStatus.getMembersMentioned(idolMembers);
        if(membersMentioned.size() > 1) {
            GroupFeedBot.sendMessageToGroupChannel(fansiteStatus.toString());
        } else if(membersMentioned.size() == 1) {
            GroupFeedBot.sendMessage(membersMentioned.get(0), fansiteStatus.toString());
        } else if(fansiteStatus.containsAnyHashtag(idolGroup.getIdentifiers())) {
            GroupFeedBot.sendMessageToGroupChannel(fansiteStatus.toString());
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        //TODO: lower priority
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        //TODO: lower priority
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        //TODO: lower priority
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
        //TODO: lower priority
    }

    @Override
    public void onException(Exception e) {
        //TODO: lower priority
    }
}
