package sk.tuke.gamestudio_frontend.service;

import org.springframework.beans.factory.annotation.Autowired;

import sk.tuke.gamestudio_frontend.entity.Rating;
import sk.tuke.gamestudio_frontend.repository.RatingRepository;
import sk.tuke.gamestudio_frontend.service.other.RatingException;
import sk.tuke.gamestudio_frontend.service.interfaces.RatingService;

import java.util.List;

public class RatingServiceJPA implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public void setRating(Rating rating) {
        try {
            ratingRepository.save(rating);
        } catch (RatingException e) {
            throw new RatingException("Error saving rating\n" + e.getMessage());
        }
    }

    @Override
    public int getAverageRating(String game) {
        try {
            return ratingRepository.findAverageRatingByGame(game);
        } catch (RatingException e) {
            throw new RatingException("Error getting average rating\n" + e.getMessage());
        }
    }

    @Override
    public int getRating(String game, String player) {
        try {
            List<Rating> ratings = ratingRepository.findAllByGameAndPlayer(game, player);
            return ratings.isEmpty() ? -1 : ratings.get(0).getRating();
        } catch (RatingException e) {
            throw new RatingException("Error getting rating\n" + e.getMessage());
        }
    }

    @Override
    public void reset() throws RatingException {
        try {
            ratingRepository.deleteAll();
        } catch (RatingException e) {
            throw new RatingException("Error resetting ratings\n" + e.getMessage());
        }
    }
}
