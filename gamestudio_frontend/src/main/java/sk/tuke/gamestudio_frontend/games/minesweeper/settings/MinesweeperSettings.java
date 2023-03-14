package sk.tuke.gamestudio_frontend.games.minesweeper.settings;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class MinesweeperSettings implements Serializable {

    private final int rowCount;
    private final int columnCount;
    private final int mineCount;

    public static final MinesweeperSettings EASY = new MinesweeperSettings(9, 9, 10);
    public static final MinesweeperSettings MEDIUM = new MinesweeperSettings(16, 16, 40);
    public static final MinesweeperSettings HARD = new MinesweeperSettings(16, 30, 99);
    public static final MinesweeperSettings TEST = new MinesweeperSettings(8, 8, 2);

    private static final String SETTINGS_FILE = "src/main/java/sk/tuke/gamestudio_frontend/games/minesweeper/settings/minesweeper.settings.bin";

    public MinesweeperSettings(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
    }

    public MinesweeperSettings() throws Exception {
        this.rowCount = EASY.rowCount;
        this.columnCount = EASY.columnCount;
        this.mineCount = EASY.mineCount;
        save();
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

    public void save() throws Exception {
        FileOutputStream out = new FileOutputStream(SETTINGS_FILE);
        ObjectOutputStream s = new ObjectOutputStream(out);

        s.writeObject(this);
        s.close();
    }

    public static MinesweeperSettings load() throws Exception {
        File file = new File(SETTINGS_FILE);

        if (!file.exists()) {
            return new MinesweeperSettings();
        }

        FileInputStream in = new FileInputStream(SETTINGS_FILE);
        ObjectInputStream s = new ObjectInputStream(in);

        MinesweeperSettings settings = (MinesweeperSettings) s.readObject();
        s.close();

        return settings;
    }
}
