package sk.tuke.gamestudio_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio_backend.entity.Rating;
import sk.tuke.gamestudio_backend.repository.RatingRepository;
import sk.tuke.gamestudio_backend.service.exceptions.RatingException;
import sk.tuke.gamestudio_backend.service.interfaces.RatingService;

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

            List<Rating> ratings = ratingRepository.findAllByGame(game);
            if (ratings.isEmpty()) {
                return -1;
            }
            int avgRating = ratingRepository.findAverageRatingByGame(game);
            avgRating = avgRating == 0 ? -1 : avgRating;
            return avgRating;

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
