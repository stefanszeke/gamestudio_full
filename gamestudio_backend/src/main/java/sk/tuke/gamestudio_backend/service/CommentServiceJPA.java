package sk.tuke.gamestudio_backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import sk.tuke.gamestudio_backend.entity.CommentRequest;
import sk.tuke.gamestudio_backend.repository.CommentRepository;
import sk.tuke.gamestudio_backend.service.interfaces.CommentService;
import sk.tuke.gamestudio_backend.service.exceptions.CommentException;
import sk.tuke.gamestudio_backend.entity.Comment;

import java.sql.Timestamp;
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
            return commentRepository.findFirst10ByGameOrderByIdDesc(game);
        } catch (Exception e) {
            throw new CommentException("Error getting comments\n" + e.getMessage());
        }
    }

    public Comment getCommentById(Long id) {
        try {
            return commentRepository.findById(id).get();
        } catch (Exception e) {
            throw new CommentException("Error getting comment\n" + e.getMessage());
        }
    }

    @Override
    public void updateComment(Long id, CommentRequest commentRequest) {
        try {
            Comment comment = commentRepository.findById(id).get();
            comment.setComment(commentRequest.getComment());
            comment.setPlayer(commentRequest.getPlayer());
            comment.setCommentedOn(new Timestamp(System.currentTimeMillis()));
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new CommentException("Error updating comment\n" + e.getMessage());
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

    @Override
    public void deleteCommentById(Long id) {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            throw new CommentException("Error deleting comment\n" + e.getMessage());
        }
    }
}
