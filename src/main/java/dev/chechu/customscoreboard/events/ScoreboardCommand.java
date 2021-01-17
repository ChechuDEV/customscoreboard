package dev.chechu.customscoreboard.events;

import dev.chechu.customscoreboard.CustomBoard;
import dev.chechu.customscoreboard.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if ( commandSender instanceof ConsoleCommandSender ) {
            commandSender.sendMessage(ChatColor.RED + "[CustomScoreboard] Using this plugin's commands with the console may cause many problems, so please use them with players.");
            return true;
        }
        Player player = (Player) commandSender;
        if ( !player.hasPermission("customscoreboard")) noPerms(player);
        switch (strings[0]) {
            case "reload":
                if ( !player.hasPermission("customscoreboard.reload")) {
                    noPerms(player);
                    break;
                }
                reload(player);
                break;
            case "hide":
                if ( !player.hasPermission("customscoreboard.hide")) {
                    noPerms(player);
                    break;
                }
                hide(player);
                break;
            case "show":
                if ( !player.hasPermission("customscoreboard.hide")) {
                    noPerms(player);
                    break;
                }
                show(player);
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
            tabComplete.add("show");
        }

        if ( commandSender.hasPermission("customscoreboard.reload")) tabComplete.add("reload");

        Collections.sort(tabComplete);
        return strings.length == 1 ? tabComplete : null;
    }

    private void reload(Player player) {
        Main.getPlugin().reloadConfig();
        Main.scoreboardData.setScoreboard(Main.getPlugin().getConfig().getStringList("scoreboard-lines"));
        Main.boards.clear();
        Bukkit.getScheduler().cancelTasks(Main.getPlugin());
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if ( onlinePlayer == null ) break;
            Main.createBoard(onlinePlayer);
        }
        player.sendMessage(ChatColor.DARK_GREEN + "[CustomScoreboard] " + ChatColor.GOLD + "Scoreboard reloaded successfully.");
    }

    private void hide(Player player) {
        Main.boards.stream().filter(customBoard -> customBoard.getPlayer().equals(player)).findFirst().ifPresent(CustomBoard::hideScoreboard);
        player.sendMessage(ChatColor.DARK_GREEN + "[CustomScoreboard] " + ChatColor.GOLD + "Scoreboard " + ChatColor.RED + "hidden" + ChatColor.GOLD + ". Type " + ChatColor.GREEN + "/scoreboard show " + ChatColor.GOLD + "to show the scoreboard again.");
    }

    private void show(Player player) {
        Main.boards.stream().filter(customBoard -> customBoard.getPlayer().equals(player)).findFirst().ifPresent(CustomBoard::showScoreboard);
        player.sendMessage(ChatColor.DARK_GREEN + "[CustomScoreboard] " + ChatColor.GOLD + "Scoreboard " + ChatColor.RED + "shown" + ChatColor.GOLD + ". Type " + ChatColor.GREEN + "/scoreboard hide " + ChatColor.GOLD + "to hide the scoreboard.");

    }

    private void help(Player player) {
        player.sendMessage(ChatColor.DARK_GREEN + "[CustomScoreboard] " + ChatColor.GOLD + "Help");
        player.sendMessage(ChatColor.GREEN + "/scoreboard help" + ChatColor.LIGHT_PURPLE + " - " + ChatColor.YELLOW + "Shows you this message with help.");
        player.sendMessage(ChatColor.GREEN + "/scoreboard info" + ChatColor.LIGHT_PURPLE + " - " + ChatColor.YELLOW + "Shows you information of this plugin.");
        if ( player.hasPermission("customscoreboard.hide"))
            player.sendMessage(ChatColor.GREEN + "/scoreboard hide/show" + ChatColor.LIGHT_PURPLE + " - " + ChatColor.YELLOW + "Hides or shows the scoreboard");
        if ( player.hasPermission("customscoreboard.reload"))
            player.sendMessage(ChatColor.GREEN + "/scoreboard reload" + ChatColor.LIGHT_PURPLE + " - " + ChatColor.YELLOW + "Reloads the plugin");
    }

    private void info(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_GREEN + "[CustomScoreboard] " + ChatColor.GOLD + "Allows you to set Custom Scoreboards on your server. " + ChatColor.RED + "Made by DarkDragon. " + ChatColor.GOLD + "Get it in " + ChatColor.BLUE + "" + ChatColor.UNDERLINE + "spigotmc.org/resources/custom-scoreboard.87154");
        player.sendMessage("");
    }

    private void noPerms(Player player) {
        player.sendMessage(ChatColor.RED + "[CustomScoreboard] You have no permissions to use this command.");
    }

    private void notFound(Player player) {
        player.sendMessage(ChatColor.RED + "[CustomScoreboard] This command does not exist, please use " + ChatColor.AQUA + "/scoreboard help" + ChatColor.RED + ".");
    }

}
