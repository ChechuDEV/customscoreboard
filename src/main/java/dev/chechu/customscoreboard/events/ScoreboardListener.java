package dev.chechu.customscoreboard.events;

import dev.chechu.customscoreboard.Main;
import dev.chechu.customscoreboard.CustomBoard;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScoreboardListener implements Listener {
    private Plugin plugin;
    private List<CustomBoard> boards = new ArrayList<>();
    private static int playerCount;

    public ScoreboardListener(Main main) {
        plugin = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerCount = Bukkit.getOnlinePlayers().size();

        if ( Main.updateOnJoin() )
            if ( !boards.isEmpty() ){
                for (CustomBoard customBoard : boards) {
                    customBoard.setLines(Main.getScoreboardLines());
                    customBoard.setScoreboard();
                }
            }

        CustomBoard board = new CustomBoard(event.getPlayer());
        board.setLines(Main.getScoreboardLines());
        board.setScoreboard();
        boards.add(board);
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        playerCount = playerCount - 1;
        boards.stream().filter(customBoard -> customBoard.getPlayer().equals(event.getPlayer())).findFirst().ifPresent(playerQuitBoard -> boards.remove(playerQuitBoard));
        if ( Main.updateOnJoin() )
            if ( !boards.isEmpty() ){
                for (CustomBoard customBoard : boards) {
                    customBoard.setLines(Main.getScoreboardLines());
                    customBoard.setScoreboard();
                }
            }
    }

    public static int getOnlinePlayers() {
        return playerCount;
    }
}
