package sk.tuke.gamestudio_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import sk.tuke.gamestudio_backend.entity.Comment;
import sk.tuke.gamestudio_backend.entity.CommentRequest;
import sk.tuke.gamestudio_backend.entity.Score;
import sk.tuke.gamestudio_backend.service.interfaces.CommentService;
import sk.tuke.gamestudio_backend.service.interfaces.RatingService;
import sk.tuke.gamestudio_backend.service.interfaces.ScoreService;
import sk.tuke.gamestudio_backend.service.other.ScoreException;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class GamesWebController {

    @Autowired private ScoreService scoreService;
    @Autowired private CommentService commentService;
    @Autowired private RatingService ratingService;

    @GetMapping("/games/{game}")
    public ModelAndView getGameData(@PathVariable String game, Model model) throws ScoreException {
        List<Score> scores = scoreService.getTopScores(game);
        List<Comment> comments = commentService.getComments(game);
        int rating = ratingService.getAverageRating(game);

        model.addAttribute("comments", comments);
        model.addAttribute("scores", scores);
        model.addAttribute("rating", rating);
        model.addAttribute("game", game);
        return new ModelAndView("pages/games/" + game);
    }


    @GetMapping("/comments")
    public ModelAndView getComments(@RequestParam(defaultValue = "") String game, Model model) {
        List<Comment> comments = commentService.getComments(game);
        model.addAttribute("comments", comments);
        return new ModelAndView("pages/comments");
    }

    @PostMapping("/comments")
    public RedirectView addComment(@ModelAttribute CommentRequest comment) {
        Comment newComment = new Comment(comment.getPlayer(), comment.getGame(), comment.getComment(), new Timestamp(System.currentTimeMillis()));
        commentService.addComment(newComment);
        return new RedirectView("/comments?game=" + comment.getGame());
    }
}
