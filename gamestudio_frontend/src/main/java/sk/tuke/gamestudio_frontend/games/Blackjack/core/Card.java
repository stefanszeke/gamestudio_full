package sk.tuke.gamestudio_frontend.games.Blackjack.core;

public class Card {
    private String suit;
    private String rank;
    private int value;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        setValue(rank);
    }

    private void setValue(String rank) {
        if (rank.equals("Ace")) {
            value = 11;
        } else if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King")) {
            value = 10;
        } else {
            value = Integer.parseInt(rank);
        }
    }

    @Override
    public String toString() {
        return String.format("[%s of %s] = %d", rank, suit, value);
    }

    public int getValue() {
        return value;
    }


    public String getRank() {
        return rank;
    }

    public String convertAce() {
        if (rank.equals("Ace") && value == 11) {
            value = 1;
        }
        return rank;
    }
}
