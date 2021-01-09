package dev.chechu.customscoreboard;

import dev.chechu.customscoreboard.events.ScoreboardListener;
import dev.chechu.customscoreboard.objects.Lines;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class CustomBoard {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private Player player;

    public CustomBoard(Player player) {
        this.player = Objects.requireNonNull(player, "player");
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("scoreboard","dummy", " ");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void setLines(String... args) {
        List<String> lines = Arrays.asList(args);
        for (String line : lines) {
            setLine(lines.size() - lines.indexOf(line), line);
        }

    }

    public void setLines(List<String> lines) {
        for (String line : lines) {
            setLine(lines.size() - lines.indexOf(line), line);
        }
    }

    public void setLine(int line, String arg) {
        String text = arg.replaceAll("&","§")
                .replaceAll("\\{online}",String.valueOf(ScoreboardListener.getOnlinePlayers()))
                .replaceAll("\\{x}", String.valueOf(player.getLocation().getBlockX()))
                .replaceAll("\\{y}", String.valueOf(player.getLocation().getBlockY()))
                .replaceAll("\\{z}", String.valueOf(player.getLocation().getBlockZ()))
                .replaceAll("\\{displayname}", player.getDisplayName())
                .replaceAll("\\{world}", player.getWorld().getName())
                .replaceAll("\\{maxplayers}", String.valueOf(Bukkit.getServer().getMaxPlayers()));


        if ( Main.isEconomyOn() ) text = text.replaceAll("\\{money}", String.valueOf(Math.round(Main.getEconomy().getBalance(player) * 100.0) / 100.0));
        if ( Main.isChatOn() ) text = text.replaceAll("\\{prefix}", Main.getChat().getPlayerPrefix(player)).replaceAll("\\{suffix}", Main.getChat().getPlayerSuffix(player));

        int randomTeam = (int) (Math.random() * ((99999 - 1)+1)) - 1;
        Team team = scoreboard.registerNewTeam("team" + randomTeam);
        team.addEntry(ChatColor.BLACK + StringUtils.repeat(" ",line) + ChatColor.WHITE);
        //onlineCounter.setPrefix("" + ChatColor.DARK_RED + Bukkit.getOnlinePlayers().size() + ChatColor.RED + "/" + ChatColor.DARK_RED + Bukkit.getMaxPlayers());
        team.setPrefix(text);
        objective.getScore(ChatColor.BLACK + StringUtils.repeat(" ",line) + ChatColor.WHITE).setScore(line-1);
    }
/*
    if ( Main.chatOn ) {
        team.setPrefix(text.replaceAll("\\{prefix}", Main.getChat().getPlayerPrefix(player)));
        team.setPrefix(text.replaceAll("\\{suffix}", Main.getChat().getPlayerSuffix(player)));
    } else {
        team.setPrefix(text.replaceAll("\\{prefix}", ChatColor.RED + "error"));
        team.setPrefix(text.replaceAll("\\{suffix}", ChatColor.RED + "error"));
    }*/
    public void setScoreboard() {
        player.setScoreboard(scoreboard);
    }

    public Player getPlayer() {
        return player;
    }
}
