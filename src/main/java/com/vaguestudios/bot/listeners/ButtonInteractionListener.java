package com.vaguestudios.bot.listeners;


import com.vaguestudios.bot.commands.tickets.NewApplication;
import com.vaguestudios.bot.commands.tickets.NewSupport;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonInteractionListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        if ("commissions".equals(buttonId)) {
            System.out.println("Received Button Interaction: " + event.getType());
            // Handle Application button
        } else if ("application".equals(buttonId)) {
            System.out.println("Received Button Interaction: " + event.getType());
            Member member = event.getMember();
            NewApplication newapplication = new NewApplication();
            newapplication.handleCommand(event);
        } else if ("support".equals(buttonId)) {
            System.out.println("Received Button Interaction: " + event.getType());
            Member member = event.getMember();
            NewSupport newsupportticket = new NewSupport();
            newsupportticket.handleCommand(event);

        }
    }
}