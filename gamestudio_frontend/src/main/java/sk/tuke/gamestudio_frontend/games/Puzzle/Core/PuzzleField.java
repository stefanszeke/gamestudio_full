package sk.tuke.gamestudio_frontend.games.Puzzle.Core;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;

import java.io.Serializable;

public class PuzzleField implements Serializable, GameField {

    private Integer[][] puzzleField;
    private int rows;
    private int columns;
    private int shuffleCount;
    private int[] player;

    public PuzzleField(int rows, int columns, int shuffleCount) {
        puzzleField = new Integer[rows][columns];
        this.rows = rows;
        this.columns = columns;
        this.shuffleCount = shuffleCount;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getShuffleCount() {
        return shuffleCount;
    }

    public Integer getPuzzleBlock(int row, int column) {
        return puzzleField[row][column];
    }

    public Integer[][] getPuzzleField() {
        return puzzleField;
    }

    public void setPuzzleField(Integer[][] newField) {
        this.puzzleField = newField;
    }

    public void setPuzzleBlock(int row, int column, Integer value) {
        puzzleField[row][column] = value;
    }

    public void generate() {
        int count = 1;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                puzzleField[row][column] = count;
                count++;
            }
        }
        puzzleField[rows - 1][columns - 1] = null;
        this.player = new int[]{rows - 1, columns - 1};
    }

    public void move(String direction) {
        if (direction.equals("s") || direction.equals("down")) {
            if (player[0] > 0) {
                int swapValue = getPuzzleBlock(player[0] - 1, player[1]);
                puzzleField[player[0]][player[1]] = swapValue;
                puzzleField[player[0] - 1][player[1]] = null;
                player[0]--;
            }
        } else if (direction.equals("w") || direction.equals("up")) {
            if (player[0] < rows - 1) {
                int swapValue = getPuzzleBlock(player[0] + 1, player[1]);
                puzzleField[player[0]][player[1]] = swapValue;
                puzzleField[player[0] + 1][player[1]] = null;
                player[0]++;
            }
        } else if (direction.equals("d") || direction.equals("right")) {
            if (player[1] > 0) {
                int swapValue = getPuzzleBlock(player[0] , player[1]-1);
                puzzleField[player[0]][player[1]] = swapValue;
                puzzleField[player[0]][player[1]-1] = null;
                player[1]--;
            }
        } else if (direction.equals("a") || direction.equals("left")) {
            if (player[1] < columns-1) {
                int swapValue = getPuzzleBlock(player[0] , player[1]+1);
                puzzleField[player[0]][player[1]] = swapValue;
                puzzleField[player[0]][player[1]+1] = null;
                player[1]++;
            }
        }
    }

    public boolean isSolved() {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if(row != rows-1 || column != columns-1) {

                    if(getPuzzleBlock(row,column) == null) {
                        return false;
                    }
                    int value = getPuzzleBlock(row,column);
                    if(count+1 != value) {
                        return false;
                    }
                    count = value;
                }
            }
        }
        return true;
    }

    public void shuffle(int times) {
        String[] moves = {"w","a","s","d"};
        for(int i = 0; i < times; i++) {
            String randomMove = moves[(int)(Math.random() * moves.length)];
            move(randomMove);
        }
        while(isSolved()) {
            shuffle(times);
        }
    }





}
