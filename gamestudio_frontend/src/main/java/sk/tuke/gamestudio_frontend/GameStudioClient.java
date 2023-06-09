package sk.tuke.gamestudio_frontend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import sk.tuke.gamestudio_frontend.gamestudioUI.GameStudioConsole;
import sk.tuke.gamestudio_frontend.interfaces.CommentService;
import sk.tuke.gamestudio_frontend.interfaces.RatingService;
import sk.tuke.gamestudio_frontend.interfaces.ScoreService;
import sk.tuke.gamestudio_frontend.service.ApiServiceWebClient;
import sk.tuke.gamestudio_frontend.service.CommentApiServiceTemplate;
import sk.tuke.gamestudio_frontend.service.RatingApiServiceTemplate;
import sk.tuke.gamestudio_frontend.service.ScoreApiServiceTemplate;



@SpringBootApplication
public class GameStudioClient {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(GameStudioConsole.class, args);
    }

    @Bean
    public CommandLineRunner runGameStudio(GameStudioConsole gameStudioConsole) {
        return args -> gameStudioConsole.run3();
    }

    // client
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public ScoreApiServiceTemplate apiService() {
        return new ScoreApiServiceTemplate();
    }
    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }
    @Bean
    public ApiServiceWebClient apiServiceWebClient() {
        return new ApiServiceWebClient();
    }

    // backend
    @Bean
    public ScoreService scoreService() {
        return new ScoreApiServiceTemplate();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingApiServiceTemplate();
    }


    @Bean
    public CommentService commentService() {
        return new CommentApiServiceTemplate();
    }

}
