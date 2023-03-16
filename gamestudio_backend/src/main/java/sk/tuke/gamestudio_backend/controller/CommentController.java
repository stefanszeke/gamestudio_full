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
//@RequestMapping("api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("api/comment/{game}")
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

    @PostMapping("api/comment")
    public ResponseEntity<?> postComment(@RequestBody Comment comment) {
        try {
            comment.setCommentedOn(new Timestamp(System.currentTimeMillis()));
            commentService.addComment(comment);
            return new ResponseEntity<>(Map.of("message","Comment added"), HttpStatus.CREATED);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @DeleteMapping("api/comment")
    public ResponseEntity<?> clearCommentTable() {
        try {
            commentService.reset();
            return new ResponseEntity<>(Map.of("message","Comment table cleared"), HttpStatus.ACCEPTED);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @GetMapping("/comments")
    public ModelAndView getComments(Model model) {
        List<Comment> comments = commentService.getComments("Minesweeper");
        model.addAttribute("comments", comments);
        return new ModelAndView("pages/comments");
    }

    @PostMapping("/add")
    public RedirectView addComment(@ModelAttribute CommentRequest comment) {

        System.out.println("player: " + comment.getPlayer());
        System.out.println("game: " + comment.getGame());
        System.out.println("comment: " + comment.getComment());

        Comment newComment = new Comment(comment.getPlayer(), comment.getGame(), comment.getComment(), new Timestamp(System.currentTimeMillis()));
        commentService.addComment(newComment);
        return new RedirectView("/comments");
    }




}
