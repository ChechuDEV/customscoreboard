package dev.chechu.customscoreboard;

import org.bukkit.ChatColor;

public class RandomColor {
    private ChatColor firstColor;
    private ChatColor secondColor;

    public RandomColor(ChatColor firstColor, ChatColor secondColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }

    public ChatColor getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(ChatColor firstColor) {
        this.firstColor = firstColor;
    }

    public ChatColor getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(ChatColor secondColor) {
        this.secondColor = secondColor;
    }
}
