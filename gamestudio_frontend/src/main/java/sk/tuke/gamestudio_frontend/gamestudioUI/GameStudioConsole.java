package sk.tuke.gamestudio_frontend.gamestudioUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import sk.tuke.gamestudio_frontend.entity.Score;
import sk.tuke.gamestudio_frontend.entity.Comment;
import sk.tuke.gamestudio_frontend.entity.Rating;
import sk.tuke.gamestudio_frontend.games.Blackjack.Blackjack;
import sk.tuke.gamestudio_frontend.games.Blocks.Blocks;
import sk.tuke.gamestudio_frontend.games.Jewels.Jewels;
import sk.tuke.gamestudio_frontend.games.Puzzle.Puzzle;
import sk.tuke.gamestudio_frontend.games.Tictactoe.Tictactoe;
import sk.tuke.gamestudio_frontend.games.interfaces.Game;
import sk.tuke.gamestudio_frontend.games.minesweeper.Minesweeper;
import sk.tuke.gamestudio_frontend.service.ApiServiceWebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.util.*;


@Component
@ComponentScan("sk.tuke.gamestudio_frontend")
public class GameStudioConsole {
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    @Autowired private sk.tuke.gamestudio_frontend.interfaces.ScoreService scoreService;
    @Autowired private sk.tuke.gamestudio_frontend.interfaces.CommentService commentService;
    @Autowired private sk.tuke.gamestudio_frontend.interfaces.RatingService ratingService;

    @Autowired private Minesweeper minesweeper;
    @Autowired private Tictactoe tictactoe;
    @Autowired private Blackjack blackjack;
    @Autowired private Blocks blocks;
    @Autowired private Jewels jewels;
    @Autowired private Puzzle puzzle;

    @Autowired private ApiServiceWebClient apiServiceWebClient;

    static List<String> games = new ArrayList<>();
    private final Map<String, Game> gamesmap = new LinkedHashMap<>();

    // main
    public void run1() {
        while(true) {
            printSelection();
            String choice = readLine();
            switch (choice) {
                case "1" -> minesweeper.run();
                case "2" -> tictactoe.run();
                case "3" -> blackjack.run();
                case "4" -> blocks.run();
                case "5" -> jewels.run();
                case "6" -> puzzle.run();

                case "s" -> showScore();
                case "d" -> apiTest();
                case "0" -> exitGameStudio();
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public void run2() {
        printSelection();
        String input = readLine();
        try {
            if(input.equals("0")) exitGameStudio();
            if(input.equals("s")) showScore();
            if(input.equals("d")) apiTest();

            Class clazz = Class.forName("sk.tuke.gamestudio_frontend.games.minesweeper.Minesweeper");
            Constructor<?> c = clazz.getConstructor();
            Game game = (Game) c.newInstance();
            game.run();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void run3() {
        loadGameslist();
        while (true) {
            try {
                printSelection();
                String input = readLine();
                if (input.equals("0")) exitGameStudio();
                else if (input.equals("s")) showScore();
                else if (input.equals("r")) apiTest();
                else if (input.equals("w")) webClientTest();
                else {
                    String gameName = games.get(Integer.parseInt(input) - 1);
                    Game game = gamesmap.get(gameName);
                    game.run();
                }

            } catch (Exception e) {
                System.out.println("** Invalid input **\n");
                run3();
            }
        }
    }

    private static String readLine() {
        try {
            return input.readLine().toLowerCase();
        } catch (IOException e) {
            return null;
        }
    }

    // /////////////////////////////////////////////
    private void  loadGameslist() {
        gamesmap.clear();
        games.clear();
        gamesmap.put("Minesweeper", minesweeper);
        gamesmap.put("Tictactoe", tictactoe);
        gamesmap.put("Blackjack", blackjack);
        gamesmap.put("Blocks", blocks);
        gamesmap.put("Jewels", jewels);
        gamesmap.put("Puzzle", puzzle);
        Set<String> gamesSet = gamesmap.keySet();
        games.addAll(gamesSet);
    }

    private void printSelection() {
        System.out.println("\n***************************");
        System.out.println("GAME STUDIO => Select game:\n");
        printGameList();
        System.out.println("\ns. Show High Scores");
        System.out.println("\nr. rest Services test");
        System.out.println("w. WebClient test");
        System.out.println("\n0. Exit");
        System.out.print("Enter your choice: ");
    }

    private void showScore() {
        List<Score> scoreList = new ArrayList<>();
        while(true) {
            try {
                System.out.println("\nSelect Game to show score");
                printGameList();
                System.out.println("\n0. back");
                System.out.println("Enter:");

                String choice = readLine();

                if (choice.equals("0")) {
                    System.out.println("back");
                    return;
                }
                if (Integer.parseInt(choice) > games.size() || Integer.parseInt(choice) < 0) {
                    System.out.println("Invalid choice!");
                } else {
                    scoreList = scoreService.getTopScores(games.get(Integer.parseInt(choice) - 1));

                    System.out.println("Top scores:");
                    for (Score score : scoreList) {
                        System.out.println(score);
                    }
                }
            } catch (Exception e) {
                System.out.println("wrong input type !");
            }
        }
    }

    private static void printGameList() {
        for(int i = 0; i < games.size(); i++) {
            System.out.println((i+1) + ". " + games.get(i));
        }
    }

    private static void exitGameStudio() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private void apiTest() {
        try {
            System.out.println("\n...\n");

            System.out.println("*** score service ***");
            scoreService.addScore(new Score("Minesweeper", "Janko", 100, new Timestamp(System.currentTimeMillis())));
            scoreService.addScore(new Score("Minesweeper", "Janko2", 200, new Timestamp(System.currentTimeMillis())));
            scoreService.addScore(new Score("Minesweeper", "Janko3", 300, new Timestamp(System.currentTimeMillis())));

            List<Score> scoreList = scoreService.getTopScores("Minesweeper");
            scoreList.forEach(System.out::println);

            System.out.println("\n*** comment service ***");
            commentService.addComment(new Comment("Jack", "Minesweeper", "minesweeper is cool", new Timestamp(System.currentTimeMillis())));
            commentService.addComment(new Comment("john", "Minesweeper", "first", new Timestamp(System.currentTimeMillis())));

            List<Comment> commentList = commentService.getComments("Minesweeper");
            commentList.forEach(System.out::println);

            System.out.println("\n*** rating service ***");
            ratingService.setRating(new Rating("Jack", "Minesweeper", 5, new Timestamp(System.currentTimeMillis())));
            ratingService.setRating(new Rating("John", "Minesweeper", 3, new Timestamp(System.currentTimeMillis())));
            ratingService.setRating(new Rating("Jill", "Minesweeper", 1, new Timestamp(System.currentTimeMillis())));

            int avgRating = ratingService.getAverageRating("Minesweeper");
            System.out.println("AVG Minesweeper ratings: " + avgRating);

            int jacksRating = ratingService.getRating("Minesweeper", "Jack");
            int jackzsRating = ratingService.getRating("Minesweeper", "Jackz");
            System.out.println("Jack's rating: " + jacksRating);
            System.out.println("Jackz's rating: " + jackzsRating);


            System.out.println("\n...\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void webClientTest() {
        System.out.println("\n...\n");
        try {

            System.out.println("\nrest webclient:");
            Flux<Score> scoreFlux = apiServiceWebClient.getTopScoresByGame("Minesweeper");

            scoreFlux.subscribe(System.out::println);
            scoreFlux.blockLast();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\n...\n");
    }

}
