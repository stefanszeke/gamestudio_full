package sk.tuke.gamestudio_frontend.service;

import org.springframework.beans.factory.annotation.Autowired;

import sk.tuke.gamestudio_frontend.entity.Comment;
import sk.tuke.gamestudio_frontend.repository.CommentRepository;
import sk.tuke.gamestudio_frontend.service.interfaces.CommentService;
import sk.tuke.gamestudio_frontend.service.other.CommentException;


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
