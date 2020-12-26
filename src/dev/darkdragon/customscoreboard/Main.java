package dev.darkdragon.customscoreboard;

import dev.darkdragon.customscoreboard.events.playerJoin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Main extends JavaPlugin {
    private Economy economy;
    private static Economy econ;
    @Override
    public void onEnable() {

        sendConsole("This plugin has been made by me, DarkDragon, please share and rate this plugin in it's Spigot page and join my discord server (discord.darkdragon.dev).");

        if (!setupVault()) {
            sendConsole("Can't manage to get Vault plugin (economy, chat...) , perhaps it is not installed");
        }else if(!setupEconomy()) {
            sendConsole("Can't manage to get Economy working.");
        }


        InputStream configIS = getResource("config.yml");
        InputStream scoreboardIs = getResource("scoreboard.json");
        File configFile = new File(getDataFolder(),"config.yml");
        File scoreboards = new File(getDataFolder().toString(),"Scoreboards");
        File scoreboard = new File(scoreboards, "scoreboard.json");
        if(!configFile.exists()) {
            sendConsole("Config file doesn't exist, creating one");
            try {
                if ( getDataFolder().mkdir() ) sendConsole("Plugin folder created");
                Files.copy(configIS,configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!scoreboards.exists()) {
            sendConsole("No scoreboards folder found, creating one");
            if ( scoreboards.mkdir() ) sendConsole("Scoreboards folder created");
        }
        if(!scoreboard.exists()){
            sendConsole("No custom scoreboard file found, creating an example");
            try {
                Files.copy(scoreboardIs, scoreboard.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bukkit.getPluginManager().registerEvents(new playerJoin(this), this);
        
        super.onEnable();
    }



    @Override
    public void onDisable() {
        sendConsole("Remember to support me by sharing and rating this plugin in it's Spigot page and joining my discord server (discord.darkdragon.dev)");
        super.onDisable();
    }

    private boolean setupVault() {
        return Bukkit.getPluginManager().getPlugin("Vault") != null;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if ( rsp == null ) {
            return false;
        }
        economy = rsp.getProvider();
        econ = economy;
        return economy != null;
    }

    public static Economy getEconomy() {
        return econ;
    }



    private void sendConsole(String message) {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CustomScoreboards] (SNAPSHOT) "+ ChatColor.AQUA + message);
    }
}
