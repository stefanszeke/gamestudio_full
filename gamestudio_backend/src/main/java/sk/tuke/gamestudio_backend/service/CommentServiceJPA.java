package sk.tuke.gamestudio_backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import sk.tuke.gamestudio_backend.repository.CommentRepository;
import sk.tuke.gamestudio_backend.service.interfaces.CommentService;
import sk.tuke.gamestudio_backend.service.other.CommentException;
import sk.tuke.gamestudio_backend.entity.Comment;

import java.util.List;

public class CommentServiceJPA implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void addComment(Comment comment) {
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new CommentException("Error saving comment\n" + e.getMessage());
        }

    }

    @Override
    public List<Comment> getComments(String game) {
        try {
            return commentRepository.findAllByGame(game);
        } catch (Exception e) {
            throw new CommentException("Error getting comments\n" + e.getMessage());
        }
    }

    @Override
    public void reset() {
        try {
            commentRepository.deleteAll();
        } catch (Exception e) {
            throw new CommentException("Error resetting comments\n" + e.getMessage());
        }
    }
}
