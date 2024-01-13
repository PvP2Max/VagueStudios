package com.vaguestudios.bot.utils;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RoleChecker {
    private static Properties prop = new Properties();

    static {
        try (FileInputStream ip = new FileInputStream("settings.properties")) {
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasRequiredRole(SlashCommandInteractionEvent event, String roleNameKey) {
        String requiredRoleId = prop.getProperty(roleNameKey);
        return event.getMember() != null && event.getMember().getRoles().stream().anyMatch(role -> role.getId().equals(requiredRoleId));
    }
}

