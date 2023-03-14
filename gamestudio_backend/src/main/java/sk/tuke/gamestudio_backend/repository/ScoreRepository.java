package sk.tuke.gamestudio_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.tuke.gamestudio_backend.entity.Score;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByGame(String game);

    @Query("SELECT s FROM Score s WHERE s.game = ?1 ORDER BY s.points DESC LIMIT 10")
    List<Score> findTopScoresByGame(String game);

    List<Score> findTop10ByGameOrderByPointsDesc(String game);
}