package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.GroupFeedBot;
import com.turtles.groupfeed.Properties.DiscordPropertiesReader;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandUtils {

    public static boolean isContributor(MessageCreateEvent event) {
        long authorId = event.getMessageAuthor().getId();
        if(authorId == DiscordPropertiesReader.getBotOwnerId()) {
            return true;
        }

        Optional<Server> server = event.getServer();
        Optional<User> user = event.getMessageAuthor().asUser();
        List<Role> roles = server.isPresent() && user.isPresent()
                ? GroupFeedBot.getRolesOfUser(server.get(), user.get())
                : new ArrayList<>();
        return roles.stream()
                .map(Role::getName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase("contributor"));
    }
}
