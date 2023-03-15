package sk.tuke.gamestudio_library.interfaces;


import sk.tuke.gamestudio_library.entity.Comment;
import sk.tuke.gamestudio_library.exceptions.CommentException;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
    void reset() throws CommentException;
}
