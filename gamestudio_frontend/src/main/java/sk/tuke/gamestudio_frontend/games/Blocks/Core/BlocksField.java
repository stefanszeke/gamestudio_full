package sk.tuke.gamestudio_frontend.games.Blocks.Core;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;

public class BlocksField implements GameField {

    private Block[][] blocks;
    private int rowCount;
    private int columnCount;

    private int emptyColumnIndex;
    private int lastEmptyColumnIndex;

    public BlocksField(int rows, int columns) {
        this.rowCount = rows;
        this.columnCount = columns;
        this.emptyColumnIndex = -1;
        this.lastEmptyColumnIndex = columns-1;
        blocks = new Block[rows][columns];

        generate();
    }

    private void generate() {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                blocks[row][column] = new Block(Block.Color.values()[(int) (Math.random() * Block.Color.values().length)]);
            }
        }
    }

    public Block getBlock(int row, int column) {
        return blocks[row][column];
    }
    public int getRowCount() {
        return rowCount;
    }
    public int getColumnCount() {
        return columnCount;
    }

    public void clearBlock(int row, int column) {
        Block.Color color = getBlock(row,column).getColor();
        getBlock(row,column).setColor(null);
        clearAdjacentBlock(row, column,color);

        do {
            turnOnGravity();
        } while (hasFloatingBlocks());
    }


    public void turnOnGravity() {
        for(int i = 0;i < getRowCount()-1; i++) {
            for(int j = 0; j < getColumnCount(); j++) {
                if(getBlock(i+1,j).getColor() == null) {
                    // copy to empty block
                    getBlock(i+1,j).setColor(getBlock(i,j).getColor());
                    // set old blocks null
                    getBlock(i,j).setColor(null);
                }
            }
        }
    }

    public boolean hasSameAdjacentAndNotNull(int row, int column) {
        if(getBlock(row,column).getColor() == null) {
            return false;
        }
        int[][] adjacent = {{row - 1, column}, {row + 1, column}, {row, column - 1}, {row, column + 1}};

        for(int[] adj: adjacent) {
            if( (adj[0] >= 0 && adj[0] < rowCount) && (adj[1] >= 0 && adj[1] < columnCount) ) {
                if( getBlock(adj[0],adj[1]).getColor() == getBlock(row,column).getColor() )
                    return true;
            }
        }
        return false;
    }
    public void clearAdjacentBlock(int row, int column, Block.Color color) {
        int[][] adjacent = {{row - 1, column}, {row + 1, column}, {row, column - 1}, {row, column + 1}};

        for(int[] adj: adjacent) {
            if( (adj[0] >= 0 && adj[0] < rowCount) && (adj[1] >= 0 && adj[1] < columnCount) ) {
                    if( getBlock(adj[0],adj[1]).getColor() == color ) {
                        clearBlock(adj[0],adj[1]);
                    }
            }
        }
    }

    public boolean hasFloatingBlocks() {
        for(int i = 1; i < getRowCount(); i++) {
            for(int j = 0; j < getColumnCount(); j++) {
                if(getBlock(i,j).getColor() == null) {
                    if( getBlock(i-1,j).getColor() != null ) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public boolean checkAndMoveEmptyColumn() {
        if(hasEmptyColumn()) {
            moveEmptyLeft();
            return true;
        }
        return false;
    }

    public boolean hasEmptyColumn() {
        int lastRow = getRowCount()-1;
        int columns = lastEmptyColumnIndex;
        for(int i = 0; i < columns; i++) {
            if(getBlock(lastRow,columns-1-i).getColor() == null) {
                emptyColumnIndex = columns-1-i;
                return true;
            }
        }
        return false;
    }

    public void  moveEmptyLeft() {
        for(int i = emptyColumnIndex+1; i <= lastEmptyColumnIndex; i++) {
            for(int j = 0; j < getRowCount(); j++) {
                getBlock(j,i-1).setColor(getBlock(j,i).getColor());
            }
        }
        clearLastColumn();
        lastEmptyColumnIndex--;
    }

    private void clearLastColumn() {
        for(int i = 0; i < getRowCount(); i++) {
            getBlock(i,lastEmptyColumnIndex).setColor(null);
        }
    }

    public boolean hasNoMoreMoves() {
        for(int i = 0; i < getRowCount(); i++) {
            for(int j = 0; j < getColumnCount(); j++) {
                if(hasSameAdjacentAndNotNull(i,j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int countNotNullBlocks() {
        int count = 0;
        for(int i = 0; i < getRowCount(); i++) {
            for(int j = 0; j <= lastEmptyColumnIndex; j++) {
                if(getBlock(i,j).getColor() != null) {
                    count++;
                }
            }
        }
        return count;
    }

}
