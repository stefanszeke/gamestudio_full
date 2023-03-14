package sk.tuke.gamestudio_backend.service.interfaces;

import sk.tuke.gamestudio_backend.entity.Comment;
import sk.tuke.gamestudio_backend.service.other.CommentException;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
    void reset() throws CommentException;
}
