package sk.tuke.gamestudio_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sk.tuke.gamestudio_backend.service.CommentServiceJPA;
import sk.tuke.gamestudio_backend.service.RatingServiceJPA;
import sk.tuke.gamestudio_backend.service.ScoreServiceJPA;
import sk.tuke.gamestudio_backend.service.interfaces.CommentService;
import sk.tuke.gamestudio_backend.service.interfaces.RatingService;
import sk.tuke.gamestudio_backend.service.interfaces.ScoreService;

@SpringBootApplication
public class GamestudioBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamestudioBackendApplication.class, args);
		System.out.println("\n**** Server running ****\n");
	}


	@Bean
	public ScoreService scoreService() {
		return new ScoreServiceJPA();
	}

	@Bean
	public CommentService commentService() {
		return new CommentServiceJPA();
	}

	@Bean
	public RatingService ratingService() {
		return new RatingServiceJPA();
	}

}
