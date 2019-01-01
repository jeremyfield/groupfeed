package com.turtles.twicefeed;

import twitter4j.MediaEntity;
import twitter4j.Status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatusParser {
    public static String makeMessage(Status status) {
        List<MediaEntity> mediaEntities = Arrays.asList(status.getMediaEntities());
        StringBuilder builder = new StringBuilder();
        builder.append(getMetaData(status))
                .append(findSourceDate(status).orElse(""));
        return collectURLs(mediaEntities, status, builder);
    }

    private static String collectURLs(List<MediaEntity> mediaEntities, Status status, StringBuilder builder) {
        String type = mediaEntities.get(0).getType();
        if(type.equals("photo")) {
            builder.append(compilePhotoURLS(mediaEntities));
            return builder.toString();
        }
        else if(type.equals("video")) {
            builder.append(getStatusURL(status));
            return builder.toString();
        }
        else return "";
    }

    private static String compilePhotoURLS(List<MediaEntity> mediaEntities) {
        return mediaEntities.stream()
                .map(MediaEntity::getMediaURLHttps)
                .map(url -> url + ":orig")
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private static String getMetaData(Status status) {
        String screenName = "`@" + status.getUser().getScreenName() + "`";
        String url = getStatusURL(status);
        StringBuilder builder = new StringBuilder();
        builder.append(screenName)
                .append(" | ")
                .append("<").append(url).append(">")
                .append(System.lineSeparator());
        return builder.toString();
    }

    private static String getStatusURL(Status status) {
        StringBuilder builder = new StringBuilder();
        builder.append("https://twitter.com/twicefeed/status/")
                .append(status.getId());
        return builder.toString();
    }

    private static Optional<String> findSourceDate(Status status) {
        String regex = "(\\d{6})";
        String text = status.getText();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if(matcher.find() && matcher.groupCount() == 1) {
            return Optional.of(matcher.group(0) + System.lineSeparator());
        }
        else return Optional.empty();
    }
}
