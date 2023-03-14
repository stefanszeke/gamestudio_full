package sk.tuke.gamestudio_frontend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio_frontend.entity.Score;

import java.util.List;

public class ApiServiceTemplate {
    private static final String APIURL = "http://localhost:8090/api/score";

    @Autowired
    private RestTemplate restTemplate;

    public List<Score> getTopScoresByGame(String game) {
//        return restTemplate.getForObject(APIURL + "/top/" + game, List.class);
        ResponseEntity<List<Score>> response = restTemplate.exchange(
                APIURL + "/top/" + game,
                HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<List<Score>>() {
                });
        return response.getBody();
    }
}
