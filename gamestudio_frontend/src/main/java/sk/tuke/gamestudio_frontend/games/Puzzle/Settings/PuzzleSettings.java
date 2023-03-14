package sk.tuke.gamestudio_frontend.games.Puzzle.Settings;
import java.io.*;

public class PuzzleSettings implements Serializable {
    private final int rowCount;
    private final int columnCount;
    private final int shuffleCount;

    public static final PuzzleSettings VERY_EASY = new PuzzleSettings(3, 3, 4);
    public static final PuzzleSettings EASY = new PuzzleSettings(4, 3, 20);
    public static final PuzzleSettings MEDIUM = new PuzzleSettings(4, 4, 40);
    public static final PuzzleSettings HARD = new PuzzleSettings(4, 5, 500);
    public static final PuzzleSettings VERY_HARD = new PuzzleSettings(5, 5, 800);

    private static final String SETTINGS_FILE = "src/main/java/sk/tuke/gamestudio_frontend/games/Puzzle/Settings/puzzle.settings.bin";

    public PuzzleSettings(int rowCount, int columnCount, int shuffleCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.shuffleCount = shuffleCount;
    }

    public PuzzleSettings() throws Exception {
        this.rowCount = PuzzleSettings.EASY.rowCount;
        this.columnCount = PuzzleSettings.EASY.columnCount;
        this.shuffleCount = PuzzleSettings.EASY.shuffleCount;
        save();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getShuffleCount() {
        return shuffleCount;
    }

    public void save() throws Exception {
        FileOutputStream out = new FileOutputStream(SETTINGS_FILE);
        ObjectOutputStream s = new ObjectOutputStream(out);

        s.writeObject(this);
        s.close();
    }

    public static PuzzleSettings load() throws Exception {
        File file = new File(SETTINGS_FILE);

        if (!file.exists()) {
            return new PuzzleSettings();
        }

        FileInputStream in = new FileInputStream(SETTINGS_FILE);
        ObjectInputStream s = new ObjectInputStream(in);

        PuzzleSettings settings = (PuzzleSettings) s.readObject();
        s.close();

        return settings;
    }
}
