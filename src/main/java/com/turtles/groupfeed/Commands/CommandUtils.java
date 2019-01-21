package com.turtles.groupfeed.Commands;

import com.turtles.groupfeed.Properties.DiscordPropertiesReader;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;

import java.util.List;

public class CommandUtils {

    public static boolean isContributor(MessageReceivedEvent event) {
        if(event.getAuthor().getLongID() == DiscordPropertiesReader.getBotOwnerId()) {
            return true;
        }

        List<IRole> roles = event.getAuthor().getRolesForGuild(event.getGuild());
        return roles.stream()
                .map(IRole::getName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase("contributor"));
    }
}
