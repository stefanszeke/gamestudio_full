package sk.tuke.gamestudio_frontend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;




import sk.tuke.gamestudio_library.entity.Score;
import sk.tuke.gamestudio_library.exceptions.ScoreException;
import sk.tuke.gamestudio_library.interfaces.ScoreService;

import java.util.List;


public class ScoreApiServiceTemplate implements ScoreService {

    @Value("${api.url}/score")
    private String APIURL;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Score> getTopScores(String game) {
//        return restTemplate.getForObject(APIURL + "/top/" + game, List.class);
        ResponseEntity<List<Score>> response = restTemplate.exchange(
                APIURL + "/top/" + game,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Score>>() {
                });
        return response.getBody();
    }

    @Override
    public void addScore(Score score) throws ScoreException {
        restTemplate.postForObject(APIURL, score, Score.class);
    }


    @Override
    public void reset() throws ScoreException {
        restTemplate.delete(APIURL);
    }
}
