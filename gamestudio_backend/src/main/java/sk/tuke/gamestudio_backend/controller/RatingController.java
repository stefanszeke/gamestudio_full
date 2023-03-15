package sk.tuke.gamestudio_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio_backend.entity.Rating;
import sk.tuke.gamestudio_backend.service.interfaces.RatingService;

import java.sql.Timestamp;

@RestController
@CrossOrigin(origins = "http://localhost:8100/")
@RequestMapping("/api/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/avg/{game}") // path param: game:  /api/ratingavg/Blocks
    public int getAverageRating(@PathVariable String game) {
        return ratingService.getAverageRating(game);
    }

    @GetMapping("") // query params: player, game:  /api/rating?player=Jano&game=Blocks
    public int getRating(@RequestParam("player") String player, @RequestParam("game") String game) {
        return ratingService.getRating(game, player);
    }

    @PostMapping({"","/"})
    public void postRating(@RequestBody Rating rating) {
        rating.setRatedOn(new Timestamp(System.currentTimeMillis()));
        ratingService.setRating(rating);
    }

    @DeleteMapping({"","/"})
    public void clearRatingTable() {
        ratingService.reset();
    }
}
