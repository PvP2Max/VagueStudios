package com.vaguestudios.bot.listeners;

import com.vaguestudios.bot.commands.profile.ProfileCommand;
import com.vaguestudios.bot.commands.profile.ReviewsCommand;
import com.vaguestudios.bot.commands.profile.ViewPayPalCommand;
import com.vaguestudios.bot.commands.profile.set.*;
import com.vaguestudios.bot.utils.panels.PanelTicketCommand;
import com.vaguestudios.bot.utils.settings.SetSettingsCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {
    private SlashCommandInteractionEvent event;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if ("profile".equals(event.getName())) {
            ProfileCommand profileCmd = new ProfileCommand();
            profileCmd.handleCommand(event);
        } else if ("set".equals(event.getName())) {
            if ("bio".equals(event.getSubcommandName())) {
                BioCommand bioCmd = new BioCommand();
                bioCmd.handleCommand(event);
            } else if ("portfolio".equals(event.getSubcommandName())) {
                PortfolioCommand portfolioCmd = new PortfolioCommand();
                portfolioCmd.handleCommand(event);
            } else if ("timezone".equals(event.getSubcommandName())) {
                TimezoneCommand timezoneCmd = new TimezoneCommand();
                timezoneCmd.handleCommand(event);
            } else if ("vouchley".equals(event.getSubcommandName())) {
                VouchleyUsernameCommand vouchleyusernameCmd = new VouchleyUsernameCommand();
                vouchleyusernameCmd.handleCommand(event);
            } else if ("paypal".equals(event.getSubcommandName())) {
                PayPalEmailCommand paypalemailCmd = new PayPalEmailCommand();
                paypalemailCmd.handleCommand(event);}
        } else if ("reviews".equals(event.getName())) {
            ReviewsCommand reviewsCmd = new ReviewsCommand();
            reviewsCmd.handleCommand(event);
        } else if ("viewpaypal".equals(event.getName())) {
            // System.out.println("Received command: " + event.getName());
            ViewPayPalCommand viewpaypalCmd = new ViewPayPalCommand();
            viewpaypalCmd.handleCommand(event);
        } else if ("panel".equals(event.getName())) {
            String option = event.getOption("type").getAsString();
            if ("ticketspanel".equals(option)) {
                PanelTicketCommand panelticketCmd = new PanelTicketCommand();
                panelticketCmd.handleCommand(event);
            } else if ("bankpanel".equals(option)) {

            } else if ("profilepanel".equals(option)) {

            }
        } else if ("settings".equals(event.getName()) && "set".equals(event.getSubcommandName())) {
            String setting = event.getOption("name").getAsString();
            String value = event.getOption("value").getAsString();
            new SetSettingsCommand().updateSetting(event, setting, value); // Assuming a method like this exists
        }
    }
}
