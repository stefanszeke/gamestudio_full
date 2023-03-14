package sk.tuke.gamestudio_frontend.games.Blackjack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio_frontend.games.Blackjack.core.Table;
import sk.tuke.gamestudio_frontend.games.interfaces.Game;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;

@Component
public class Blackjack implements Game {

    @Autowired
    @Qualifier("blackJackUI")
    private UserInterface userInterface;

    private Blackjack() {}

    public void run() {
        userInterface.newGameStarted(new Table());
    }
}
