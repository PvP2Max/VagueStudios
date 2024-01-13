package com.vaguestudios.bot.utils.settings;

import com.vaguestudios.bot.utils.RoleChecker;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SetSettingsCommand {
    public void updateSetting(SlashCommandInteractionEvent event, String name, String value) {
        if (RoleChecker.hasRequiredRole(event, "executive_role")) {
            Properties prop = new Properties();
            try (FileInputStream in = new FileInputStream("settings.properties")) {
                prop.load(in);
                prop.setProperty(name, value);
                prop.store(new FileOutputStream("settings.properties"), null);
                event.reply("Setting `" + name + "` has been updated to `" + value + "`.").setEphemeral(true).queue();
            } catch (IOException e) {
                event.reply("Failed to update the setting. Please check logs.").setEphemeral(true).queue();
                e.printStackTrace();
            }
        } else {
            event.reply("You do not have the required permissions.").setEphemeral(true).queue();
        }
    }
}

