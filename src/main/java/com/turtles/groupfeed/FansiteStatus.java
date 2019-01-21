package com.turtles.groupfeed;

import com.turtles.groupfeed.Constants.TwitterConstants;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        return isRetweet() || isReply() || !containsMedia() || containsMediaType(TwitterConstants.ANIMATED_GIF);
    }

    public List<IdolMember> getMembersMentioned(Set<IdolMember> idolMembers) {
        return idolMembers.stream()
                .filter(idolMember -> containsAnyHashtag(idolMember.getIdentifiers()))
                .collect(Collectors.toList());
    }

    public String getUrl() {
        return TwitterConstants.TWITTER_DOMAIN + status.getUser().getScreenName() + TwitterConstants.STATUS_PATH + status.getId();
    }

    public Optional<String> getSourceDate() {
        Pattern pattern = Pattern.compile(TwitterConstants.SOURCE_DATE_PATTERN);
        Matcher matcher = pattern.matcher(status.getText());
        return (matcher.find() && matcher.groupCount() == 1) ?
                Optional.of(matcher.group(0) + System.lineSeparator()) : Optional.empty();
    }

    public String getMediaUrls() {
        if(containsMediaType(TwitterConstants.PHOTO)) {
            return Arrays.stream(status.getMediaEntities())
                    .map(MediaEntity::getMediaURLHttps)
                    .collect(Collectors.joining(System.lineSeparator()));
        } else if(containsMediaType(TwitterConstants.VIDEO)) {
            return getUrl();
        } else {
            return TwitterConstants.NO_MEDIA;
        }
    }

    public String toString() {
        String str = getUrl() + System.lineSeparator();
        str += TwitterUtils.formatScreenName(status.getUser().getScreenName()) + System.lineSeparator();
        if(getSourceDate().isPresent()) {
            str += getSourceDate() + System.lineSeparator();
        }
        str += getMediaUrls();
        return str;
    }
}
