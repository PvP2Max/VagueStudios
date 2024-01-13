package com.vaguestudios.bot.commands.profile.set;

import com.vaguestudios.bot.database.DatabaseConnector;
import com.vaguestudios.bot.utils.RoleChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BioCommand {
    public void handleCommand(SlashCommandInteractionEvent event) {
        if(RoleChecker.hasRequiredRole(event, "freelancer_role")) {
            if (event.getOption("bio") != null) {
                String bioText = event.getOption("bio").getAsString();
                if(bioText.length() <= 500) {
                    try (Connection conn = DatabaseConnector.connect()) {
                        String sql = "INSERT INTO freelancers (UserID, Bio) VALUES(?,?) ON CONFLICT(UserID) DO UPDATE SET Bio = ?;";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, event.getUser().getId());
                            pstmt.setString(2, bioText);
                            pstmt.setString(3, bioText);
                            pstmt.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                // Send confirmation embed
                event.replyEmbeds(new EmbedBuilder()
                                .setTitle("Vague Studios")
                                .setDescription("Your bio has been set to: " + bioText)
                                .setColor(Color.BLUE)
                                .build())
                        .setEphemeral(true)
                        .queue();
                } else {
                    event.reply("Your bio must be 500 characters or less.").setEphemeral(true).queue();
                }
            } else {
                // Handle the case where bio is not provided
                event.reply("Bio was not provided! Please use the command like `/set bio Your bio here`.")
                        .setEphemeral(true)
                        .queue();
            }
        } else {
            event.reply("You do not have the required role to use this command.").setEphemeral(true).queue();
        }
    }
}
