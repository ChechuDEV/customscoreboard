package dev.darkdragon.customscoreboard.Objects;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class PlayerScoreboardsObj {
    private Player player;
    private boolean updatable;
    private List<String> lines;
    private int ID;
    private Scoreboard scoreboard;



    public PlayerScoreboardsObj(Player player, boolean updatable, List<String> lines, int ID, Scoreboard scoreboard) {
        this.player = player;
        this.updatable = updatable;
        this.lines = lines;
        this.ID = ID;
        this.scoreboard = scoreboard;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public Player getPlayer() {
        return player;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
