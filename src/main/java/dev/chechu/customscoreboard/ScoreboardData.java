package dev.chechu.customscoreboard;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

import java.util.List;

public class ScoreboardData {
    private boolean moneyTag;
    private boolean xyzTag;
    private boolean membersTag;
    private List<String> scoreboard;
    private Economy economy;
    private Chat chat;

    public ScoreboardData(boolean moneyTag, boolean xyzTag, boolean membersTag, List<String> scoreboard, Economy economy, Chat chat) {
        this.moneyTag = moneyTag;
        this.xyzTag = xyzTag;
        this.membersTag = membersTag;
        this.scoreboard = scoreboard;
        this.economy = economy;
        this.chat = chat;
    }

    public ScoreboardData() {
        this.moneyTag = false;
        this.xyzTag = false;
        this.membersTag = false;
        this.scoreboard = null;
        this.economy = null;
        this.chat = null;
    }

    public boolean hasMoneyTag() {
        return moneyTag;
    }

    public void setMoneyTag(boolean moneyTag) {
        this.moneyTag = moneyTag;
    }

    public boolean hasXyzTag() {
        return xyzTag;
    }

    public void setXyzTag(boolean xyzTag) {
        this.xyzTag = xyzTag;
    }

    public boolean hasMembersTag() {
        return membersTag;
    }

    public void setMembersTag(boolean membersTag) {
        this.membersTag = membersTag;
    }

    public List<String> getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(List<String> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public Economy getEconomy() {
        return economy;
    }

    public void setEconomy(Economy economy) {
        this.economy = economy;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public boolean isChatOn() {
        return chat != null;
    }

    public boolean isEconomyOn() {
        return economy != null;
    }
}
