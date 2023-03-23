package sk.tuke.gamestudio_backend.service.interfaces;

import sk.tuke.gamestudio_backend.entity.Score;
import sk.tuke.gamestudio_backend.service.exceptions.ScoreException;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    void reset() throws ScoreException;

    Score getScoreById(Long id) throws ScoreException;
}
