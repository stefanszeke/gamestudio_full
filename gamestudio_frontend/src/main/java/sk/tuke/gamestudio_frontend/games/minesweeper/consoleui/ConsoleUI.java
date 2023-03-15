package sk.tuke.gamestudio_frontend.games.minesweeper.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;



import sk.tuke.gamestudio_frontend.games.minesweeper.core.Clue;
import sk.tuke.gamestudio_frontend.games.minesweeper.core.MinesField;
import sk.tuke.gamestudio_frontend.games.minesweeper.core.GameState;
import sk.tuke.gamestudio_frontend.games.minesweeper.core.Tile;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;

import sk.tuke.gamestudio_library.entity.Score;
import sk.tuke.gamestudio_library.interfaces.ScoreService;
import sk.tuke.gamestudio_library.exceptions.ScoreException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Console user interface.
 */
@Component
@ComponentScan("sk.tuke.gamestudio")
public class ConsoleUI implements UserInterface {

    @Autowired
    private ScoreService scoreService;

    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private MinesField minesField;
    private boolean exitRequest = false;

    /**
     * Reads line of text from the reader.
     * @return line as a string
     */
    private String readLine() {
        try {
            return input.readLine().toLowerCase();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Starts the game.
     * @param minesField field of mines and clues
     */
    @Override
    public void newGameStarted(GameField minesField) {
        this.minesField = (MinesField) minesField;
        gameLoop();
    }

    private void gameLoop() {
        boolean solved = false;
        boolean failed = false;

        Instant start = Instant.now();

        do {
            update();
            processInput();

            solved = minesField.isSolved();
            failed = minesField.getState() == GameState.FAILED;

            if(failed) printLose();

            if(solved) printWin();

        } while(!solved && ! failed && !exitRequest);

        Instant end = Instant.now();
        Duration time = Duration.between(start, end);

        showAndPostScore(time, solved);
    }
    
    /**
     * Updates user interface - prints the field.
     */
    @Override
    public void update() {

        int numberOfTiles = minesField.getRowCount()* minesField.getColumnCount();
        System.out.println();
        System.out.printf("opened: %d/%d | marked: %d/%d %n%n" , minesField.getNumberOf(Tile.State.OPEN),numberOfTiles- minesField.getMineCount(), minesField.getNumberOf(Tile.State.MARKED), minesField.getMineCount());

        // print top numbers
        System.out.printf("%4s"," ");
        for(int i = 0; i < minesField.getColumnCount(); i++) System.out.printf("%3s",i);
        System.out.println();

        // print rows
        for(int i = 0; i < minesField.getRowCount(); i++) {
            System.out.println();
            System.out.printf((char)(65+i) + "%3s"," "); // print letter
            for(int j = 0; j < minesField.getColumnCount(); j++) {
                Tile tile = minesField.getTile(i, j);
                printTile(tile);
//                printTileUncovered(tile); // debug

            }
        }
        // print keys
        System.out.printf("%n%nPlease enter: <OC2>:open | <MA1>:mark | <X>:exit %n");
    }

    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */

    // [xX]|[oOmM][a-zA-Z][0-9]
    private void processInput() {
        System.out.println("");

        String userInput = readLine();
        boolean valid = false;

        while(!valid) {
            try {
                handleInput(userInput);
                valid = true;
            } catch (WrongFormatException e) {
                System.out.println(e.getMessage());
                userInput = readLine();
            }
        }

        Pattern p = Pattern.compile("x|([om])([a-z])([\\d]+)");
        Matcher m = p.matcher(userInput);

        if(m.matches() && !m.group(0).equals("x")) {
            String action = m.group(1);
            int row = Integer.parseInt(m.group(3));
            int column = (char) m.group(2).charAt(0) - 97;
            System.out.println("coo: " + column);

            if(action.equals("o")) minesField.openTile(column, row);

            else if (action.equals("m")) {
                if(minesField.getNumberOf(Tile.State.MARKED) < minesField.getMineCount()) {
                    minesField.markTile(column, row);
                } else {
                    System.out.println("Marked limit reached!");
                }
            };
        }
    }

    private void handleInput(String input) throws WrongFormatException {
        int maxRow = (97+ minesField.getRowCount());
        boolean rowInBounds = false;
        boolean colInBounds = false;
//
        Pattern p = Pattern.compile("x|([om])([a-z])(\\d+)");
        Matcher m = p.matcher(input);

        if (m.matches()) {
            if(m.group(0).equals("x")) {
                System.out.println("exiting ...");
//                System.exit(0);
                exitRequest = true;
                return;
            }

            int colNumb = Integer.parseInt(m.group(3));
            int rowNumb = m.group(2).charAt(0);

            if(colNumb < minesField.getColumnCount()) colInBounds = true;
            if(rowNumb < maxRow) rowInBounds = true;
        }

        if (!m.matches()) throw new WrongFormatException("from exception: Wrong input pattern, please try again.");
        if (!colInBounds) throw new WrongFormatException("from exception: Column Out of bounds");
        if (!rowInBounds) throw new WrongFormatException("from exception: Row Out of bounds");
    }

    private void printTile(Tile tile) {
        if(tile.getState() == Tile.State.CLOSED) System.out.printf("%3s","-");
        else if(tile.getState() == Tile.State.MARKED) System.out.printf("%3s","X");
        else if(tile.getState() == Tile.State.OPEN) {
            if(tile instanceof Clue) System.out.printf(("%3s"), ((Clue)tile).getValue());
            else System.out.printf("%3s","M");
        }
    }

    private void printTileUncovered(Tile tile) {
            if(tile instanceof Clue) System.out.print(((Clue) tile).getValue() + " ");
            else System.out.print("M ");
    }

    private void printLose() {
        update();
        System.out.println("********************");
        System.out.println("****** BOOOM *******");
        System.out.println("********************");
    }
    private void printWin() {
        update();
        System.out.println("********************");
        System.out.println("***** Very cool ****");
        System.out.println("********************");
    }

    private void showAndPostScore(Duration time, boolean solved) {
        try {

            String timeFormatted = new SimpleDateFormat("HH:mm:ss").format(new Date(time.toMillis()-3600000));
            System.out.println("Elapsed time: " + timeFormatted);

            if(solved) {
                int timescore = (int)time.toMillis()/2000;
                int points = ((minesField.getRowCount()+ minesField.getColumnCount()) * minesField.getMineCount()) - timescore;

                System.out.println("You won! Your score is: " + points);

                String userName = System.getProperty("user.name");
                Score score = new Score("Minesweeper", userName, points, new Timestamp(System.currentTimeMillis()));
                scoreService.addScore(score);
            }

            List<Score> topScore = scoreService.getTopScores("Minesweeper");
            System.out.println("Top scores:");
            topScore.forEach(s -> System.out.println(s));

        } catch (ScoreException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
