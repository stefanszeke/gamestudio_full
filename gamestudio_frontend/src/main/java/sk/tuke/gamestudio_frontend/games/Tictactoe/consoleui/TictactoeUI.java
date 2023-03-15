package sk.tuke.gamestudio_frontend.games.Tictactoe.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;

import sk.tuke.gamestudio_frontend.games.Tictactoe.core.Grid;

import sk.tuke.gamestudio_library.entity.Score;
import sk.tuke.gamestudio_library.interfaces.ScoreService;
import sk.tuke.gamestudio_library.exceptions.ScoreException;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@ComponentScan("sk.tuke.gamestudio")
public class TictactoeUI implements UserInterface {

    @Autowired
    private ScoreService scoreService;

    private Grid grid;
    private Boolean exitRequested = false;

    private int moves;
    private boolean playerWon = false;

    private Scanner scanner = new Scanner(System.in);
    private Pattern inputPattern;

    // main loop
    public void newGameStarted(GameField grid) {
        this.grid = (Grid) grid;
        inputPattern = Pattern.compile("([a-c])([1-3])|x");
        moves = 0;
        playerWon = false;

        mainLoop();
    }

    private void mainLoop() {
        Instant start = Instant.now();

        do {
            update();
            processInput();
            moves++;
            if(isEndGame("X")) {
                playerWon = true;
                break;
            }
            AIInput();
            if (isEndGame("O")) break;
        } while (!grid.getEndGame() && !exitRequested);

        Instant end = Instant.now();
        Duration time = Duration.between(start, end);

        if (playerWon) {
            showAndSaveScore(moves, time);
        }

        if (!exitRequested) {
            printScore();
            askReplay();
        }
    }

    private void processInput() {
        System.out.println("Enter your move: (A2,B1,C3), x to exit");
        String userInput = scanner.nextLine();
        Boolean validInput = false;

        while(!validInput) {
            try {
                validateInput(userInput);
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                userInput = scanner.nextLine();
            }
        }

        if(exitRequested) return;
        Integer index = grid.getTilesMap().get(userInput.toLowerCase());
        grid.setTile(index, "X");
    }

    public void validateInput(String userInput) {
        Matcher matcher = inputPattern.matcher(userInput.toLowerCase());

        boolean rowInBounds = false;
        boolean colInBounds = false;

        if(matcher.matches()) {
            if(userInput.equalsIgnoreCase("x")) {
                System.out.println("Exiting game...");
                exitRequested = true;
            } else {
                String rowInput = matcher.group(1);
                String colInput = matcher.group(2);

                if(rowInput.equals("a") || rowInput.equals("b") || rowInput.equals("c")) {
                    rowInBounds = true;
                }
                if(colInput.equals("1") || colInput.equals("2") || colInput.equals("3")) {
                    colInBounds = true;
                }
                if(!rowInBounds) throw new InputMismatchException("Row out of bounds! rows: A, B, C");
                if(!colInBounds) throw new InputMismatchException("Column out of bounds! columns: 1, 2, 3");
                if(!grid.getTile(grid.getTilesMap().get(rowInput+colInput)).getMark().equals(".")) throw new InputMismatchException("Tile already taken!");
            }
        }
        if(!matcher.matches()) throw new InputMismatchException("Wrong input format! Please use the following format: A1, B2, C3");

    }

    public void update() {
        int j = 1;
        System.out.printf("%4s1  2  3 %n", " ");
        for(int row = 0; row < 3; row++) {
            System.out.printf("%2s", (char) (row+65));
            System.out.printf("%1s", " ");
            for(int col = 0; col < 3; col++) {
                System.out.printf("%2s", grid.getTile(row+col+j-1).getMark());
                if(col < 2) System.out.print("|");
            }
            j+=2;
            System.out.println();
        }
    }

    private void AIInput() {
        int index = (int) (Math.random() * 9);
        while("X".equals(grid.getTile(index).getMark()) || "O".equals(grid.getTile(index).getMark()) ) {
            index = (int) (Math.random() * 9);
        }
        grid.setTile(index, "O");
    }

    private boolean isEndGame(String player) {
        if(grid.iskWin()) {
            update();
            if(player.equals("X")) System.out.println("[******* You Win! *******]");
            else System.out.println("[******* You lose! *******]");
            grid.setEndGame(true);
            return true;
        }
        if(grid.iskDraw()) {
            update();
            System.out.println("Draw!");
            grid.setEndGame(true);
            return true;
        }
        return false;
    }

    private void printScore() {
        try {
            List<Score> scores = scoreService.getTopScores("Tictactoe");
            System.out.println("Top scores:");
            for (Score score : scores) {
                System.out.println(score);
            }
        } catch (ScoreException e) {
            System.out.println("Error loading scores");
        }
    }

    private void askReplay() {
        System.out.println("Do you want to play again? (y/n)");
        String userInput = scanner.nextLine();
        if(userInput.equalsIgnoreCase("y")) {
            newGameStarted(new Grid());
        } else {
            System.out.println("Exiting game...");
        }
    }

    private void showAndSaveScore(int moves, Duration time) {
        try {
            String timeFormat = new SimpleDateFormat("HH:mm:ss").format(time.toMillis()-3600000);
            System.out.printf("Your time: %s, moves: %d%n", timeFormat, moves);

            int timeScore = ((int) time.toMillis())/2000;
            int score = 130-(10*(moves)) - timeScore;
            System.out.println("Your score: " + score);
            Score newScore = new Score("Tictactoe", "user", score, new Timestamp(System.currentTimeMillis()));
            scoreService.addScore(newScore);
        } catch (ScoreException e) {
            System.out.println("Error saving score");
        }
    }


}

