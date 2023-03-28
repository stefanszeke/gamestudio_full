package sk.tuke.gamestudio_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.tuke.gamestudio_backend.entity.Rating;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByGame(String game);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.game = ?1")
    double findAverageRatingByGame(String game);

    List<Rating> findAllByGameAndPlayer(String game, String player);
}
