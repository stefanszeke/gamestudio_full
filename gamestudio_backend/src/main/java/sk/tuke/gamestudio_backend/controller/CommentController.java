package sk.tuke.gamestudio_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import sk.tuke.gamestudio_backend.entity.Comment;
import sk.tuke.gamestudio_backend.entity.CommentRequest;
import sk.tuke.gamestudio_backend.service.interfaces.CommentService;
import sk.tuke.gamestudio_backend.service.other.CommentException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{game}")
    public ResponseEntity<?> getAllCommentsByGame(@PathVariable String game) {
        try {
            List<Comment> comments = commentService.getComments(game);
            if (comments.isEmpty()) {
                return new ResponseEntity<>(Map.of("message","No comments found for game " + game), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(comments);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @GetMapping("/v2/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        try {
            Comment comment = commentService.getCommentById(id);
            if (comment == null) {
                return new ResponseEntity<>(Map.of("message","No comment found for id " + id), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(comment);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> postComment(@RequestBody Comment comment) {
        try {
            comment.setCommentedOn(new Timestamp(System.currentTimeMillis()));
            commentService.addComment(comment);
            return new ResponseEntity<>(Map.of("message","Comment added"), HttpStatus.CREATED);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        try {
            commentService.updateComment(id, commentRequest);
            return new ResponseEntity<>(Map.of("message","Comment updated"), HttpStatus.ACCEPTED);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> clearCommentTable() {
        try {
            commentService.reset();
            return new ResponseEntity<>(Map.of("message","Comment table cleared"), HttpStatus.ACCEPTED);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id) {
        try {
            commentService.deleteCommentById(id);
            return new ResponseEntity<>(Map.of("message","Comment deleted"), HttpStatus.ACCEPTED);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }






}
