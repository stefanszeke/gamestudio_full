package sk.tuke.gamestudio_frontend.games.Tictactoe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;
import sk.tuke.gamestudio_frontend.games.Tictactoe.core.Grid;
import sk.tuke.gamestudio_frontend.games.interfaces.Game;

@Component
public class Tictactoe implements Game {

    @Autowired
    @Qualifier("tictactoeUI")
    private UserInterface userInterface;

    private Tictactoe() {}

    public void run() {
        userInterface.newGameStarted(new Grid());
    }
}
