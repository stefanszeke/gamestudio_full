package sk.tuke.gamestudio_frontend.games.Tictactoe.core;

import sk.tuke.gamestudio_frontend.games.interfaces.GameField;

import java.util.Map;

public class Grid implements GameField {
    Boolean endGame = false;
    Integer[][] winStates = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    Map<String, Integer> tilesMap = Map.of("a1", 0, "a2", 1, "a3", 2, "b1", 3, "b2", 4, "b3", 5, "c1", 6, "c2", 7, "c3", 8);

    private final Tile[] tiles = new Tile[9];

    public Grid() {
        for(int i = 0; i < 9; i++) {
            tiles[i] = new Tile();
        }

    }

    public Tile getTile(int i) {
        return tiles[i];
    }

    public void setTile(int i, String mark) {
        tiles[i].setMark(mark);
    }

    public boolean getEndGame() {
        return endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public Integer[][] getWinStates() {
        return winStates;
    }

    public Map<String, Integer> getTilesMap() {
        return tilesMap;
    }

    public boolean iskWin() {
        for(int i = 0; i < 8; i++) {
            if(tiles[winStates[i][0]].getMark().equals(tiles[winStates[i][1]].getMark()) && tiles[winStates[i][1]].getMark().equals(tiles[winStates[i][2]].getMark()) && !tiles[winStates[i][0]].getMark().equals(".")) {
                return true;
            }
        }
        return false;
    }

    public boolean iskDraw() {
        for(int i = 0; i < 9; i++) {
            if(tiles[i].getMark().equals(".")) {
                return false;
            }
        }
        return true;
    }
}
