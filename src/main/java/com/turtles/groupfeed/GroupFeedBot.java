package com.turtles.groupfeed;

import com.turtles.groupfeed.Commands.*;
import com.turtles.groupfeed.Properties.MembersPropertiesReader;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;
import twitter4j.TwitterStream;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GroupFeedBot {

    private static DiscordApi discordApi;

    public GroupFeedBot(String token, TwitterStream twitterStream) {
        FallbackLoggerConfiguration.setDebug(false);

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.addCommand("check", new CheckCommand());
        commandHandler.addCommand("post", new PostCommand());
        commandHandler.addCommand("uptime", new UptimeCommand());
        commandHandler.addCommand("follow", new FollowCommand(twitterStream));
        commandHandler.addCommand("unfollow", new UnfollowCommand(twitterStream));
        commandHandler.addCommand("refresh", new RefreshCommand(twitterStream));

        discordApi = new DiscordApiBuilder().setToken(token).login().join();
        discordApi.addListener(commandHandler);
    }

    public static void sendMessageToGroupChannel(final String message) {
        Set<Long> channelIds = MembersPropertiesReader.getGroupChannelIds();
        sendMessage(channelIds, message);
    }

    public static void sendMessage(final IdolMember idolMember, final String message) {
        Set<Long> channelIds = idolMember.getChannelIds();
        sendMessage(channelIds, message);
    }

    public static void sendMessage(Set<Long> channelIds, final String message) {
        channelIds.stream()
                .map(channelId -> discordApi.getTextChannelById(channelId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(channel -> channel.sendMessage(message));
    }

    public static List<Role> getRolesOfUser(Server server, User user) {
        return user.getRoles(server);
    }
}
