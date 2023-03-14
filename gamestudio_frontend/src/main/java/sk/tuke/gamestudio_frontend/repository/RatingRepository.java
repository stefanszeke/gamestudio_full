package sk.tuke.gamestudio_frontend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.tuke.gamestudio_frontend.entity.Rating;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.game = ?1")
    int findAverageRatingByGame(String game);

    List<Rating> findAllByGameAndPlayer(String game, String player);
}
