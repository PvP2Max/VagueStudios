package com.vaguestudios.bot.commands.profile;

import com.vaguestudios.bot.database.DatabaseConnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewsCommand {
    public void handleCommand(SlashCommandInteractionEvent event) {
        String userId = event.getOption("user").getAsUser().getId();
        String vouchleyUsername = ""; // Default Vouchley Username

        try (Connection conn = DatabaseConnector.connect()) {
            String sql = "SELECT VouchleyUsername FROM freelancers WHERE UserID = ?;";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    vouchleyUsername = rs.getString("VouchleyUsername");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (vouchleyUsername == null || vouchleyUsername.isEmpty()) {
            event.reply("This freelancer does not have a Vouchley username set.").setEphemeral(true).queue();
        } else {
            String vouchleyLink = "https://vouchley.com/user/" + vouchleyUsername;
            event.replyEmbeds(new EmbedBuilder()
                            .setTitle("Vouchley Reviews")
                            .setDescription("Vouchley profile for the user: [" + vouchleyUsername + "](" + vouchleyLink + ")")
                            .setColor(Color.BLUE)
                            .build())
                 .setEphemeral(true)
                 .queue();
        }
    }
}
