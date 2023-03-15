package sk.tuke.gamestudio_library.interfaces;


import sk.tuke.gamestudio_library.entity.Score;
import sk.tuke.gamestudio_library.exceptions.ScoreException;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    void reset() throws ScoreException;
}
