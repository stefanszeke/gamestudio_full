package sk.tuke.gamestudio_frontend.service.interfaces;

import sk.tuke.gamestudio_frontend.entity.Rating;
import sk.tuke.gamestudio_frontend.service.other.RatingException;


public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;
    void reset() throws RatingException;
}
