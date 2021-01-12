package dev.chechu.customscoreboard;

import dev.chechu.customscoreboard.events.ScoreboardListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    private Logger log;
    private Economy econ;
    private Chat chat;

    private static Plugin plugin;

    public static ScoreboardData scoreboardData;

    @Override
    public void onEnable() {
        log = getLogger();
        log.info("Remember to rate and share this plugin. You can also join my discord server: discord.darkdragon.dev");

        scoreboardData = new ScoreboardData();
        // CONFIG FILE CHECKING AND CREATION
        File config = new File(getDataFolder(),"config.yml");
        InputStream configIS = getResource("config.yml");
        if( !config.exists() ) {
            log.warning("Config file not found, creating it.");
            if ( getDataFolder().mkdir() ) log.info("Plugin folder created.");
            if ( !copyFile(configIS, config) ) {
                log.severe("Couldn't create the config file, disabling the plugin!");
                getPluginLoader().disablePlugin(this);
            }
        }

        getConfig().getStringList("scoreboard-lines").forEach(s -> log.info(s));

        scoreboardData.setScoreboard(getConfig().getStringList("scoreboard-lines"));

        for (String s : scoreboardData.getScoreboard()) {
            if ( s.contains("{x}") || s.contains("{y}") || s.contains("{z}") ) scoreboardData.setXyzTag(true);
            if ( s.contains("{online}") ) scoreboardData.setMembersTag(true);
            if (s.contains("{money}")) scoreboardData.setMoneyTag(true);
        }

        // VAULT SETUP
        if ( vaultSetup() ) {

            // VAULT ECONOMY SETUP
            if (!economySetup()) log.warning("Can't hook up with Economy. Perhaps an economy plugin is missing.");

            // VAULT CHAT SETUP
            if (!chatSetup()) log.warning("Can't hook up with any Chat plugin.");

        } else log.warning("Vault not detected, can't hook up.");

        getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);

        plugin = this;

        super.onEnable();
    }

    private boolean copyFile(InputStream in, File out) {
        try {
            Files.copy(in, out.toPath());
            return true;
        } catch (IOException exception) {
            log.severe(exception.getMessage());
        }
        return false;
    }

    private boolean vaultSetup() {
        return Bukkit.getPluginManager().getPlugin("Vault") != null;
    }

    private boolean economySetup() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        scoreboardData.setEconomy(econ);
        return econ != null;
    }

    private boolean chatSetup() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = rsp.getProvider();
        scoreboardData.setChat(chat);
        return chat != null;
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
