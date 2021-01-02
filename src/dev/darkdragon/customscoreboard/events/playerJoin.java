package dev.darkdragon.customscoreboard.events;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.darkdragon.customscoreboard.Main;
import dev.darkdragon.customscoreboard.Objects.PlayerScoreboardsObj;
import dev.darkdragon.customscoreboard.Objects.ScoreboardObj;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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

public class playerJoin implements Listener {
    private static Plugin plugin;

    public static List<PlayerScoreboardsObj> playerScoreboardsObjs = new ArrayList<>();

    public playerJoin(Main main) {
        plugin = main;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        showScoreboard(player);
    }

    public static void showScoreboard(Player player ) {
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean set = playerScoreboardsObjs.stream().anyMatch(playerScoreboardsObj -> playerScoreboardsObj.getPlayer() == player);
                boolean updatable = false;
                PlayerScoreboardsObj playerObj = null;
                if (set) {
                    updatable = playerScoreboardsObjs.stream().anyMatch(playerScoreboardsObj -> playerScoreboardsObj.getPlayer() == player && playerScoreboardsObj.isUpdatable());
                    playerObj = playerScoreboardsObjs.stream().filter(playerScoreboardsObj -> player.equals(playerScoreboardsObj.getPlayer())).findFirst().orElse(null);
                }
                List<String> newLines = new ArrayList<>();
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

                Objective obj = scoreboard.registerNewObjective("scoreboard","dummy", plugin.getConfig().getString("scoreboard-title").replaceAll("&","§"));
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                AtomicBoolean isUpdatable = new AtomicBoolean(false);
                AtomicBoolean update = new AtomicBoolean(false);
                try {
                    Reader reader = Files.newBufferedReader(Paths.get(plugin.getDataFolder().toString(),"Scoreboards", plugin.getConfig().getString("scoreboard-file") + ".json"));

                    List<ScoreboardObj> scoreboardObjList = new Gson().fromJson(reader, new TypeToken<List<ScoreboardObj>>() {}.getType());

                    if (scoreboardObjList.size() == 16) sendConsole(ChatColor.RED + "Max lines of 16 reached");
                    boolean finalUpdatable = updatable;
                    PlayerScoreboardsObj finalPlayerObj = playerObj;
                    scoreboardObjList.forEach(scoreboardObj -> {
                        if (scoreboardObj.getText().equals("")) {
                            String text = StringUtils.repeat(" ",scoreboardObjList.indexOf(scoreboardObj));
                            obj.getScore(text).setScore(scoreboardObjList.size() - scoreboardObjList.indexOf(scoreboardObj) - 1);
                            newLines.add(text);

                        } else {
                            String text = scoreboardObj.getText();
                            int maxSize = plugin.getConfig().getInt("scoreboard-size");

                            String line = getLine(text, scoreboardObj.getAlign(), maxSize, player);
                            newLines.add(line);
                            if(text.contains("{money}") || text.contains("{onlineplayers}") || text.contains("{maxplayers}") || text.contains("{X}") || text.contains("{Y}") || text.contains("{Z}")) {
                                isUpdatable.set(true);
                            }
                            if(finalUpdatable) { if(!line.equals(finalPlayerObj.getLines().get(scoreboardObjList.indexOf(scoreboardObj))))
                                update.set(true);
                            }
                            obj.getScore(line).setScore(scoreboardObjList.size() - scoreboardObjList.indexOf(scoreboardObj) - 1);

                        }
                    });

                    if (set && updatable && update.get()) player.setScoreboard(scoreboard);
                    if (!set) player.setScoreboard(scoreboard);
                    reader.close();
                    if(!set) playerScoreboardsObjs.add(new PlayerScoreboardsObj(player, isUpdatable.get(), newLines, getTaskId(), scoreboard));
                    updatable = isUpdatable.get();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                if ( !updatable ) {
                    cancel();
                }

                if (!player.isOnline()){
                    playerScoreboardsObjs.remove(playerObj);
                    cancel();
                }
            }
        }.runTaskTimer(plugin,0L, 20*plugin.getConfig().getLong("scoreboard-refresh-rate"));
    }

    private static String getLine(String unformattedText, String align, int maxSize, Player player) {
        StringBuilder stringBuilder = new StringBuilder();
        String text = unformattedText
                .replaceAll("\\{onlineplayers}", String.valueOf(plugin.getServer().getOnlinePlayers().size()))
                .replaceAll("\\{maxplayers}", String.valueOf(plugin.getServer().getMaxPlayers()))
                .replaceAll("\\{displayname}", player.getDisplayName() )
                .replaceAll("\\{X}", String.valueOf(player.getLocation().getX()))
                .replaceAll("\\{Y}", String.valueOf(player.getLocation().getY()))
                .replaceAll("\\{Z}", String.valueOf(player.getLocation().getZ()));
        if ( Main.isEconomyOn() ) {
            text = text.replaceAll("\\{money}",String.valueOf(Math.round(Main.getEconomy().getBalance(player) * 100.0) / 100.0));
        } else {
            text = text.replaceAll("\\{money}", "§4error");
        }


        text = text.replaceAll("&","§");
        String countedLetters = text.replaceAll("§([0-9a-fk-or])","");
        int letters = countedLetters.length();
        int spaces = maxSize - letters;
        if (spaces < 0) {
            sendConsole(ChatColor.RED + "Line is larger than maximum size!");
            String line = StringUtils.substring(text,0,maxSize);
            return ChatColor.RED + line;

        } else if(align.equals("left")) {
            stringBuilder.append(text);
            stringBuilder.append(StringUtils.repeat(" ",spaces));
        } else if (align.equals("center")) {
            if (spaces % 2 == 0){
                stringBuilder.append(StringUtils.repeat(" ", spaces/2));
                stringBuilder.append(text);
                stringBuilder.append(StringUtils.repeat(" ", spaces/2));
            } else {
                stringBuilder.append(StringUtils.repeat(" ", (spaces - 1) / 2));
                stringBuilder.append(text);
                stringBuilder.append(StringUtils.repeat(" ", (spaces - 1)/12));
            }
        } else if (align.equals("right")) {
            stringBuilder.append(StringUtils.repeat(" ", spaces));
            stringBuilder.append(text);
        }
        return stringBuilder.toString();
    }

    private static void sendConsole(String message) {
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CustomScoreboards] "+ ChatColor.AQUA + message);
    }
}
