package com.turtles.twicefeed;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Accounts {

    private static final JsonReaderFactory READER_FACTORY = Json.createReaderFactory(null);
    private static final JsonWriterFactory WRITER_FACTORY = Json.createWriterFactory(null);

    public static List<String> getAccountsFollowed() throws IOException {
        try(InputStream inputStream = new FileInputStream("src/main/resources/fansites.json");
            JsonReader reader = READER_FACTORY.createReader(inputStream)) {
            JsonObject rootObject = reader.readObject();
            JsonArray screenNames = rootObject.getJsonArray("fansites");
            return screenNames.stream()
                    .map(JsonValue::toString)
                    .map(s -> s.substring(1, s.length()-1))
                    .sorted(String::compareToIgnoreCase)
                    .collect(Collectors.toList());
        }
    }

    public static void setAccountsFollowed(List<String> accounts) throws IOException {
        try(OutputStream outputStream = new FileOutputStream("src/main/resources/fansites.json");
            JsonWriter writer = WRITER_FACTORY.createWriter(outputStream)) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            accounts.stream().forEach(account -> arrayBuilder.add(account));
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("fansites", arrayBuilder.build());
            writer.writeObject(objectBuilder.build());
        }
    }

    public static long[] getUserIds() throws IOException {
        Twitter twitter = TwitterFactory.getSingleton();
        List<String> screenNames = getAccountsFollowed();

        List<Long> userIds = new ArrayList<>();
        for(String screenName : screenNames) {
            try {
                userIds.add(twitter.showUser(screenName).getId());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        return userIds.stream().mapToLong(l -> l).toArray();
    }
}
