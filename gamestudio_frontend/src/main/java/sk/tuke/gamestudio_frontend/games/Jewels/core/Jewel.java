package sk.tuke.gamestudio_frontend.games.Jewels.core;

public class Jewel {

    public enum Color {
        RED, GREEN, BLUE, YELLOW, COUNT
    }

    private Color color;

    public Jewel(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
