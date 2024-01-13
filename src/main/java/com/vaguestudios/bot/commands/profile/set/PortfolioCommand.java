package com.vaguestudios.bot.commands.profile.set;

import com.vaguestudios.bot.database.DatabaseConnector;
import com.vaguestudios.bot.utils.RoleChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PortfolioCommand {
    public void handleCommand(SlashCommandInteractionEvent event) {
        if(RoleChecker.hasRequiredRole(event, "freelancer_role")) {
            String portfolioLink = event.getOption("portfolio").getAsString();
            if (isValidURL(portfolioLink)) {
                try (Connection conn = DatabaseConnector.connect()) {
                    String sql = "INSERT INTO freelancers (UserID, Portfolio) VALUES(?,?) ON CONFLICT(UserID) DO UPDATE SET Portfolio = ?;";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, event.getUser().getId());
                        pstmt.setString(2, portfolioLink);
                        pstmt.setString(3, portfolioLink);
                        pstmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                event.replyEmbeds(new EmbedBuilder()
                                .setTitle("Portfolio Update")
                                .setDescription("Your portfolio link has been set to: " + portfolioLink)
                                .setColor(Color.BLUE)
                                .build())
                        .setEphemeral(true)
                        .queue();
            } else {
                event.reply("The provided link is not valid. Please provide a valid URL.")
                        .setEphemeral(true)
                        .queue();
            }
        } else {
            event.reply("You do not have the required role to use this command.").setEphemeral(true).queue();
        }
    }
private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}

