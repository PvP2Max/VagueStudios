package com.vaguestudios.bot.commands.tickets;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

public class NewSupport {
    public void handleCommand(ButtonInteractionEvent event) {
        event.deferReply(true).queue(); // Acknowledge the button click
        Member member = event.getMember();
        if (member == null) return; // Ensure member is not null

        Properties prop = new Properties();
        String supportCategoryId;

        try (FileInputStream in = new FileInputStream("settings.properties")) {
            prop.load(in);
            supportCategoryId = prop.getProperty("supportcategory");
        } catch (IOException e) {
            e.printStackTrace();
            return; // Handle the error appropriately
        }

        long categoryId = Long.parseLong(supportCategoryId); // Convert the ID to a long
        Guild guild = event.getGuild();

        if (guild != null) {
            String channelName = "support-" + member.getUser().getName();
            List<TextChannel> channels = guild.getTextChannelsByName(channelName, true);
            if (!channels.isEmpty()) {
                event.getHook().editOriginal("You already have an open support ticket.").queue();
                return; // Exit if a ticket already exists
            }

            guild.getCategoryById(categoryId).createTextChannel(channelName)
                    .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), null)
                    .queue(channel -> {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("Vague Studios Support")
                                .setDescription("A member of our support team will be with you shortly. Until then, please provide a detailed description of what you are needing assistance with.")
                                .setColor(Color.BLUE);

                        channel.sendMessageEmbeds(embed.build())
                                .setActionRow(
                                        Button.danger("archive", "Archive").withEmoji(Emoji.fromUnicode("\uD83D\uDDD1")) // Unicode for wastebasket emoji
                                )
                                .queue();

                        event.getHook().editOriginal("Support ticket created successfully.").setComponents().queue();
                    });
        };
    }
}