package dev.chechu.customscoreboard.events;

import dev.chechu.customscoreboard.CustomBoard;
import dev.chechu.customscoreboard.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {
    private static int playerCount;

    public ScoreboardListener() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerCount = Bukkit.getOnlinePlayers().size();

        if ( Main.scoreboardData.hasMembersTag() )
            if ( !Main.boards.isEmpty() ){
                for (CustomBoard customBoard : Main.boards) {
                    customBoard.updateLines(Main.scoreboardData.getScoreboard());
                    customBoard.setScoreboard();
                }
            }

        Main.createBoard(event.getPlayer());
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        playerCount = playerCount - 1;
        Main.boards.stream().filter(customBoard -> customBoard.getPlayer().equals(event.getPlayer())).findFirst().ifPresent(playerQuitBoard -> {
            playerQuitBoard.stopSchedule();
            Main.boards.remove(playerQuitBoard);
        });
        if ( Main.scoreboardData.hasMembersTag() )
            if ( !Main.boards.isEmpty() ){
                for (CustomBoard customBoard : Main.boards) {
                    customBoard.updateLines(Main.scoreboardData.getScoreboard());
                    customBoard.setScoreboard();
                }
            }
    }

    public static int getOnlinePlayers() {
        return playerCount;
    }
}
