package com.vaguestudios.bot.commands.profile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vaguestudios.bot.database.DatabaseConnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileCommand {
    public void handleCommand(SlashCommandInteractionEvent event) {
        // TODO: Fetch user profile and reviews from Vouchley
        // TODO: Construct and send the embed

        String userId = event.getOption("user").getAsUser().getId();
        String bio = "No bio provided.";  // Default bio text
        String portfolioLink = "No porfotlio provided."; // Default Portfolio text
        String timezone = "none"; // Default timezone text
        String avatarUrl = event.getOption("user").getAsUser().getEffectiveAvatarUrl();
        String vouchleyUsername = "VagueStudios";
        String url = "https://www.vouchley.com/api/v1/user?username=" + vouchleyUsername;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer 9aec1d81-ce3a-48d6-956a-0cd0ce3edd19") // Replace with your API key
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                    JsonObject userObject = jsonObject.getAsJsonObject("user");
                    String averageRating = userObject.get("average_rating").getAsString();
                    String totalValue = userObject.get("total_value").getAsString();

                });

        try (Connection conn = DatabaseConnector.connect()) {
            String sql = "SELECT Bio, Portfolio, Timezone, VouchleyUsername FROM freelancers WHERE UserID = ?;";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    bio = rs.getString("Bio") != null ? rs.getString("Bio") : "No bio set.";
                    portfolioLink = rs.getString("Portfolio") != null ? rs.getString("Portfolio") : "No portfolio set.";
                    timezone = rs.getString("Timezone") != null ? rs.getString("Timezone") : "No timezone set.";
                    vouchleyUsername = rs.getString("VouchleyUsername") != null ? rs.getString("VouchleyUsername") : "";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String finalVouchleyUsername = vouchleyUsername;
        String finalTimezone = timezone;
        String finalPortfolioLink = portfolioLink;
        String finalBio = bio;
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                    JsonObject userObject = jsonObject.getAsJsonObject("user");
                    String averageRating = userObject.get("average_rating").getAsString();
                    String totalValue = userObject.get("total_value").getAsString();

                    // Move embed-building logic here
                    event.replyEmbeds(new EmbedBuilder()
                                    .setTitle(event.getOption("user").getAsUser().getEffectiveName() + "'s Profile")
                                    .setDescription(finalBio.isEmpty() ? "No bio set." : finalBio)
                                    .addField("**Portfolio Link**", finalPortfolioLink, true)
                                    .addField("**Timezone**", finalTimezone, true)
                                    .addField("**Completed Commissions**", "**n/a**", true)
                                    .addField("Vouchley", "[" + finalVouchleyUsername + "](https://vouchley.com/user/" + finalVouchleyUsername + ")", true)
                                    .addField("Rating", "Average Rating: " + getStarRating(averageRating) + "\nTotal Value: $" + totalValue, true)
                                    .setColor(Color.BLUE)
                                    .setThumbnail(avatarUrl)
                                    .setFooter("Tip: use /reviews <@freelancer> to view all of a freelancer's reviews.")
                                    .build())
                            .setEphemeral(true)
                            .queue();
                });
    }
    private String getStarRating(String averageRating) {
        float rating = Float.parseFloat(averageRating);
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (rating > i) {
                stars.append(rating - i >= 1 ? "★" : "☆");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }
}