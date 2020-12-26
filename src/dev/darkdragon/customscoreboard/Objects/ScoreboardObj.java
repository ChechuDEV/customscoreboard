package dev.darkdragon.customscoreboard.Objects;

public class ScoreboardObj {
    @Override
    public String toString() {
        return "{" +
                "text='" + text + '\'' +
                ", align='" + align + '\'' +
                '}';
    }

    private String text;
    private String align;

    public ScoreboardObj(String text, String align) {
        this.text = text;
        this.align = align;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
