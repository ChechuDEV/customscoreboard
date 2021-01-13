package dev.chechu.customscoreboard.events;

import dev.chechu.customscoreboard.CustomBoard;
import dev.chechu.customscoreboard.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScoreboardCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if ( !player.hasPermission("customscoreboard")) noPerms(player);
        switch (strings[0]) {
            case "reload":
                if ( !player.hasPermission("customscoreboard.reload")) noPerms(player);
                reload();
                break;
            case "hide":
                if ( !player.hasPermission("customscoreboard.hide")) noPerms(player);
                hide(player);
                break;
            case "unhide":
                if ( !player.hasPermission("customscoreboard.hide")) noPerms(player);
                unhide(player);
                break;
            case "help":
                help(player);
                break;
            case "info":
                info(player);
                break;
            default:
                notFound(player);
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> tabComplete = new ArrayList<>();
        if ( commandSender.hasPermission("customscoreboard")) {
            tabComplete.add("help");
            tabComplete.add("info");
        }

        if ( commandSender.hasPermission("customscoreboard.hide")) {
            tabComplete.add("hide");
            tabComplete.add("unhide");
        }

        if ( commandSender.hasPermission("customscoreboard.reload")) tabComplete.add("reload");

        Collections.sort(tabComplete);
        return strings.length == 1 ? tabComplete : null;
    }

    private void reload() {
        Main.getPlugin().reloadConfig();
        Main.scoreboardData.setScoreboard(Main.getPlugin().getConfig().getStringList("scoreboard-lines"));
        Main.boards.clear();
        Bukkit.getScheduler().cancelTasks(Main.getPlugin());
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if ( onlinePlayer == null ) break;
            Main.createBoard(onlinePlayer);
        }
    }

    private void hide(Player player) {
        Main.boards.stream().filter(customBoard -> customBoard.getPlayer().equals(player)).findFirst().ifPresent(CustomBoard::hideScoreboard);
    }

    private void unhide(Player player) {
        Main.boards.stream().filter(customBoard -> customBoard.getPlayer().equals(player)).findFirst().ifPresent(CustomBoard::unHideScoreboard);
    }

    private void help(Player player) {
        player.sendMessage(ChatColor.AQUA + "[Custom Scoreboard] HELP");
        player.sendMessage(ChatColor.AQUA + "W.I.P. :(");
    }

    private void info(Player player) {
        player.sendMessage(ChatColor.AQUA + "[Custom Scoreboard] INFORMATION - V2.0-Snapshot");
        player.sendMessage(ChatColor.AQUA + "A plugin that allows you to set Custom Scoreboards on your server.");
        player.sendMessage(ChatColor.AQUA + "Made by Tsetsi.");
        player.sendMessage(ChatColor.AQUA + "Check it out in Spigot: https://www.spigotmc.org/resources/custom-scoreboard.87154/");
    }

    private void noPerms(Player player) {
        player.sendMessage(ChatColor.RED + "[Custom Scoreboard] You have no permissions for this command.");
    }

    private void notFound(Player player) {
        player.sendMessage(ChatColor.RED + "[Custom Scoreboard] Command doesn't exist, please use \"" + ChatColor.AQUA + "/scoreboard help" + ChatColor.RED + "\".");
    }

}
