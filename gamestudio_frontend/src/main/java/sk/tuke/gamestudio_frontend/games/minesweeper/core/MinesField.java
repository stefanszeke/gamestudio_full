package sk.tuke.gamestudio_frontend.games.minesweeper.core;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;

/**
 * Field represents playing field and game logic.
 */
public class MinesField implements GameField {
    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;

    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public MinesField(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;

        if (mineCount >= rowCount*columnCount) {
             throw new IllegalArgumentException("Mine count can't be equal or larger than number of tiles");
        } else {
            tiles = new Tile[rowCount][columnCount];
            //generate the field content
            generate();
        }
    }

    public MinesField(Tile[][] tiles, int mineCount) {
        this.rowCount = tiles.length;
        this.columnCount = tiles[0].length;
        this.mineCount = mineCount;
        this.tiles = tiles;
        placeClues();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getState() {
        return state;
    }

    public void setTile(int row, int column, Tile tile) {
        tiles[row][column] = tile;
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public int getNumberOf(Tile.State state) {
        int result = 0;
        for(int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (tiles[i][j].getState() == (state)) result++;
            }
        }
        return  result;
    }


    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if(tile instanceof Clue && ((Clue)tile).getValue() == 0) {
                openAdjacentZeros(row, column);
            }
            if (tile instanceof Mine) {
                state = GameState.FAILED;
                return;
            }

            if (isSolved()) {
                state = GameState.SOLVED;
                return;
            }
        }
    }

    /**
     * Marks tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void markTile(int row, int column) {
        Tile tile = getTile(row, column);
        if(tile.getState() == Tile.State.CLOSED && mineCount > getNumberOf(Tile.State.MARKED)){
            tile.setState(Tile.State.MARKED);
        }else if(tile.getState() == Tile.State.MARKED){
            tile.setState(Tile.State.CLOSED);
        }
    }

    /**
     * Generates playing field.
     */
    private void generate() {
        placeMines();
        placeClues();
    }

    private void placeMines() {
        int minesPlaced = 0;
        while(minesPlaced < mineCount) {
            int randomRowIndex = (int) (Math.random() * rowCount);
            int randomColumnIndex = (int) (Math.random() * columnCount);

            if(tiles[randomRowIndex][randomColumnIndex] == null) {
                Tile tile = new Mine();
                tiles[randomRowIndex][randomColumnIndex] = tile;
                minesPlaced++;
            }
        }
    }

    private void placeClues() {
        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < columnCount; j++) {
                if(tiles[i][j] == null) {
                    int adjacentMines = countAdjacentMines(i,j);
                    Tile tile = new Clue(adjacentMines);
                    tiles[i][j] = tile;
                }
            }
        }
    }

    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    public boolean isSolved() {
        return (rowCount*columnCount) - getNumberOf(Tile.State.OPEN) == mineCount;
    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }
    private void openAdjacentZeros(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                            openTile(actRow, actColumn);
                    }
                }
            }
        }
    }
}
