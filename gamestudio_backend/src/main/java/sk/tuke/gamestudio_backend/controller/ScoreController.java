package sk.tuke.gamestudio_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio_backend.entity.Score;
import sk.tuke.gamestudio_backend.service.interfaces.ScoreService;
import sk.tuke.gamestudio_backend.service.exceptions.ScoreException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8100/")
@RequestMapping("/api/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/top/{game}")
    public ResponseEntity<?> getTopScores(@PathVariable String game) {
        try {
            List<Score> topScores = scoreService.getTopScores(game);
            if(topScores.isEmpty()) {
//                return ResponseEntity.notFound().build();
                return new ResponseEntity<>(Map.of("message","No scores found for game " + game), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(topScores);

        } catch (ScoreException e) {
           return serverError(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScoreById(@PathVariable Long id) {
        try {
            Score scores = scoreService.getScoreById(id);
            if(scores == null) {
//                return ResponseEntity.notFound().build();
                return new ResponseEntity<>(Map.of("message","No scores found for id " + id), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(scores);
        } catch (ScoreException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }

    }

    @PostMapping({"","/"})
    public ResponseEntity<?> postScore(@RequestBody Score score) {
        try {
            score.setPlayedOn(new Timestamp(System.currentTimeMillis()));
            scoreService.addScore(score);
//            return ResponseEntity.created(URI.create("http://localhost:8085/api/score/" + score.getId())).build();
            return new ResponseEntity<>(Map.of("message","Score added"), HttpStatus.CREATED);
        } catch (ScoreException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    @DeleteMapping({"","/"})
    public ResponseEntity<?> clearScoreTable() {
        try {
            scoreService.reset();
//            return ResponseEntity.noContent().build();
            return new ResponseEntity<>(Map.of("message","Score table cleared"), HttpStatus.ACCEPTED);
        } catch (ScoreException e) {
            return ResponseEntity.internalServerError().body("server error " + e.getMessage());
        }
    }

    private static ResponseEntity<?> serverError(ScoreException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.internalServerError().body(Map.of("message","Server Error"));
    }


//    @GetMapping("/top/{game}")
//    public List<Score> getTopScores(@PathVariable String game) {
//        return scoreService.getTopScores(game);
//    }

//    @PostMapping({"","/"})
//    public void postScore(@RequestBody Score score) {
//        score.setPlayedOn(new Timestamp(System.currentTimeMillis()));
//        scoreService.addScore(score);
//    }

//    @DeleteMapping({"","/"})
//    public void clearScoreTable() {
//        scoreService.reset();
//    }
}
