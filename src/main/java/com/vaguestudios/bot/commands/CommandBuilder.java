package com.vaguestudios.bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CommandBuilder {
    public static void initializeCommands(JDA jda){

        // Creates a new Property
        Properties prop = new Properties();

        // Loads the config.properties file
        try (FileInputStream ip = new FileInputStream("config.properties")) {
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Defines the Commands
        var profileCommand = Commands.slash("profile", "View the profile of a Freelancer")
                .addOptions(new OptionData(OptionType.USER, "user", "The Freelancer you want to view", true));
        var setCommand = Commands.slash("set", "Update your Freelancer Profile.")
                .addSubcommands(
                        new SubcommandData("bio", "Update your bio.")
                                .addOptions(new OptionData(OptionType.STRING, "bio", "Your new bio", true)),
                        new SubcommandData("portfolio", "Update your portfolio link.")
                                .addOptions(new OptionData(OptionType.STRING, "portfolio", "Your portfolio link", true)),
                        new SubcommandData("timezone", "Update your timezone.")
                                .addOptions(new OptionData(OptionType.STRING, "timezone", "Your timezone.", true)
                                        .addChoice("GMT - 12:00", "GMT-12")
                                        .addChoice("GMT - 11:00", "GMT-11")
                                        .addChoice("GMT - 10:00", "GMT-10")
                                        .addChoice("GMT - 09:00", "GMT-9")
                                        .addChoice("GMT - 08:00", "GMT-8")
                                        .addChoice("GMT - 07:00", "GMT-7")
                                        .addChoice("GMT - 06:00", "GMT-6")
                                        .addChoice("GMT - 05:00", "GMT-5")
                                        .addChoice("GMT - 04:00", "GMT-4")
                                        .addChoice("GMT - 03:00", "GMT-3")
                                        .addChoice("GMT - 02:00", "GMT-2")
                                        .addChoice("GMT - 01:00", "GMT-1")
                                        .addChoice("GMT - 00:00", "GMT-0")
                                        .addChoice("GMT + 01:00", "GMT-1")
                                        .addChoice("GMT + 02:00", "GMT-2")
                                        .addChoice("GMT + 03:00", "GMT-3")
                                        .addChoice("GMT + 04:00", "GMT-4")
                                        .addChoice("GMT + 05:00", "GMT-5")
                                        .addChoice("GMT + 06:00", "GMT-6")
                                        .addChoice("GMT + 07:00", "GMT-7")
                                        .addChoice("GMT + 08:00", "GMT-8")
                                        .addChoice("GMT + 09:00", "GMT-9")
                                        .addChoice("GMT + 10:00", "GMT-10")
                                        .addChoice("GMT + 11:00", "GMT-11")
                                        .addChoice("GMT + 12:00", "GMT-12")),
                        new SubcommandData("vouchley", "Update your Vouchley username.")
                                .addOptions(new OptionData(OptionType.STRING, "vouchleyusername", "Your Vouchley username.", true)),
                        new SubcommandData("paypal", "Update your PayPal e-mail.")
                                .addOptions(new OptionData(OptionType.STRING, "email", "Your PayPal e-mail.", true)));
        var reviewsCommand = Commands.slash("reviews", "View the reviews of a Freelancer")
                .addOptions(new OptionData(OptionType.USER, "user", "The Freelancer you want to view.", true));
        var viewpaypalCommand = Commands.slash("viewpaypal", "View the paypal email of a Freelancer")
                .addOptions(new OptionData(OptionType.USER, "user", "The Freelancer you want to view.", true));
        var panelCommand = Commands.slash("panel", "Send a user panel.")
                .addOptions(new OptionData(OptionType.STRING, "type","Choose your panel", true)
                        .addChoice("Tickets", "ticketspanel")
                        .addChoice("Profile","profilepanel")
                        .addChoice("Bank", "bankpanel"));
        var settingsCommand = Commands.slash("settings", "Discord bot settings command.")
                .addSubcommands(new SubcommandData("set", "Update a certain settings value.")
                                .addOptions(new OptionData(OptionType.STRING, "name", "Setting Name.", true)
                                        .addChoice("Support Category", "supportcategory")
                                        .addChoice("Commissions Category", "commissionscategory")
                                        .addChoice("Applications Category", "applicationscategory")
                                        .addChoice("Executive Role ID", "executive_role")
                                        .addChoice("Support Role ID", "support_role")
                                        .addChoice("Commission Manager Role ID", "cm_role")
                                        .addChoice("Human Resources Agent Role ID", "hr_role")
                                        .addChoice("Freelancer Role ID", "freelancer_role")
                                        .addChoice("VIP Client Role ID", "vipclient_role")
                                        .addChoice("VIP Client Commissions Required", "vipclient_commissions")
                                        .addChoice("Client Role ID", "client_role"),
                                new OptionData(OptionType.STRING, "value", "New value for the setting.", true)));


        // This READS the guild_id and sets it to guildID variable
        String guildID = prop.getProperty("guild_id");
        if (guildID == null || guildID.isEmpty()){
            System.err.println("Please configure the guild_id with your server id in config.properties");
        }

        // Registers the command at the GUILD level only.
        jda.getGuildById(guildID).upsertCommand(profileCommand).queue();
        jda.getGuildById(guildID).upsertCommand(setCommand).queue();
        jda.getGuildById(guildID).upsertCommand(reviewsCommand).queue();
        jda.getGuildById(guildID).upsertCommand(viewpaypalCommand).queue();
        jda.getGuildById(guildID).upsertCommand(panelCommand).queue();
        jda.getGuildById(guildID).upsertCommand(settingsCommand).queue();
    }
}