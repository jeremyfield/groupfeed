package com.turtles.groupfeed;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;
import twitter4j.TwitterStream;

import java.util.List;

public class GroupFeedBot {

    private static IDiscordClient discordClient;

    public GroupFeedBot(String token, TwitterStream twitterStream) {
        ClientBuilder clientBuilder = new ClientBuilder().withToken(token).registerListener(new GroupFeedEvents(twitterStream));
        try {
            discordClient = clientBuilder.login();
        } catch (DiscordException e) {
            System.err.println("Discord bot could not log in: ");
            e.printStackTrace();
        }
    }

    public IDiscordClient getDiscordClient() {
        return discordClient;
    }

    public static void sendMessageToGroupChannel(final String message) {
        List<Long> channelIds = PropertiesReader.getGroupChannelIds();
        channelIds.forEach(channelId -> sendMessage(discordClient.getChannelByID(channelId), message));
    }

    public static void sendMessage(final String memberName, final String message) {
        List<Long> channelIds = PropertiesReader.getMemberChannelIds(memberName);
        channelIds.forEach(channelId -> sendMessage(discordClient.getChannelByID(channelId), message));
    }

    public static void sendMessage(final IChannel channel, final String message) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(message);
            } catch (DiscordException e) {
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
            }
        });
    }
}
