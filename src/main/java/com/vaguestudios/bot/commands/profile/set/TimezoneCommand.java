package com.vaguestudios.bot.commands.profile.set;

import com.vaguestudios.bot.database.DatabaseConnector;
import com.vaguestudios.bot.utils.RoleChecker;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TimezoneCommand {
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (RoleChecker.hasRequiredRole(event, "freelancer_role")) {
            String timezone = event.getOption("timezone").getAsString();
            if (isValidTimezone(timezone)) {
                try (Connection conn = DatabaseConnector.connect()) {
                    String sql = "INSERT INTO freelancers (UserID, Timezone) VALUES(?,?) ON CONFLICT(UserID) DO UPDATE SET Timezone = ?;";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, event.getUser().getId());
                        pstmt.setString(2, timezone);
                        pstmt.setString(3, timezone);
                        pstmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                event.reply("Your timezone has been set to: " + timezone).setEphemeral(true).queue();
            } else {
                event.reply("Invalid timezone provided. Please provide a valid GMT timezone.").setEphemeral(true).queue();
            }
        } else {
            event.reply("You do not have the required role to use this command.").setEphemeral(true).queue();
        }
    }

    private boolean isValidTimezone(String timezone) {
        // Define your list of valid GMT timezones
        List<String> validTimezones = Arrays.asList("GMT-12", "GMT-11", "GMT-10", "GMT-9", "GMT-8", "GMT-7", "GMT-6", "GMT-5", "GMT-4", "GMT-3", "GMT-2", "GMT-1", "GMT-0", "GMT+1", "GMT+2", "GMT+3", "GMT+4", "GMT+5", "GMT+6", "GMT+7", "GMT+8", "GMT+9", "GMT+10", "GMT+11", "GMT+12");
        return validTimezones.contains(timezone);
    }
}