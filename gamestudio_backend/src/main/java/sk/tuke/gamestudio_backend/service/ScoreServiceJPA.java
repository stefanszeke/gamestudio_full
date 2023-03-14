package sk.tuke.gamestudio_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import sk.tuke.gamestudio_backend.entity.Score;
import sk.tuke.gamestudio_backend.repository.ScoreRepository;
import sk.tuke.gamestudio_backend.service.interfaces.ScoreService;
import sk.tuke.gamestudio_backend.service.other.ScoreException;


import java.util.List;

public class ScoreServiceJPA implements ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Override
    public void addScore(Score score) throws ScoreException {
        try {
            scoreRepository.save(score);
        } catch (DataAccessException e) {
            throw new ScoreException("Error saving score\n" + e.getMessage());
        }
    }


    public List<Score> getAllScores() throws ScoreException {
        try {
            return scoreRepository.findAll();
        } catch (DataAccessException e) {
            throw new ScoreException("Error getting top scores\n" + e.getMessage());
        }
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        try {
            return scoreRepository.findTop10ByGameOrderByPointsDesc(game);
        } catch (DataAccessException e) {
            throw new ScoreException("Error getting top scores\n" + e.getMessage());
        }
    }


    public Score getScoreById(Long id) throws ScoreException {
        try {
            return scoreRepository.findById(id).orElse(null);
        } catch (DataAccessException e) {
            throw new ScoreException("Error getting top scores\n" + e.getMessage());
        }
    }

    @Override
    public void reset() throws ScoreException {
        try {
            scoreRepository.deleteAll();
        } catch (DataAccessException e) {
            throw new ScoreException("Error resetting scores\n" + e.getMessage());
        }
    }
}
