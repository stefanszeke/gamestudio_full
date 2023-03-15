package sk.tuke.gamestudio_frontend.games.Puzzle.PuzzleUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio_frontend.games.Puzzle.Core.PuzzleField;
import sk.tuke.gamestudio_frontend.games.Puzzle.Settings.PuzzleStorage;
import sk.tuke.gamestudio_frontend.games.interfaces.GameField;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;
import sk.tuke.gamestudio_library.entity.Score;
import sk.tuke.gamestudio_library.interfaces.ScoreService;
import sk.tuke.gamestudio_library.exceptions.ScoreException;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@ComponentScan("sk.tuke.gamestudio")
public class PuzzleUI implements UserInterface {

    @Autowired
    private ScoreService scoreService;

    private PuzzleField puzzleField;
    private Scanner scanner;
    private Pattern pattern;

    private boolean exitRequested;
    private boolean solved;

    private int moves;

    public void newGameStarted(GameField puzzleField) {
        this.scanner = new Scanner(System.in);
        this.puzzleField = (PuzzleField) puzzleField;
        this.pattern = Pattern.compile("^[wasdx]$|up|down|left|right|exit|save|load");
        this.moves = 0;

        this.exitRequested = false;
        this.solved = false;

        mainLoop();
    }

    public void mainLoop() {
        setup(this.puzzleField.getShuffleCount());

        Instant start = Instant.now();

        while(!exitRequested && !solved) {
            processInput();
            moves++;
            update();
            solved = puzzleField.isSolved();
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);

        if(exitRequested) return;

        sendAndShowScore(timeElapsed);
    }

    private void processInput() {
        String input = scanner.nextLine().toLowerCase();
        boolean isValidInput = false;

        while (!isValidInput) {
            try {
                validateInput(input);
                isValidInput = true;
            } catch (WrongInputPatternException e) {
                System.out.println(e.getMessage());
                input = scanner.nextLine().toLowerCase();
            }
        }

        if("x".equals(input) || "exit".equals(input)) {
            PuzzleStorage.clear();
            System.out.println("Exiting...");
            exitRequested = true;
            return;
        }

        if("save".equals(input)) {
            PuzzleStorage.savePuzzle(puzzleField);
            System.out.println("** Saved **");
            moves--;
            return;
        }

        if("load".equals(input)) {
            if(PuzzleStorage.loadPuzzle() != null) {
                puzzleField = PuzzleStorage.loadPuzzle();
                System.out.println("** Loaded **");
                moves--;
            } else {
                System.out.println("** Nothing to Load **");
            }
            return;
        }
        puzzleField.move(input);
    }
    private void validateInput(String input) throws WrongInputPatternException {
        Matcher matcher = pattern.matcher(input);

        if("x".equals(input) || "exit".equals(input))  {return; }
        if(!matcher.matches()) { throw new WrongInputPatternException("Not valid input"); }
    }

    private void setup(int shuffleTimes) {
        PuzzleStorage.clear();
        puzzleField.generate();
        System.out.println("[GOAL]:");
        update();
        System.out.println("\n[SHUFFLING ... ]");
        puzzleField.shuffle(shuffleTimes);
        update();
    }


    public void update() {
        if(exitRequested) return;
        System.out.println();
        System.out.println("[moves: "+ moves+"]");
        for(int row = 0; row < puzzleField.getRows(); row++) {
            for(int column = 0; column < puzzleField.getColumns(); column++) {
                if(puzzleField.getPuzzleBlock(row, column) != null) {
                    System.out.printf("| %-2d ",puzzleField.getPuzzleBlock(row, column));
                } else {
                    System.out.printf("| %-2s "," ");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("Enter [WASD]/[up,down,left,right] to move, (x to exit)(save)(load):");
    }

    private void sendAndShowScore(Duration timeElapsed) {
        try {
            System.out.println("Game solved");
            String timeFormatted = String.format("%02d:%02d:%02d",
                    timeElapsed.toHours(),
                    timeElapsed.toMinutes() % 60,
                    timeElapsed.getSeconds() % 60);

            int timePenalty = (int) (timeElapsed.toMillis() / 3000);
            int movesPenalty = moves * 2;
            int fieldScoreSize = puzzleField.getRows() * puzzleField.getColumns() * 100;
            System.out.println("\n[Field size: " + puzzleField.getRows() + "x" + puzzleField.getColumns() + " = Score: " + fieldScoreSize + "]");
            System.out.println("- [Moves: " + moves + " penalty: " + movesPenalty+ "]");
            System.out.println("- [Time: " + timeFormatted + " = penalty: " + timePenalty + "]");
            int finalScore = fieldScoreSize - movesPenalty - timePenalty;
            System.out.println("= [Final score: " + finalScore + "]");

            Score score = new Score("Puzzle", "Player", finalScore, new Timestamp(System.currentTimeMillis()));
            scoreService.addScore(score);

            List<Score> scoreList = scoreService.getTopScores("Puzzle");
            System.out.println("\nTop scores:");
            for (Score s : scoreList) {
                System.out.println(s);
            }
        } catch (ScoreException e) {
            System.out.println("Error sending score");
        }
    }

}
