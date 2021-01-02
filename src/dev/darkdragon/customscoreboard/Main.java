package dev.darkdragon.customscoreboard;

import dev.darkdragon.customscoreboard.commands.ScoreboardCommands;
import dev.darkdragon.customscoreboard.events.playerJoin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends JavaPlugin {
    private static Economy econ;
    private static boolean economyOn = true;
    @Override
    public void onEnable() {

        // ADS
        sendConsole("This plugin has been made by me, DarkDragon, please share and rate this plugin in it's Spigot page and join my discord server (discord.darkdragon.dev).");

        // File init
        InputStream configIS = getResource("config.yml");
        InputStream scoreboardIs = getResource("scoreboard.json");
        File configFile = new File(getDataFolder(),"config.yml");
        File scoreboards = new File(getDataFolder().toString(),"Scoreboards");

        // Config checker v1, not checking all variables TODO
        if ( !configFile.exists() ) {
            sendConsole("Config file doesn't exist, creating one");
            if ( getDataFolder().mkdir() ) sendConsole("Plugin folder created");
            copyFile(configIS, configFile.toPath());
        }

        // Get scoreboard path from config file
        File scoreboard = new File(scoreboards, getConfig().getString("scoreboard-file") + ".json");

        // Creates a folder for scoreboards if it doesn't exist
        if ( !scoreboards.exists() ) {
            sendConsole("No scoreboards folder found, creating one.");
            if ( scoreboards.mkdir() ) sendConsole("Scoreboards folder created");
        }

        // Creates an example file if it doesn't exist
        if ( scoreboards.isDirectory() ) {
            if(isEmpty(scoreboards)) {
                sendConsole("Scoreboards folder is empty. Creating an example of scoreboard, please check your config.yml.");
                copyFile(scoreboardIs, scoreboard.toPath());
            }
        }

        // Checks if Economy is available
        if(!setupEconomy()) {
            economyOn = false;
            sendConsole(ChatColor.DARK_RED + "Can't manage to get Economy working, perhaps Vault is not installed.");
        }

        // Starts showing the scoreboard to players on join
        Bukkit.getPluginManager().registerEvents(new playerJoin(this), this);

        // Starts command listening
        this.getCommand("scoreboard").setExecutor(new ScoreboardCommands(this));

        super.onEnable();
    }


    private void copyFile(InputStream in, Path out) {
        try {
            Files.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty(File file) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(file.toPath())) {
            return !directoryStream.iterator().hasNext();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public void onDisable() {
        sendConsole("Remember to support me by sharing and rating this plugin in it's Spigot page and joining my discord server (discord.darkdragon.dev)");
        super.onDisable();
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static boolean isEconomyOn() {
        return economyOn;
    }


    private void sendConsole(String message) {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CustomScoreboards] (SNAPSHOT) "+ ChatColor.AQUA + message);
    }
}
