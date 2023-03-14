package sk.tuke.gamestudio_frontend.games.Blocks.Core;

public class Block {

    public enum Color {
        RED, GREEN, BLUE, YELLOW
    }

    private Color color;

    public Block(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


}
