package com.turtles.twicefeed;

import twitter4j.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwiceFeedListener implements StatusListener {

    private Map<String, String> translation;
    private List<String> memberNames;

    public TwiceFeedListener() {
        translation = new HashMap<>();
        translation.put("나연", "nayeon");
        translation.put("정연", "jeongyeon");
        translation.put("모모", "momo");
        translation.put("사나", "sana");
        translation.put("지효", "jihyo");
        translation.put("미나", "mina");
        translation.put("다현", "dahyun");
        translation.put("채영", "chaeyoung");
        translation.put("쯔위", "tzuyu");

        memberNames = Arrays.asList(new String[]{"nayeon", "jeongyeon", "momo", "sana", "jihyo", "mina", "dahyun", "chaeyoung", "tzuyu"});
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
        String[] membersMentioned = parseForMembers(status.getHashtagEntities());
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

    private void sendMessage(String[] membersMentioned, Status status, String message) {
        if(membersMentioned.length == 0) {
            if (hasTwiceHashtag(status.getHashtagEntities()))
                TwiceFeedBot.sendMessage("ot9", message);
        }
        else if(membersMentioned.length > 1)
            TwiceFeedBot.sendMessage("ot9", message);
        else
            TwiceFeedBot.sendMessage(membersMentioned[0], message);
    }

    private boolean hasTwiceHashtag(HashtagEntity[] hashtagEntities) {
        return Arrays.stream(hashtagEntities)
                .map(HashtagEntity::getText)
                .anyMatch(s -> s.equalsIgnoreCase("twice") || s.equals("트와이스"));
    }

    private String[] parseForMembers(HashtagEntity[] hashtagEntities) {
        return Arrays.stream(hashtagEntities)
                .map(HashtagEntity::getText)
                .map(String::toLowerCase)
                .map(text -> translation.get(text) != null ? translation.get(text) : text)
                .filter(text -> memberNames.contains(text))
                .distinct()
                .toArray(String[]::new);
    }
}
