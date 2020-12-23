package dev.darkdragon.customscoreboard.events;

import dev.darkdragon.customscoreboard.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

import java.util.Collections;

public class playerJoin implements Listener {
    Plugin plugin;
    public playerJoin(Main main) {
        plugin = main;
    }
    int maxChar = 10;
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e){
        final Player p = e.getPlayer();

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {



        },0, 20 * 5);
    }
}
