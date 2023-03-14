package sk.tuke.gamestudio_frontend.games.Jewels.core;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;

public class JewelField implements GameField {
    private Jewel[][] jewels;
    private int rowCount;
    private int columnCount;

    private int adjacentCount;
    private int toReplenish;
    private int score;
    private int scoreToWrite;
    private int moves;

    public JewelField(int rows, int columns) {
        this.rowCount = rows;
        this.columnCount = columns;
        this.jewels = new Jewel[rows][columns];
        this.adjacentCount = 0;
        this.toReplenish = 0;
        this.scoreToWrite = 0;
        this.score = 0;
        this.moves = 0;
        generate();
    }

    public void swapJewels(int row1, int column1, int row2, int column2) {
        int count1 = 0;
        int count2 = 0;
        Jewel.Color color1 = getJewel(row1,column1).getColor();
        Jewel.Color color2 = getJewel(row2,column2).getColor();
        getJewel(row1,column1).setColor(color2);
        getJewel(row2,column2).setColor(color1);

        adjacentCount = 0;
        countAdjacentBlock(row1, column1, color1);
        count1 = adjacentCount;
        if(count1 > 2) {
            setAdjacentCountBlocks(null);
        } else {
            setAdjacentCountBlocks(color1);
        }

        adjacentCount = 0;
        countAdjacentBlock(row2, column2, color2);
        count2 = adjacentCount;
        if(count2 > 2) {
            setAdjacentCountBlocks(null);
        } else {
            setAdjacentCountBlocks(color2);
        }

        if(count1 < 3 && count2 < 3) {
            getJewel(row1,column1).setColor(color1);
            getJewel(row2,column2).setColor(color2);
        } else {
            moves++;
        }
        score += scoreToWrite;
        System.out.println("score test:"  + score);
        scoreToWrite = 0;
    }

    private void generate() {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                Jewel.Color newColor = Jewel.Color.values()[(int) (Math.random() * 4)];
                int adjacent = getDirectAdjacent(row, column, newColor);
                while(adjacent > 0) {
                    newColor = Jewel.Color.values()[(int) (Math.random() * 4)];
                    adjacent = getDirectAdjacent(row, column, newColor);
                }
                jewels[row][column] = new Jewel(newColor);
            }
        }
    }

    public Jewel getJewel(int row, int column) {
        return jewels[row][column];
    }
    public int getRowCount() {
        return rowCount;
    }
    public int getColumnCount() {
        return columnCount;
    }
    public int getAdjacentCount() {
        return adjacentCount;
    }
    public int getScore() {
        return score;
    }
    public int getMoves() {
        return moves;
    }

    public void turnOnGravity() {
        for(int i = 0;i < getRowCount()-1; i++) {
            for(int j = 0; j < getColumnCount(); j++) {
                if(getJewel(i+1,j).getColor() == null) {
                    // copy to empty block
                    getJewel(i+1,j).setColor(getJewel(i,j).getColor());
                    // set old blocks null
                    getJewel(i,j).setColor(null);
                }
            }
        }
    }
    public boolean hasFloatingBlocks() {
        for(int i = 1; i < getRowCount(); i++) {
            for(int j = 0; j < getColumnCount(); j++) {
                if(getJewel(i,j).getColor() == null) {
                    if( getJewel(i-1,j).getColor() != null ) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public void countAdjacentBlock(int row, int column, Jewel.Color color) {
        int[][] adjacent = {{row - 1, column}, {row + 1, column}, {row, column - 1}, {row, column + 1}};
        for(int[] adj: adjacent) {
            if( (adj[0] >= 0 && adj[0] < rowCount) && (adj[1] >= 0 && adj[1] < columnCount) ) {
                if (getJewel(adj[0], adj[1]).getColor() == color) {
                    adjacentCount++;
                    countJewel(adj[0], adj[1]);
                }
            }
        }
    }
    public int getDirectAdjacent(int row, int column, Jewel.Color color) {
        int count = 0;
        int[][] adjacent = {{row - 1, column}, {row + 1, column}, {row, column - 1}, {row, column + 1}};
        for(int[] adj: adjacent) {
            if( (adj[0] >= 0 && adj[0] < rowCount) && (adj[1] >= 0 && adj[1] < columnCount) ) {
                if(jewels[adj[0]][adj[1]] != null) {
                    if (getJewel(adj[0], adj[1]).getColor() == color) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void countJewel(int row, int column) {
        Jewel.Color color = getJewel(row, column).getColor();
        getJewel(row, column).setColor(Jewel.Color.COUNT);
        countAdjacentBlock(row, column, color);
    }

    public void clearFields(int row, int column) {
        Jewel.Color color = getJewel(row, column).getColor();
        adjacentCount = 0;
        countJewel(row, column);
        if(adjacentCount > 2) {
            setAdjacentCountBlocks(null);
        } else {
            setAdjacentCountBlocks(color);
        }
    }

    private void setAdjacentCountBlocks(Jewel.Color color) {
        int count = 0;
        for(int row = 0; row < getRowCount(); row++) {
            for(int column = 0; column < getColumnCount(); column++) {
                if(getJewel(row, column).getColor() == Jewel.Color.COUNT) {
                    getJewel(row, column).setColor(color);
                    count++;
                    if(color == null) toReplenish++;
                }
                if(count == adjacentCount) {
                    if(color == null) {
                        System.out.println("count: " + count);
                        scoreToWrite += (10*count) + (count*count);
                        System.out.println("score to write: " + scoreToWrite);
                    }
                    return;
                }
            }
        }
    }

    public void replenish() {
        int replenished = 0;
        for(int row = 0; row < getRowCount(); row++) {
            for(int column = 0; column < getColumnCount(); column++) {
                if(getJewel(row,column).getColor() == null) {
                    getJewel(row,column).setColor(Jewel.Color.values()[(int) (Math.random() * 4)]);
                    replenished++;
                    if(replenished == toReplenish) {
                        toReplenish = 0;
                        return;
                    }
                }
            }
        }
    }


}

// 3 10 * 3 + 3 * 3 = 30 + 9 = 39
// 4 10 * 4 + 4 * 4 = 40 + 16 = 56
// 5 10 * 5 + 5 * 5 = 50 + 25 = 75
// 6 10 * 6 + 6 * 6 = 60 + 36 = 96 (39 * 2 = 78)
