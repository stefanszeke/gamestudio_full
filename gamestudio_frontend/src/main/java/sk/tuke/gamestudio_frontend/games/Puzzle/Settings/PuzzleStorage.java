package sk.tuke.gamestudio_frontend.games.Puzzle.Settings;

import sk.tuke.gamestudio_frontend.games.Puzzle.Core.PuzzleField;

import java.io.*;

public class PuzzleStorage implements Serializable {


    private static final String SAVE_FILE = "src/main/java/sk/tuke/gamestudio/games/Puzzle/Settings/puzzleSave.bin";


    public static void savePuzzle(PuzzleField puzzleField) {

        try {
            FileOutputStream out = new FileOutputStream(SAVE_FILE);
            ObjectOutputStream s = new ObjectOutputStream(out);

            s.writeObject(puzzleField);
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PuzzleField loadPuzzle() {
        try {
            File file = new File(SAVE_FILE);

            if (!file.exists()) {
                return null;
            }

            FileInputStream in = new FileInputStream(SAVE_FILE);
            ObjectInputStream s = new ObjectInputStream(in);

            PuzzleField puzzle = (PuzzleField) s.readObject();
            s.close();
            return puzzle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void clear() {
        try {
            File file = new File(SAVE_FILE);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
