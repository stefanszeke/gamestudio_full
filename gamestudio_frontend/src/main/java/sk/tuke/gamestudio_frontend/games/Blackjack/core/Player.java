package sk.tuke.gamestudio_frontend.games.Blackjack.core;

import java.util.ArrayList;
import java.util.List;

public class Player {
    List<Card> cards;
    int total = 0;
    int unconvertedAce = 0;

    public Player() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
        if (card.getRank().equals("Ace")) unconvertedAce++;
        setTotal();
    }

    public void setTotal() {
        int count = 0;
        for(Card c: cards) {
            count += c.getValue();
        }
        if(unconvertedAce > 0 && count > 21) {
            convertAce();
            setTotal();
        } else {
            this.total = count;
        }
    }

    public int getTotal() {
        return this.total;
    }

    public void clearHand() {
        cards.clear();
        total = 0;
        unconvertedAce = 0;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void convertAce() {
        for (Card c : cards) {
            if (c.getRank().equals("Ace") && c.getValue() == 11) {
                c.convertAce();
                unconvertedAce--;
                break;
            }
        }
    }
}
