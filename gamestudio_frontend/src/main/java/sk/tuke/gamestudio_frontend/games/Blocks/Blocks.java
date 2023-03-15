package sk.tuke.gamestudio_frontend.games.Blocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio_frontend.games.Blocks.Core.BlocksField;
import sk.tuke.gamestudio_frontend.games.interfaces.Game;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;


@Component
public class Blocks implements Game {

    @Autowired
    @Qualifier("blocksConsoleUI")
    private UserInterface userInterface;

    public Blocks() {}

    public void run() {
        userInterface.newGameStarted(new BlocksField(5,15));
    }
}
