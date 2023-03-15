package sk.tuke.gamestudio_frontend.games.Puzzle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio_frontend.games.Puzzle.Core.PuzzleField;
import sk.tuke.gamestudio_frontend.games.Puzzle.Settings.PuzzleSettings;
import sk.tuke.gamestudio_frontend.games.interfaces.Game;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;

import java.util.Scanner;

@Component
public class Puzzle implements Game {

    @Autowired
    @Qualifier("puzzleUI")
    private UserInterface userInterface;

    private Scanner input;
    private PuzzleSettings puzzleSettings;

    private boolean newGame;
    private boolean exitRequest;

    private Puzzle() {}

    public void run() {
        try {
            input = new Scanner(System.in);
            this.newGame = false;
            this.exitRequest = false;
            puzzleSettings = PuzzleSettings.load();

            while (!newGame && !exitRequest) {

                mainMenu();

                if (exitRequest) {
                    System.out.println("Exiting ...");
                    return;
                } else if (newGame) {
                    System.out.println("Starting new game ...");
                    start();
                    newGame = false;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void start() {
        userInterface.newGameStarted(new PuzzleField(puzzleSettings.getRowCount(),puzzleSettings.getColumnCount(),puzzleSettings.getShuffleCount()));
    }

    // settings & main menu
    private void mainMenu() throws Exception {
        System.out.println("\nWelcome to Puzzle, Please select:");
        System.out.println(" 1. new game");
        System.out.println(" 2. settings");
        System.out.println(" 3. exit");
        String line = input.nextLine();
        switch(line) {
            case "1" -> newGame = true;
            case "2" -> setSettings();
            case "3" -> exitRequest = true;
            default -> System.out.println("Invalid input");
        }
    }
    private void setSettings() throws Exception {
        System.out.println("Please select difficulty");
        System.out.println(" 1. very easy [3x3]");
        System.out.println(" 2. easy [4x3]");
        System.out.println(" 3. medium [4x4]");
        System.out.println(" 4. hard [5x4]");
        System.out.println(" 5. very hard [5x5]");
        System.out.println(" x. exit");
        System.out.println();
        System.out.println("Harder difficulty gets more shuffles.");

        String line = input.nextLine();

        switch (line) {
            case "1" -> {
                System.out.println("Setting to very easy");
                PuzzleSettings.VERY_EASY.save();
            }
            case "2" -> {
                System.out.println("Setting to easy");
                PuzzleSettings.EASY.save();
            }
            case "3" -> {
                System.out.println("Setting to medium");
                PuzzleSettings.MEDIUM.save();
            }
            case "4" -> {
                System.out.println("Setting to hard");
                PuzzleSettings.HARD.save();
            }
            case "5" -> {
                System.out.println("Setting to very hard");
                PuzzleSettings.VERY_HARD.save();
            }
            case "x" -> System.out.println("exiting ...");
            default -> System.out.println("Invalid input");
        }
        puzzleSettings = PuzzleSettings.load();
    }


}
