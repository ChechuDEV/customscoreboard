package dev.darkdragon.customscoreboard.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.darkdragon.customscoreboard.Main;
import dev.darkdragon.customscoreboard.Objects.PlayerScoreboardsObj;
import dev.darkdragon.customscoreboard.Objects.ScoreboardObj;
import dev.darkdragon.customscoreboard.events.playerJoin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScoreboardCommands implements CommandExecutor {
    Plugin plugin;
    public ScoreboardCommands(Main main) {
        plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;

        if(!player.hasPermission("customscoreboard")) {
            noPerms(player);
        }
        switch (args[0]) {
            case "help":
                helpShow(player);
                return true;
            case "info":
                infoShow(player);
                return true;
            case "reload":
                reload(player);
                return true;
            case "hide":
                hide(player);
                return true;
            case "unhide":
                unhide(player);
                return true;
            default:
                player.sendMessage(ChatColor.RED + "[CustomScoreboard] Command not found");
                return true;
        }
    }

    private void unhide(Player player) {
        if ( player.hasPermission("customscoreboard.hide") ) {
            playerJoin.showScoreboard(player);
        }else noPerms(player);
    }

    private void hide(Player player) {
        if ( player.hasPermission("customscoreboard.hide") ) {
            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            PlayerScoreboardsObj playerObj = playerJoin.playerScoreboardsObjs.stream().filter(playerScoreboardsObj -> player.equals(playerScoreboardsObj.getPlayer())).findFirst().orElse(null);
            if (playerObj != null) {
                Bukkit.getScheduler().cancelTask(playerObj.getID());
                playerJoin.playerScoreboardsObjs.remove(playerObj);
            }
        }else noPerms(player);
    }


    // Half working???? FIXME
    private void reload(Player player) {
        if ( player.hasPermission("customscoreboard.reload") ) {
            plugin.reloadConfig();
            for (PlayerScoreboardsObj playerScoreboardsObj : playerJoin.playerScoreboardsObjs) {
                Bukkit.getScheduler().cancelTask(playerScoreboardsObj.getID());
                playerJoin.playerScoreboardsObjs.remove(playerScoreboardsObj);
            }
            plugin.getServer().getOnlinePlayers().forEach(player1 -> playerJoin.showScoreboard(player));
        }else noPerms(player);
    }

    private void infoShow(Player player) {

    }

    private void helpShow(Player player) {

    }

    private void noPerms(Player player) {
        player.sendMessage(ChatColor.RED + "[CustomScoreboard] You do not have permissions for this command!");
    }
}
