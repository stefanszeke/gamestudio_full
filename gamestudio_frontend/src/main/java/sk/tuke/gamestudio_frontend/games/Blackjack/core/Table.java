package sk.tuke.gamestudio_frontend.games.Blackjack.core;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;

public class Table implements GameField {
    private Player player;
    private Player dealer;
    private Cards cards;

    public Table() {
        player = new Player();
        dealer = new Player();
        cards = new Cards();
        cards.shuffle();
    }

    public void firstDeal() {
        player.addCard(cards.deal());
        dealer.addCard(cards.deal());
        player.addCard(cards.deal());
        dealer.addCard(cards.deal());
    }

    public void hit(Player p) {
        p.addCard(cards.deal());
    }

    public boolean isBust(Player p) {
        return p.getTotal() > 21;
    }

    public boolean isBlackJack(Player p) {
        return p.getTotal() == 21;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getDealer() {
        return dealer;
    }

    public void tableReset() {
        player.clearHand();
        dealer.clearHand();
        cards.resetPack();
        cards.shuffle();
    }
}
