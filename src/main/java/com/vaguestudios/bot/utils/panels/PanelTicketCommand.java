package com.vaguestudios.bot.utils.panels;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PanelTicketCommand {
    public void handleCommand(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        Properties prop = new Properties();
        String imageUrl = null;
        try (FileInputStream ip = new FileInputStream("config.properties")) {
            prop.load(ip);
            imageUrl = prop.getProperty("panelimage");
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextChannel channel = event.getChannel().asTextChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Vague Studios Support");
        embed.setDescription("");
        embed.setImage(imageUrl); // Ensure imageUrl is defined and valid

        if (channel != null) {
            channel.sendMessageEmbeds(embed.build())
                    .setActionRow(
                            Button.primary("commissions", "Commissions").withEmoji(Emoji.fromCustom("order", Long.parseLong("1193703621255254016"),false)),
                            Button.primary("application", "Application").withEmoji(Emoji.fromCustom("apply", Long.parseLong("1193703661721890858"),false)),
                            Button.primary("support", "Support").withEmoji(Emoji.fromCustom("support", Long.parseLong("1193703716847620246"),false))
                    )
                    .queue();
            event.getHook().editOriginal("Panel successfully created.").setComponents().queue();
        } else {
            System.out.println("Error: Target channel not found.");
        }
    }
}
