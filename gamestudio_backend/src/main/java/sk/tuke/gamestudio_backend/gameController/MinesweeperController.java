package sk.tuke.gamestudio_backend.gameController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import sk.tuke.gamestudio_backend.Games.minesweeper.core.Clue;
import sk.tuke.gamestudio_backend.Games.minesweeper.core.GameState;
import sk.tuke.gamestudio_backend.Games.minesweeper.core.MinesField;
import sk.tuke.gamestudio_backend.Games.minesweeper.core.Tile;
import sk.tuke.gamestudio_backend.entity.Comment;
import sk.tuke.gamestudio_backend.entity.Rating;
import sk.tuke.gamestudio_backend.entity.Score;
import sk.tuke.gamestudio_backend.service.exceptions.ScoreException;
import sk.tuke.gamestudio_backend.service.interfaces.CommentService;
import sk.tuke.gamestudio_backend.service.interfaces.RatingService;
import sk.tuke.gamestudio_backend.service.interfaces.ScoreService;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/games/MinesweeperTailwind")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MinesweeperController {

    private MinesField field = null;
    private boolean marking = false;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    public String testString() {
        return "test";
    }

    public  int getRowCount() {
        MinesField field = new MinesField(7, 7, 2);
        return field.getRowCount();
    }

    ////////////////
    // requests
    @GetMapping("/loadServices")
    public ModelAndView getGameData() throws ScoreException {
        ModelAndView modelAndView = new ModelAndView("pages/games/MinesweeperTailwind")
                .addObject("comments", commentService.getComments("MineSweeperTailwind"))
                .addObject("scores", scoreService.getTopScores("MineSweeperTailwind"))
                .addObject("rating", ratingService.getAverageRating("MineSweeperTailwind"))
                .addObject("game", "MineSweeperTailwind");

        return modelAndView;
    }

    @RequestMapping
    public String processInput(@RequestParam(value = "row", required = false) Integer row, @RequestParam(value = "column", required = false) Integer column) throws ScoreException {
        statOrUpdateGame(row, column);
        return "pages/games/MinesweeperTailwind";
    }

    @RequestMapping("/markSwitch")
    public String switchMarking() {
        if(field == null) startNewGame();
        else marking = !marking;
        return "pages/games/MinesweeperTailwind";
    }

    @RequestMapping("/newGame")
    public String newGame() {
        startNewGame();
        return "pages/games/MinesweeperTailwind";
    }

    @PostMapping("/setDifficulty")
    public String setDifficulty(@RequestParam(value = "difficulty", required = false) String difficulty) {
        switch (difficulty) {
            case "easy" -> field = new MinesField(7, 7, 2);
            case "medium" -> field = new MinesField(7, 12, 10);
            case "hard" -> field = new MinesField(12, 20, 20);
        }
        return "pages/games/MinesweeperTailwind";
    }

    @RequestMapping("/getMarked")
    public String getMarked() {
        StringBuilder sb = new StringBuilder();
        sb.append(field.getNumberOf(Tile.State.MARKED));
        sb.append("/");
        sb.append(field.getMineCount());
        return sb.toString();
    }

    @RequestMapping("/getOpen")
    public String getRemaining() {
        StringBuilder sb = new StringBuilder();
        sb.append(field.getNumberOf(Tile.State.OPEN));
        sb.append("/");
        sb.append(field.getRowCount() * field.getColumnCount() - field.getMineCount());
        return sb.toString();
    }

    ////////////////
    // methods

    private void statOrUpdateGame(Integer row, Integer column) throws ScoreException {

        if (field == null) {
            startNewGame();
        }
        if (row != null && column != null) {

            if(!checkInput(row, column)) return;

            GameState stateBeforeMove = field.getState();

            if(stateBeforeMove == GameState.PLAYING) {
                if(marking) field.markTile(row, column);
                else field.openTile(row, column);
            }

            if(field.getState() == GameState.SOLVED && stateBeforeMove != field.getState()) {
                Score score = new Score("MinesweeperTailwind", "player", field.getScore(), new Timestamp(System.currentTimeMillis()));
                scoreService.addScore(score);
            }

        }
    }

    public boolean isMarking() {
        return marking;
    }

    private void startNewGame() {
        field = new MinesField(7, 7, 2);
        marking = false;
    }

    public boolean checkInput(Integer row, Integer column) {
        boolean rowValid = row != null && row >= 0 && row < field.getRowCount();
        boolean columnValid = column != null && column >= 0 && column < field.getColumnCount();
        return rowValid && columnValid;
    }

    public boolean isGameOver() {
        return field.getState() == GameState.FAILED || field.getState() == GameState.SOLVED;
    }

    public boolean isGameWon() {
        return field.getState() == GameState.SOLVED;
    }

    public boolean isGameLost() {
        return field.getState() == GameState.FAILED;
    }

    public Tile[][] getFieldTiles() {
        return field.getTiles();
    }

    public int getAvgRating() {
        return ratingService.getAverageRating("MinesweeperTailwind");
    }

    public List<Score> getScores() throws ScoreException {
        return scoreService.getTopScores("MinesweeperTailwind");
    }

    public List<Comment> getComments() {
        return commentService.getComments("MinesweeperTailwind");
    }

    public int testNumber() {
        return 5;
    }


    ////////////////
    // render
    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='minefield-wrapper' >\n");
        sb.append("<table class='minefield'> \n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {

                if(field.getState() == GameState.PLAYING) {
                    var tile = field.getTile(row, column);
                    sb.append("<td class='").append(getTileClass(tile)).append("'>\n");
                    sb.append("<a href='/mines?row=").append(row).append("&column=").append(column).append("'>");
                    sb.append("<span>").append(getTileText(tile)).append("</span>");
                    sb.append("</a>\n");
                    sb.append("</td>\n");
                } else {
                    var tile = field.getTile(row, column);
                    sb.append("<td class='").append(getTileClass(tile)).append("'>\n");
                    sb.append("<span>").append(getTileText(tile)).append("</span>");
                    sb.append("</td>\n");

                }

            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");
        if(field.getState() != GameState.PLAYING) {
            if(field.getState() == GameState.FAILED) sb.append("<div class='overlay overlay-r'>You lost!</div>\n");
            if(field.getState() == GameState.SOLVED) sb.append("<div class='overlay overlay-g'>You won!</div>\n");
        }
        sb.append("</div>\n");
        return sb.toString();
    }

    public String getTileText(Tile tile) {
        return switch (tile.getState()) {
            case CLOSED -> "C";
            case MARKED -> "🚩";
            case OPEN -> tile instanceof Clue ? String.valueOf(((Clue) tile).getValue()) : "💣";
            default -> throw new IllegalArgumentException("Unsupported tile state " + tile.getState());
        };
    }

    public String getTileClass(Tile tile) {
        return switch (tile.getState()) {
            case OPEN -> tile instanceof Clue ? "tw-cell tw-cell-" + ((Clue) tile).getValue() : "mine";
            case CLOSED -> "tw-cell closed";
            case MARKED -> "tw-cell tw-cell-flagged";
            default -> throw new RuntimeException("Unexpected tile state");
        };
    }

}