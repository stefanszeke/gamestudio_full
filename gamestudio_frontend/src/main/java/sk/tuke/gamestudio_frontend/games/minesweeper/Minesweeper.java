package sk.tuke.gamestudio_frontend.games.minesweeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio_frontend.games.interfaces.Game;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;
import sk.tuke.gamestudio_frontend.games.minesweeper.core.MinesField;
import sk.tuke.gamestudio_frontend.games.minesweeper.settings.MinesweeperSettings;

import java.util.Scanner;


@Component
public class Minesweeper implements Game {

    @Autowired
    @Qualifier("consoleUI")
    private UserInterface userInterface;

    @Autowired
    private MinesweeperSettings minesweeperSettings;

    private Scanner scanner = new Scanner(System.in);

    private boolean exitRequest = false;
    private boolean newGame = false;

    public Minesweeper() {}

    public void run() {
        try {

            exitRequest = false;
            newGame = false;

            String userName = System.getProperty("user.name");
            System.out.println("\nWelcome " + userName);

            while (!newGame && !exitRequest) {
                mainMenu();
            }

            if (!exitRequest) {
                minesweeperSettings = MinesweeperSettings.load();

                MinesField minesField = new MinesField(minesweeperSettings.getRowCount(), minesweeperSettings.getColumnCount(), minesweeperSettings.getMineCount());
                userInterface.newGameStarted(minesField);
            }

            if (!exitRequest) {
                askNewGame();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void mainMenu() throws Exception {
        System.out.println("Welcome to Minesweeper, Please select");
        System.out.println(" 1. new game");
        System.out.println(" 2. settings");
        System.out.println(" 3. exit");
        String line = scanner.nextLine();
        switch(line) {
            case "1" -> newGame = true;
            case "2" -> setSettings();
            case "3" -> exitRequest = true;
            default -> System.out.println("Invalid input");
        }
    }

    private void setSettings() throws Exception {
        System.out.println("Please select difficulty");
        System.out.println(" 1. easy");
        System.out.println(" 2. medium");
        System.out.println(" 3. hard");
        System.out.println(" t. test (2 mines only)");
        System.out.println(" x. exit");

        String line = scanner.nextLine();

        switch (line) {
            case "1" -> {
                System.out.println("Setting to easy");
                MinesweeperSettings.EASY.save();
            }
            case "2" -> {
                System.out.println("Setting to medium");
                MinesweeperSettings.MEDIUM.save();
            }
            case "3" -> {
                System.out.println("Setting to hard");
                MinesweeperSettings.HARD.save();
            }
            case "t" -> {
                System.out.println("Setting to test");
                MinesweeperSettings.TEST.save();
            }
            case "x" -> System.out.println("exiting ...");
            default -> System.out.println("Invalid input");
        }
    }

    private void askNewGame() {
        System.out.println("\nNew Game ? (y/n)");
        String line = scanner.nextLine();
        if (line.equals("y")) {
            try {
                run();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("exiting ...");
        }
    }


}
