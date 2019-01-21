package com.turtles.groupfeed;

import com.turtles.groupfeed.Commands.*;
import com.turtles.groupfeed.Properties.MembersPropertiesReader;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;
import twitter4j.TwitterStream;

import java.util.Set;

public class GroupFeedBot {

    private static IDiscordClient discordClient;

    public GroupFeedBot(String token, TwitterStream twitterStream) {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.addCommand("check", new CheckCommand());
        commandHandler.addCommand("post", new PostCommand());
        commandHandler.addCommand("uptime", new UptimeCommand());
        commandHandler.addCommand("follow", new FollowCommand(twitterStream));
        commandHandler.addCommand("unfollow", new UnfollowCommand(twitterStream));
        commandHandler.addCommand("refresh", new RefreshCommand(twitterStream));

        ClientBuilder clientBuilder = new ClientBuilder().withToken(token).registerListener(commandHandler);
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
        Set<Long> channelIds = MembersPropertiesReader.getGroupChannelIds();
        channelIds.forEach(channelId -> sendMessage(discordClient.getChannelByID(channelId), message));
    }

    public static void sendMessage(final IdolMember idolMember, final String message) {
        Set<Long> channelIds = idolMember.getChannelIds();
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
