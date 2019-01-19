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
        if(status.isRetweet() || status.getInReplyToStatusId() != -1)
            return;

        if(status.getMediaEntities().length == 0)
            return;
        else if(status.getMediaEntities()[0].getType().equals("animated_gif"))
            return;

        String message = StatusParser.makeMessage(status);
        List<String> membersMentioned = parseForMembers(status.getHashtagEntities());
        sendMessage(membersMentioned, status, message);
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

    private void sendMessage(List<String> membersMentioned, Status status, String message) {
        if(membersMentioned.size() == 0) {
            if (hasGroupHashtag(status.getHashtagEntities())) {
                GroupFeedBot.sendMessageToGroupChannel(message);
            }
        } else if(membersMentioned.size() > 1) {
            GroupFeedBot.sendMessageToGroupChannel(message);
        } else {
            GroupFeedBot.sendMessage(membersMentioned.get(0), message);
        }
    }

    private boolean hasGroupHashtag(HashtagEntity[] hashtagEntities) {
        return idolGroup.getIdentifiers().stream()
                .anyMatch(identifier -> containsHashtag(hashtagEntities, identifier));
    }

    private List<String> parseForMembers(HashtagEntity[] hashtagEntities) {
        List<String> membersMentioned = new ArrayList<>();
        for(IdolMember member: idolMembers) {
            Set<String> identifiers = member.getIdentifiers();
            if(containsAnyHashtag(hashtagEntities, identifiers)) {
                membersMentioned.add(member.getName());
            }
        }
        return membersMentioned;
    }

    public static boolean containsAnyHashtag(HashtagEntity[] hashtagEntities, Set<String> stringList) {
        return stringList.stream()
                .anyMatch(string -> containsHashtag(hashtagEntities, string));
    }

    public static boolean containsHashtag(HashtagEntity[] hashtagEntities, String string) {
        return Arrays.stream(hashtagEntities)
                .map(HashtagEntity::getText)
                .anyMatch(string::equalsIgnoreCase);

    }
}
