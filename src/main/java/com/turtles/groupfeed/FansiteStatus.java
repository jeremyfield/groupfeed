package com.turtles.groupfeed;

import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FansiteStatus {

    private Status status;

    public FansiteStatus(Status status) {
        this.status = status;
    }

    public boolean isRetweet() {
        return status.isRetweet();
    }

    public boolean isReply() {
        return status.getInReplyToStatusId() != -1;
    }

    public boolean containsMedia() {
        return status.getMediaEntities().length > 0;
    }

    public boolean containsMediaType(String type) {
        return containsMedia() && status.getMediaEntities()[0].getType().equals(type);
    }

    public boolean containsHashtag(String hashtag) {
        return Arrays.stream(status.getHashtagEntities())
                .map(HashtagEntity::getText)
                .anyMatch(hashtag::equalsIgnoreCase);
    }

    public boolean containsAnyHashtag(Set<String> hashtags) {
        return hashtags.stream().anyMatch(hashtag -> containsHashtag(hashtag));
    }

    public boolean isIgnorableStatus() {
        return isRetweet() || isReply() || !containsMedia() || containsMediaType("animated_gif");
    }

    public List<IdolMember> getMembersMentioned(Set<IdolMember> idolMembers) {
        return idolMembers.stream()
                .filter(idolMember -> containsAnyHashtag(idolMember.getIdentifiers()))
                .collect(Collectors.toList());
    }
}
