package sk.tuke.gamestudio_frontend.games.Jewels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio_frontend.games.Jewels.core.JewelField;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;
import sk.tuke.gamestudio_frontend.games.interfaces.Game;

@Component
public class Jewels implements Game {

    @Autowired
    @Qualifier("jewelUI")
    private UserInterface userInterface;

    private Jewels() {}

    public void run() {
        userInterface.newGameStarted(new JewelField(5, 15));
    }
}
