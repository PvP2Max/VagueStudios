package com.vaguestudios.bot.commands.profile.set;

import com.vaguestudios.bot.database.DatabaseConnector;
import com.vaguestudios.bot.utils.RoleChecker;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PayPalEmailCommand {
    public void handleCommand(SlashCommandInteractionEvent event) {
        if(RoleChecker.hasRequiredRole(event, "freelancer_role")) {
            String email = event.getOption("email").getAsString();
            if (isValidEmail(email)) {
                try (Connection conn = DatabaseConnector.connect()) {
                    String sql = "INSERT INTO freelancers (UserID, PayPalEmail) VALUES(?,?) ON CONFLICT(UserID) DO UPDATE SET PayPalEmail = ?;";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, event.getUser().getId());
                        pstmt.setString(2, email);
                        pstmt.setString(3, email);
                        pstmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                event.reply("Your PayPal email has been set to: " + email).setEphemeral(true).queue();
            } else {
                event.reply("The provided email is not valid. Please provide a valid email.").setEphemeral(true).queue();
            }
        } else {
            event.reply("You do not have the required role to use this command.").setEphemeral(true).queue();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}