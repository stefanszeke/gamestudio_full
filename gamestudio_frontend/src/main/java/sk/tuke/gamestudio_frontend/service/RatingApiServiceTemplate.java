package sk.tuke.gamestudio_frontend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio_frontend.entity.Rating;
import sk.tuke.gamestudio_frontend.interfaces.RatingException;
import sk.tuke.gamestudio_frontend.interfaces.RatingService;

public class RatingApiServiceTemplate implements RatingService {


    @Value("${api.url}/rating")
    String APIURL;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public void setRating(Rating rating) throws RatingException {
        restTemplate.postForObject(APIURL, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
//        return restTemplate.getForEntity(APIURL + "/avg/" + game, Integer.class).getBody();
        Integer average = restTemplate.getForObject(APIURL + "/avg/" + game, Integer.class);
        if(average == null) {
            return -1;
        }
        return average;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Integer rating = restTemplate.getForObject(APIURL + "?game=" + game + "&player=" + player, Integer.class);
        if(rating == null) {
            return -1;
        }
        return rating;
    }

    @Override
    public void reset() throws RatingException {
        restTemplate.delete(APIURL);
    }
}
