package dev.darkdragon.customscoreboard;

import dev.darkdragon.customscoreboard.events.playerJoin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Economy economy;
    private static Economy econ;
    private Chat chat;
    @Override
    public void onEnable() {
        sendConsole("This plugin has been made by me, DarkDragon, please share and rate this plugin in it's Spigot page and join my discord server (discord.darkdragon.dev).");
        if (!setupVault()) {
            sendConsole("Can't manage to get Vault plugin (economy, chat...) , perhaps it is not installed");
            if(!setupEconomy()) {
                sendConsole("Can't manage to get Economy working.");
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
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CustomScoreboards] "+ ChatColor.AQUA + message);
    }
}
