package com.vaguestudios.bot.commands.profile.set;

import com.vaguestudios.bot.database.DatabaseConnector;
import com.vaguestudios.bot.utils.RoleChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VouchleyUsernameCommand {
    public void handleCommand(SlashCommandInteractionEvent event) {
        if(RoleChecker.hasRequiredRole(event, "freelancer_role")) {
            String vouchleyUsername = event.getOption("vouchleyusername").getAsString();

            try (Connection conn = DatabaseConnector.connect()) {
                String sql = "INSERT INTO freelancers (UserID, VouchleyUsername) VALUES(?,?) ON CONFLICT(UserID) DO UPDATE SET VouchleyUsername = ?;";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, event.getUser().getId());
                    pstmt.setString(2, vouchleyUsername);
                    pstmt.setString(3, vouchleyUsername);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            event.replyEmbeds(new EmbedBuilder()
                            .setTitle("Vouchley Username Update")
                            .setDescription("Your Vouchley username has been set to: " + vouchleyUsername)
                            .setColor(Color.BLUE)
                            .build())
                    .setEphemeral(true)
                    .queue();
        } else {
            event.reply("You do not have the required role to use this command.").setEphemeral(true).queue();
        }
    }
}
