package com.turtles.groupfeed;

import twitter4j.TwitterException;
import twitter4j.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FansiteIds {

    public static long[] getFansiteIdsAsLongArray() {
        return getFansiteIds().stream().mapToLong(l -> l).toArray();
    }

    public static boolean isFansiteStored(User fansite) {
        return isFansiteStored(fansite.getId());
    }

    public static boolean isFansiteStored(long id) {
        try {
            User fansiteToCheck = TwitterUtils.getUserById(id);
            return getFansiteIds().contains(fansiteToCheck.getId());
        } catch (TwitterException e) {
            return false;
        }
    }

    public static List<Long> getFansiteIds() {
        List<Long> fansiteIds = new ArrayList<>();
        try(Stream<String> stringStream = Files.lines(Paths.get("./fansiteIds.txt"))) {
            fansiteIds = stringStream.map(Long::valueOf).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error happened while reading from fansitesIds.txt");
        }
        return fansiteIds;
    }

    public static void setFansiteIds(List<Long> fansiteIds) {
        File fout = new File("fansiteIds.txt");
        try(FileOutputStream fileOutputStream = new FileOutputStream(fout, false);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream))) {
            String newFileText = fansiteIds.stream()
                    .map(fansiteId -> fansiteId.toString())
                    .collect(Collectors.joining(System.lineSeparator()));
            bufferedWriter.write(newFileText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addFansite(long id) {
        List<Long> currentFansiteIds = getFansiteIds();
        currentFansiteIds.add(id);
        setFansiteIds(currentFansiteIds);
    }

    public static void removeFansite(long id) {
        List<Long> currentFansiteIds = getFansiteIds();
        currentFansiteIds.remove(id);
        setFansiteIds(currentFansiteIds);
    }
}
