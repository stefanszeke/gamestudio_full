package sk.tuke.gamestudio_backend.service.interfaces;

import sk.tuke.gamestudio_backend.service.exceptions.RatingException;
import sk.tuke.gamestudio_backend.entity.Rating;

public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;
    void reset() throws RatingException;
}
