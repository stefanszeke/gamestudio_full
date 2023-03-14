package sk.tuke.gamestudio_frontend.games.Blackjack.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cards {
    String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
    String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King","Ace"};
//    String[] ranks = {"7","7","7","7","7","7","Ace","Ace","Ace","Ace","Ace","Ace","Ace"};
    List<Card> cards;

    public Cards() {
        cards = new ArrayList<>();
        resetPack();
    }

    public void resetPack() {
        cards.clear();
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        return cards.remove(0);
    }
}
