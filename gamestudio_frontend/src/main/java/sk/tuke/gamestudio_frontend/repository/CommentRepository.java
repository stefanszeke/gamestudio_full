package sk.tuke.gamestudio_frontend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tuke.gamestudio_frontend.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByGame(String game);
}
