package sk.tuke.gamestudio_frontend.service.interfaces;


import sk.tuke.gamestudio_frontend.entity.Score;
import sk.tuke.gamestudio_frontend.service.other.ScoreException;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    void reset() throws ScoreException;
}
