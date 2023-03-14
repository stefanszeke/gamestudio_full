package sk.tuke.gamestudio_backend.service.other;

import sk.tuke.gamestudio_backend.service.interfaces.ScoreService;
import sk.tuke.gamestudio_backend.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class ScoreServiceMock implements ScoreService {


    public static ScoreService getInstance() {
        return new ScoreServiceMock();
    }

    @Override
    public void addScore(Score score) {
        System.out.println("MOCK SCORE SERVICE: " + score);
    }

    @Override
    public List<Score> getTopScores(String game) {
        System.out.println("MOCK SCORE SERVICE: " + game);
        List<Score> scores = new ArrayList<>();

        scores.add(new Score(game, "MOCK1", 100, new Timestamp(System.currentTimeMillis())));
        scores.add(new Score(game, "MOCK2", 200, new Timestamp(System.currentTimeMillis())));
        return scores;

    }

    @Override
    public void reset() {
        System.out.println("MOCK SCORE SERVICE: reset");
    }

    @Override
    public Score getScoreById(Long id) throws ScoreException {
        return null;
    }
}
