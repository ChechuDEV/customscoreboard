package dev.darkdragon.customscoreboard.Objects;

import org.bukkit.entity.Player;

import java.util.List;

public class PlayerScoreboardsObj {
    private Player player;
    private boolean updatable;
    private List<String> lines;
    public PlayerScoreboardsObj(Player player, boolean updatable, List<String> lines) {
        this.player = player;
        this.updatable = updatable;
        this.lines = lines;
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
}
