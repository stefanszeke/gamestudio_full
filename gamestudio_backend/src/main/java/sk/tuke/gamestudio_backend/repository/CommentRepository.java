package sk.tuke.gamestudio_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio_backend.entity.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByGame(String game);

    List<Comment> findFirst10ByGameOrderByIdDesc(String game);
}
