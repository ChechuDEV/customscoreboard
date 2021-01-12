package dev.chechu.customscoreboard.events;

import dev.chechu.customscoreboard.CustomBoard;
import dev.chechu.customscoreboard.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScoreboardListener implements Listener {
    private List<CustomBoard> boards = new ArrayList<>();
    private static int playerCount;

    public ScoreboardListener() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerCount = Bukkit.getOnlinePlayers().size();

        if ( Main.scoreboardData.hasMembersTag() )
            if ( !boards.isEmpty() ){
                for (CustomBoard customBoard : boards) {
                    customBoard.setLines(Main.scoreboardData.getScoreboard());
                    customBoard.setScoreboard();
                }
            }

        CustomBoard board = new CustomBoard(event.getPlayer());
        board.setLines(Main.scoreboardData.getScoreboard());
        board.setTitle(Objects.requireNonNull(Main.getPlugin().getConfig().getString("scoreboard-title"), "title"));
        if ( Main.scoreboardData.hasMoneyTag() && !Main.scoreboardData.hasXyzTag() ) {
            board.startSchedule(100);
        } else if ( Main.scoreboardData.hasXyzTag() ) {
            board.startSchedule(10);
        }
        board.setScoreboard();
        boards.add(board);
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        playerCount = playerCount - 1;
        boards.stream().filter(customBoard -> customBoard.getPlayer().equals(event.getPlayer())).findFirst().ifPresent(playerQuitBoard -> {
            if ( playerQuitBoard.hasSchedule() ) playerQuitBoard.stopSchedule();
            boards.remove(playerQuitBoard);
        });
        if ( Main.scoreboardData.hasMembersTag() )
            if ( !boards.isEmpty() ){
                for (CustomBoard customBoard : boards) {
                    customBoard.setLines(Main.scoreboardData.getScoreboard());
                    customBoard.setScoreboard();
                }
            }
    }

    public static int getOnlinePlayers() {
        return playerCount;
    }
}
