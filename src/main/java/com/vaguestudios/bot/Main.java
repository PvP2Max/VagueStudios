package com.vaguestudios.bot;

import com.vaguestudios.bot.commands.CommandBuilder;
import com.vaguestudios.bot.database.DatabaseConnector;
import com.vaguestudios.bot.listeners.ButtonInteractionListener;
import com.vaguestudios.bot.listeners.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        // Create a Properties Object
        Properties prop = new Properties();

        // Load the config.properties file
        try (FileInputStream ip = new FileInputStream("config.properties")) {
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // This READS the bot_token
        String token = prop.getProperty("bot_token");
        if (token == null || token.isEmpty()) {
            System.err.println("Please configure your bot token in config.properties");
            return;
        }

        // Array of statuses and corresponding activity types
        Activity[] statusMessages = new Activity[]{
                Activity.customStatus("Vague Studios - MC Services"),
                Activity.customStatus("200+ Professional Freelancers"),
                Activity.customStatus("300+ Satisfied Clients"),
                Activity.customStatus("Thousands of orders completed."),
                Activity.customStatus("Quality Products, Affordable Prices"),
                Activity.customStatus("Where Your Vision Comes Alive!")
        };
        try {
            // Initializes JDA and waits for JDA to fully connect
            JDA jda = JDABuilder.createDefault(token)
                    // This registers the SlashCommandListener
                    .addEventListeners(new SlashCommandListener())
                    .build()
                    .awaitReady();

            // This will activate the CommandBuilder Class
            CommandBuilder.initializeCommands(jda);

            // This will activate the ButtonInteractionListener Class
            jda.addEventListener(new ButtonInteractionListener());

            // This will connect to the Database
            // This creates the Database If it doesn't exist
            String sqlCreate = "CREATE TABLE IF NOT EXISTS freelancers (UserID TEXT PRIMARY KEY, Bio TEXT, Portfolio TEXT, Timezone TEXT, VouchleyUsername TEXT, PayPalEmail TEXT);";
            try (Connection conn = DatabaseConnector.connect();
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sqlCreate);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            // Schedule to rotate status every 10 seconds
            Timer timer = new Timer();
            timer.schedule(new RotateStatusTask(jda, statusMessages), 0, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Status Tracker
    static class RotateStatusTask extends TimerTask {
        private final JDA jda;
        private final Activity[] messages;
        private int currentIndex = 0;

        public RotateStatusTask(JDA jda, Activity[] messages) {
            this.jda = jda;
            this.messages = messages;
        }

        @Override
        public void run() {
            //This sets the current status
            jda.getPresence().setActivity(messages[currentIndex]);

            // Increment index, reset if at the end
            currentIndex = (currentIndex + 1) % messages.length;
        }
    }

}


