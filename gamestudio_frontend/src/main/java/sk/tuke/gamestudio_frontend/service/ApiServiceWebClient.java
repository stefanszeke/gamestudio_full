package sk.tuke.gamestudio_frontend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import sk.tuke.gamestudio_frontend.entity.Score;

public class ApiServiceWebClient {

    @Autowired
    private WebClient webClient;

    private static final String APIURL = "http://localhost:8090/api/score";

    public Flux<Score> getTopScoresByGame(String game) {
        return webClient.get()
                .uri(APIURL + "/top/" + game)
                .retrieve()
                .bodyToFlux(Score.class);
    }
}
