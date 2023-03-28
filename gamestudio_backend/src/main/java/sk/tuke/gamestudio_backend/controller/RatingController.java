package sk.tuke.gamestudio_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio_backend.entity.Comment;
import sk.tuke.gamestudio_backend.entity.Rating;
import sk.tuke.gamestudio_backend.service.exceptions.CommentException;
import sk.tuke.gamestudio_backend.service.interfaces.RatingService;

import java.sql.Timestamp;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8100/")
@RequestMapping("/api/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/avg/{game}") // path param: game:  /api/ratingavg/Blocks
    public ResponseEntity<?> getAverageRating(@PathVariable String game) {
        try {
            double avg = ratingService.getAverageRating(game);
            if (avg == -1) {
                return new ResponseEntity<>(Map.of("message","No ratings found for game " + game), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(avg);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @GetMapping("") // query params: player, game:  /api/rating?player=Jano&game=Blocks
    public ResponseEntity<?> getRating(@RequestParam("player") String player, @RequestParam("game") String game) {
        try {
            int rating = ratingService.getRating(game, player);
            if (rating == -1) {
                return new ResponseEntity<>(Map.of("message","No rating found for game " + game + " and player " + player), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(rating);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @PostMapping({"","/"})
    public ResponseEntity<?> postRating(@RequestBody Rating rating) {
        try {
            rating.setRatedOn(new Timestamp(System.currentTimeMillis()));
            ratingService.setRating(rating);
            return new ResponseEntity<>(Map.of("message","Rating added"), HttpStatus.CREATED);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @DeleteMapping({"","/"})
    public ResponseEntity<?> clearRatingTable() {
        try {
            ratingService.reset();
            return new ResponseEntity<>(Map.of("message","Rating table cleared"), HttpStatus.OK);
        } catch (CommentException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }
}
