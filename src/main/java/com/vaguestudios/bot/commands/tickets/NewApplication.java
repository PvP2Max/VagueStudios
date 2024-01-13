package com.vaguestudios.bot.commands.tickets;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

public class NewApplication {
    public void handleCommand(ButtonInteractionEvent event) {
        event.deferReply(true).queue();
        Member member = event.getMember();
        if (member == null) return;

        Properties prop = new Properties();
        String categoryId;
        String newApplicationMessage;
        List<String> services;

        try (FileInputStream in = new FileInputStream("settings.properties")) {
            prop.load(in);
            categoryId = prop.getProperty("applicationscategory");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (FileInputStream in = new FileInputStream("config.properties")) {
            prop.load(in);
            newApplicationMessage = prop.getProperty("newapplication");
            String servicesRaw = prop.getProperty("services");
            services = List.of(servicesRaw.split(","));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        long categoryLongId = Long.parseLong(categoryId);
        Guild guild = event.getGuild();

        if (guild != null) {
            String channelName = "pending-" + member.getUser().getName();
            List<TextChannel> channels = guild.getTextChannelsByName(channelName, true);
            if (!channels.isEmpty()) {
                event.getHook().editOriginal("You already have an open application ticket.").queue();
                return;
            }

            guild.getCategoryById(categoryLongId).createTextChannel(channelName)
                    .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), null)
                    .queue(channel -> {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("Vague Studios Application")
                                .setDescription(newApplicationMessage)
                                .setColor(Color.BLUE);

                        channel.sendMessageEmbeds(embed.build()).queue();

                        StringSelectMenu.Builder menu = StringSelectMenu.create("menu:services").setPlaceholder("Choose a service category");
                        for (String service : services) {
                            menu.addOption(service.trim(), service.trim());
                        }

                        channel.sendMessage("Select which category your service you'd like to apply for falls under:")
                                .setActionRow(menu.build())
                                .queue();
                    });
        }
    }
}
