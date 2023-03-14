package sk.tuke.gamestudio_backend.service.other;

import sk.tuke.gamestudio_backend.service.interfaces.ScoreService;
import sk.tuke.gamestudio_backend.entity.Score;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ScoreServiceFile implements ScoreService {

    private static final File SETTINGS_FILE = new File("src/main/java/sk/tuke/gamestudio/service/ScoreServiceFiles/savefile.bin");



    @Override
    public void addScore(Score score) {
        List<Score> scoreList = getAllScores();
        scoreList.add(score);

        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(SETTINGS_FILE))) {
            outputStream.writeObject(scoreList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(SETTINGS_FILE))) {
            List<Score> scoreList = (List<Score>) inputStream.readObject();
            return scoreList.stream().filter(score -> score.getGame().equals(game)).collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Score> getAllScores() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(SETTINGS_FILE))) {
            List<Score> scoreList = (List<Score>) inputStream.readObject();
            return scoreList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    @Override
    public void reset() {
        if(SETTINGS_FILE.exists()) {
            SETTINGS_FILE.delete();
        }
    }

    @Override
    public Score getScoreById(Long id) throws ScoreException {
        return null;
    }
}
