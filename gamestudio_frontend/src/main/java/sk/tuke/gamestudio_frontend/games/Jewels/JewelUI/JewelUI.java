package sk.tuke.gamestudio_frontend.games.Jewels.JewelUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio_frontend.entity.Score;
import sk.tuke.gamestudio_frontend.games.Jewels.core.Jewel;
import sk.tuke.gamestudio_frontend.games.Jewels.core.JewelField;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;
import sk.tuke.gamestudio_frontend.service.interfaces.ScoreService;
import sk.tuke.gamestudio_frontend.service.other.ScoreException;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@ComponentScan("sk.tuke.gamestudio")
public class JewelUI implements UserInterface {

    @Autowired
    private ScoreService scoreService;

    private JewelField jewelField;
    private Scanner scanner;
    private Pattern pattern;

    private boolean exitRequested;
    private boolean noMoreMoves;
    private int moves;

    public void newGameStarted(GameField jewelField) {
        this.jewelField = (JewelField) jewelField;
        scanner = new Scanner(System.in);
        pattern = Pattern.compile("^([a-z])(\\d+)$|x");
        exitRequested = false;
        noMoreMoves = false;
        moves = 2;

        mainLoop();
    }

    public void mainLoop() {
        System.out.println("New game started");

        update();

        Instant start = Instant.now();

        while(!exitRequested && !noMoreMoves) {
            makeMove();
            if(exitRequested) return;
            update();

            while(jewelField.hasFloatingBlocks()) {
                jewelField.turnOnGravity();
                update();
            }
            jewelField.replenish();
            update();
            noMoreMoves = hasNoMoreMoves();
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);

        if(exitRequested) return;

        postAndShowScore(timeElapsed);

        askNewGame();
    }

    public void update() {
        System.out.printf("%n%2sScore: %d"," ",jewelField.getScore());
        System.out.printf("%2sMoves: %d/%d%n"," ",jewelField.getMoves(),moves);
        System.out.printf("%3s","|");
        for(int i = 0; i < jewelField.getColumnCount(); i++) System.out.printf("%s|",(char)(65+i));
        System.out.println();
        for(int i = 0;i < jewelField.getRowCount(); i++) {
            System.out.println();
            System.out.printf("%-2d",i+1);
            System.out.print("|");
            for(int j = 0; j < jewelField.getColumnCount(); j++) {
                printSymbol(jewelField.getJewel(i,j));
                System.out.print("|");
            }
        }
        System.out.println();
    }

    private void makeMove() {
        boolean validMove = false;
        int[] switchFrom = null;
        int[] switchTo = null;

        while(!exitRequested && !validMove) {
            System.out.println("Switch FROM ?: (A2,B1,C3), x to exit");
            switchFrom = processInput();
            if(exitRequested) return;
            System.out.println("Switch TO ?: (A2,B1,C3), x to exit");
            switchTo = processInput();
            if(exitRequested) return;
            try {
                checkInputDistance(switchFrom,switchTo);
                validMove = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Switch FROM ?: (A2,B1,C3), x to exit");
                switchFrom = processInput();
                System.out.println("Switch TO ?: (A2,B1,C3), x to exit");
                switchTo = processInput();
            }
        }


        if(switchFrom != null && switchTo != null) {
            jewelField.swapJewels(switchFrom[0],switchFrom[1],switchTo[0],switchTo[1]);
        }
    }

    private int[] processInput() {
        String input = scanner.nextLine().toLowerCase();
        boolean inputValid = false;

        while (!inputValid) {
            try {
                validateInput(input);
                inputValid = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                input = scanner.nextLine().toLowerCase();
            }
        }

        if(exitRequested) return null;

        Matcher matcher = pattern.matcher(input);
        if(matcher.matches()) {
            int row = Integer.parseInt(matcher.group(2));
            int column = matcher.group(1).charAt(0)-97;
            return new int[]{row-1,column};
        }
        return null;
    }

    private void validateInput(String input) throws Exception {
        Matcher matcher = pattern.matcher(input);

        if("x".equals(input)) {
            System.out.println("Exiting...");
            exitRequested = true;
            return;
        }

        if(!matcher.matches()) {
            throw new Exception("Not valid input");
        }

        if(matcher.matches()) {

            int column = matcher.group(1).charAt(0)-97;
            if(column > jewelField.getColumnCount()-1) {
                throw new Exception("Column doesn't exist");
            }

            int row = Integer.parseInt(matcher.group(2));
            if(row-1 > jewelField.getRowCount()-1) {
                throw new Exception("Row doesn't exist");
            }
        }
    }

    public void printSymbol(Jewel jewel) {
        if (jewel.getColor() == null) {
            System.out.print(" ");
            return;
        }
        switch (jewel.getColor()) {
            case RED ->    System.out.print("▓");
            case BLUE ->   System.out.print("░");
            case YELLOW -> System.out.print("©");
            case GREEN ->  System.out.print("X");
            case COUNT ->  System.out.print("C");
            default -> System.out.println(" ");
        }
    }

    public void checkInputDistance(int[] switchFrom, int[] switchTo) throws Exception {
        if( Math.abs(switchFrom[0]-switchTo[0]) > 1 || Math.abs(switchFrom[1]-switchTo[1]) > 1 ) {
            throw new Exception("Cant switch !");
        }
    }

    public void postAndShowScore(Duration timeElapsed) {
        try {
            System.out.println("Game solved");
            String timeFormatted = String.format("%02d:%02d:%02d",
                    timeElapsed.toHours(),
                    timeElapsed.toMinutes() % 60,
                    timeElapsed.getSeconds() % 60);
            System.out.println("Time elapsed: " + timeFormatted);
            System.out.println("Score: " + jewelField.getScore());


            Score score = new Score("Jewels", "Player", jewelField.getScore(), new Timestamp(System.currentTimeMillis()));
            scoreService.addScore(score);

            List<Score> scoreList = scoreService.getTopScores("Jewels");
            System.out.println("\nTop scores:");
            for (Score s : scoreList) {
                System.out.println(s);
            }
        } catch (ScoreException e) {
            System.out.println("Error posting score");
        }
    }

    public void askNewGame() {
        System.out.println("\nNew game? (y - yes)");
        String input = scanner.nextLine().toLowerCase();

        if("y".equals(input)) {
            newGameStarted(new JewelField(jewelField.getRowCount(), jewelField.getColumnCount()));
        } else {
            System.out.println("Exiting...");
            exitRequested = true;
        }

    }

    public boolean hasNoMoreMoves() {
        return jewelField.getMoves() >= moves;
    }
}

// [0,0] [0,1] [0,2]
// [1,0] [1,1] [1,2]
// [2,0] [2,1] [2,2]
