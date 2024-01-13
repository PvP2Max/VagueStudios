package com.vaguestudios.bot.commands.profile;

import com.vaguestudios.bot.database.DatabaseConnector;
import com.vaguestudios.bot.utils.RoleChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewPayPalCommand {

    public void handleCommand(SlashCommandInteractionEvent event) {
    if (RoleChecker.hasRequiredRole(event, "executive_role")) {
        String userId = event.getUser().getId();
        String paypalEmail = ""; // Fetch from database

            try (Connection conn = DatabaseConnector.connect()) {
                String sql = "SELECT PayPalEmail FROM freelancers WHERE UserID = ?;";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, userId);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        paypalEmail = rs.getString("PayPalEmail");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (paypalEmail.isEmpty()) {
                event.reply("No PayPal email is set for this user.").setEphemeral(true).queue();
            } else {
                event.replyEmbeds(new EmbedBuilder()
                                .setTitle("PayPal Email")
                                .setDescription(event.getUser().getName() + "'s PayPal email is: ```" + paypalEmail + "```")
                                .setColor(Color.BLUE)
                                .build())
                        .setEphemeral(true)
                        .queue();
            }
        } else {
            event.reply("You do not have the required role to use this command.").setEphemeral(true).queue();
        }
    }
}

