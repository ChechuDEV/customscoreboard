package dev.chechu.customscoreboard;

import dev.chechu.customscoreboard.events.ScoreboardListener;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.*;

public class CustomBoard {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private Player player;
    private int taskID = 0;
    ArrayList<RandomColor> randomColors = new ArrayList<>();

    public CustomBoard(Player player) {
        this.player = Objects.requireNonNull(player, "player");
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("scoreboard","dummy", " ");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void setLines(String... args) {
        List<String> lines = Arrays.asList(args);
        for (int i = 0; i < lines.size(); i++) {
            setLine(lines.size() - i, lines.get(i));
        }
    }

    public void setLines(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            setLine(lines.size() - i, lines.get(i));
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


        if ( Main.scoreboardData.isEconomyOn() ) text = text.replaceAll("\\{money}", String.valueOf(Math.round(Main.scoreboardData.getEconomy().getBalance(player) * 100.0) / 100.0));
        if ( Main.scoreboardData.isChatOn() ) text = text.replaceAll("\\{prefix}", Main.scoreboardData.getChat().getPlayerPrefix(player)).replaceAll("\\{suffix}", Main.scoreboardData.getChat().getPlayerSuffix(player));
        if ( text.isEmpty() || text.equals("") || text.equals(" ")) text = StringUtils.repeat(" ", line);

        int randomTeam = (int) (Math.random() * ((99999 - 1)+1)) - 1;
        Team team = scoreboard.registerNewTeam("team" + randomTeam);
        RandomColor randomColor = getRandomColors();

        // TODO: SIZE AND FIX LINE NUMBER
        team.addEntry(randomColor.getFirstColor() + "" + randomColor.getSecondColor());
        team.setPrefix(text);
        objective.getScore(randomColor.getFirstColor() + "" + randomColor.getSecondColor()).setScore(line-1);
    }

    public void setTitle(String title) {
        objective.setDisplayName(title.replaceAll("&","§"));
    }

    public void setScoreboard() {
        player.setScoreboard(scoreboard);
    }

    public void startSchedule(int ticks) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if ( !player.isOnline() ) {
                    this.cancel();
                }
                setScoreboard();
                taskID = this.getTaskId();
            }
        }.runTaskTimer(Main.getPlugin(), 0, ticks);
    }

    public void stopSchedule() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public boolean hasSchedule() {
        return (taskID != 0);
    }

    public Player getPlayer() {
        return player;
    }

    private RandomColor getRandomColors() {
        String colors = "1234567890abcdef";
        RandomColor selectedColors;
        do {
            ChatColor color1;
            ChatColor color2;
            int color1num = new Random().nextInt(colors.length() - 1) + 1;
            int color2num = new Random().nextInt(colors.length() - 1) + 1;
            color1 = ChatColor.getByChar(colors.charAt(color1num));
            color2 = ChatColor.getByChar(colors.charAt(color2num));

           selectedColors = new RandomColor(color1, color2);
        } while ( randomColors.contains(selectedColors));


        if ( !randomColors.contains(selectedColors) ) {
            randomColors.add(selectedColors);
            return selectedColors;
        } else {
            Main.getPlugin().getLogger().severe("There was an error randomizing teams, Scoreboard may not show properly.");
            return new RandomColor(ChatColor.DARK_PURPLE, ChatColor.YELLOW);
        }
    }
}
