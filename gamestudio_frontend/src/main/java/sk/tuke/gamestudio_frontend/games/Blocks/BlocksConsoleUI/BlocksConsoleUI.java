package sk.tuke.gamestudio_frontend.games.Blocks.BlocksConsoleUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;

import sk.tuke.gamestudio_frontend.games.Blocks.Core.Block;
import sk.tuke.gamestudio_frontend.games.Blocks.Core.BlocksField;

import sk.tuke.gamestudio_library.entity.Score;
import sk.tuke.gamestudio_library.interfaces.ScoreService;
import sk.tuke.gamestudio_library.exceptions.ScoreException;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@ComponentScan("sk.tuke.gamestudio")
public class BlocksConsoleUI implements UserInterface {

    @Autowired
    private ScoreService scoreService;

    private BlocksField blocksField;
    private Scanner scanner;
    private Pattern pattern;
    private boolean exitRequested;
    private boolean noMoreMoves;

    public void newGameStarted(GameField blocksField) {
        this.blocksField = (BlocksField) blocksField;
        scanner = new Scanner(System.in);
        pattern = Pattern.compile("^([a-z])(\\d+)$|x");

        exitRequested = false;
        noMoreMoves = false;

        mainLoop();
    }

    private void mainLoop() {
        //        debug();
        Instant start = Instant.now();

        while(!exitRequested && !noMoreMoves) {
            update();
            if(!noMoreMoves) {
                processInput();
            }
            checkMoveAndUpdateEmpty();
            noMoreMoves = blocksField.hasNoMoreMoves();
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);

        System.out.println("Game end");
        if(exitRequested) return;
        postAndShowScore(timeElapsed);
    }

    private void processInput() {
        System.out.println("Enter your move: (A2,B1,C3), x to exit");
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

        if(exitRequested) return;

        Matcher matcher = pattern.matcher(input);
        if(matcher.matches()) {
            int row = Integer.parseInt(matcher.group(2));
            int column = matcher.group(1).charAt(0)-97;

            if(blocksField.getBlock(row-1,column).getColor() == null) {
                System.out.println("Block is already empty");
                return;
            }

            if(blocksField.hasSameAdjacentAndNotNull(row-1,column)) {
                blocksField.clearBlock(row-1,column);
            } else {
                System.out.println("No adjacent friends");
            }
        }
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
            if(column > blocksField.getColumnCount()-1) {
                throw new Exception("Column doesn't exist");
            }

            int row = Integer.parseInt(matcher.group(2));
            if(row-1 > blocksField.getRowCount()-1) {
                throw new Exception("Row doesn't exist");
            }
        }
    }

    private void checkMoveAndUpdateEmpty() {
        while(blocksField.hasEmptyColumn()) {
            update();
            System.out.println("moving left...");
            blocksField.moveEmptyLeft();
        }
    }

    public void postAndShowScore(Duration timeElapsed) {
        try {
        update();
        System.out.println("You won!");
        String timeFormatted = new SimpleDateFormat("HH:mm:ss").format(new Date(timeElapsed.toMillis()-3600000));

        int timescore = (int)timeElapsed.toMillis()/2000;
        int points = 1000 - (blocksField.countNotNullBlocks()*5) - timescore;
        System.out.println("Elapsed time: " + timeFormatted+ " time penalty: " + timescore);
        System.out.println("Blocks left: " + blocksField.countNotNullBlocks() + " points penalty: " + blocksField.countNotNullBlocks()*5);
        System.out.println("Score: 1000 - " + timescore + " - "+ blocksField.countNotNullBlocks()*5+ " = " + points);
        Score newScore = new Score("Blocks", System.getProperty("user.name"), points, new Timestamp(System.currentTimeMillis()));
        scoreService.addScore(newScore);


        List<Score> scores = null;

        scores = scoreService.getTopScores("Blocks");

        System.out.println("Top scores:");
        for (Score score : scores) {
            System.out.println(score);
        }
        } catch (ScoreException e) {
            System.out.println("Error saving score");
        }
    }

    public void update() {
        System.out.printf("%n%3s","|");
        for(int i = 0; i < blocksField.getColumnCount(); i++) System.out.printf("%s|",(char)(65+i));
        System.out.println();
        for(int i = 0;i < blocksField.getRowCount(); i++) {
            System.out.println();
            System.out.printf("%-2d",i+1);
            System.out.print("|");
            for(int j = 0; j < blocksField.getColumnCount(); j++) {
                printSymbol(blocksField.getBlock(i,j));
                System.out.print("|");
            }
        }
        System.out.println();
    }

    public void printSymbol(Block block) {
        if (block.getColor() == null) {
            System.out.print(" ");
            return;
        }
        switch (block.getColor()) {
            case RED ->    System.out.print("▓");
            case BLUE ->   System.out.print("░");
            case YELLOW -> System.out.print("©");
            case GREEN ->  System.out.print("X");
            default -> System.out.println(" ");
        }
    }

    private void debug() {
        for(int i = 0; i < blocksField.getRowCount(); i++) {
            for(int j = 1; j < 4; j++) {
                blocksField.getBlock(i,j).setColor(null);
            }
        }
        blocksField.getBlock(0,3).setColor(Block.Color.RED);
        for(int i = 0; i < blocksField.getRowCount(); i++) {
            for(int j = 6; j < 11; j++) {
                blocksField.getBlock(i,j).setColor(null);
            }
        }
        do {
            blocksField.turnOnGravity();
        } while (blocksField.hasFloatingBlocks());
        update();
        checkMoveAndUpdateEmpty();
    }



}
