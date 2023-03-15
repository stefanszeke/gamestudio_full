package sk.tuke.gamestudio_library.interfaces;

import sk.tuke.gamestudio_library.entity.Rating;
import sk.tuke.gamestudio_library.exceptions.RatingException;


public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;
    void reset() throws RatingException;
}
