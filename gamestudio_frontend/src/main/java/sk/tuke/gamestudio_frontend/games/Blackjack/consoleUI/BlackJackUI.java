package sk.tuke.gamestudio_frontend.games.Blackjack.consoleUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import sk.tuke.gamestudio_frontend.entity.Score;
import sk.tuke.gamestudio_frontend.games.Blackjack.core.Card;
import sk.tuke.gamestudio_frontend.games.Blackjack.core.Player;

import sk.tuke.gamestudio_frontend.games.Blackjack.core.Table;
import sk.tuke.gamestudio_frontend.games.interfaces.GameField;
import sk.tuke.gamestudio_frontend.games.interfaces.UserInterface;
import sk.tuke.gamestudio_frontend.service.interfaces.ScoreService;
import sk.tuke.gamestudio_frontend.service.other.ScoreException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

@Component
@ComponentScan("sk.tuke.gamestudio")
public class BlackJackUI implements UserInterface {

    @Autowired
    private ScoreService scoreService;

    private Table table;
    private int score = 0;
    private int round = 1;
    private Boolean playerTurn = true;
    private Boolean dealerTurn = true;
    private Boolean playerWin = false;
    private Boolean exitRequested = false;

    private Scanner scanner = new Scanner(System.in);

    // main loop
    public void newGameStarted(GameField table) {
        this.table = (Table) table;
        mainLoop();
    }

    public void mainLoop() {
        table.firstDeal();

        Instant start = Instant.now();

        do {
            System.out.println("\n------------ [Round: "+round+"] -----------");
            while (playerTurn) {
                playerTurn();
            }
            while(dealerTurn && !exitRequested) {
                dealerTurn();
            }
            if(playerWin) setNextRound();

        } while (playerWin && !exitRequested);

        Instant end = Instant.now();
        Duration time = Duration.between(start, end);

        if(!exitRequested) {
            showAndSetScore(time);
        }
        resetGame();
    }

    public void update() {
        System.out.println("\n------------ [SCORE: "+score+"] ------------");
        for(int i = 0; i < table.getDealer().getCards().size(); i++) {
            if(i == 0 && playerTurn) {
                System.out.println("Dealer: [Hidden Card]");
            } else {
                System.out.println("Dealer: " + table.getDealer().getCards().get(i).toString());
            }
        }
        System.out.println();
        for(Card p: table.getPlayer().getCards()) {
            System.out.println("Player: " + p.toString());
        }
        System.out.println("-----------------------------------");
    }

    private void resetGame() {
        table.tableReset();
        score = 0;
        round = 1;
        playerTurn = true;
        dealerTurn = true;
        playerWin = false;
        exitRequested = false;
    }

    public String processInput() {
        System.out.println("Hit or Stand? (h/s), x to exit");
        String playerInput = scanner.nextLine().toLowerCase();
        Boolean isValid = false;

        while (!isValid) {
            if(playerInput.equals("h")) {
                System.out.println("Player HIT");
                table.hit(table.getPlayer());
                isValid = true;

            } else if(playerInput.equals("s")) {
                System.out.println("Player STAND");
                isValid = true;
            } else if (playerInput.equals("x")) {
                System.out.println("Exitign ...");
                exitRequested = true;
                isValid = true;
            }
            else {
                System.out.println("Invalid input. Try again. Hit or Stand? (h/s), x to exit");
                playerInput = scanner.nextLine().toLowerCase();
            }
        }
        return playerInput;
    }

    private void playerTurn() {
        update();
        if (table.isBlackJack(table.getPlayer())) {
            System.out.println("BlackJack! You win!");
            playerTurn = false;
            dealerTurn = false;
            playerWin = true;
        } else {
            String playerInput = processInput();
            if (playerInput.equals("s")) {
                playerTurn = false;
            } else if (playerInput.equals("x")) {
                playerTurn = false;
                dealerTurn = false;

                return;
            }
            if (table.isBust(getPlayer())) {
                update();
                System.out.println("Bust! You lose!");
                playerTurn = false;
                dealerTurn = false;
                playerWin = false;
            }
        }
    }

    private void dealerTurn() {
        update();
        if(getDealer().getTotal() > getPlayer().getTotal()) {
            System.out.println("Dealer won");
            dealerTurn = false;
            playerWin = false;
        } else {
            System.out.println("Dealer HIT");
            table.hit(getDealer());
            if(table.isBust(getDealer())) {
                update();
                System.out.println("Dealer bust, Player WON");
                dealerTurn = false;
                playerWin = true;
            }
        }
    }

    private void showAndSetScore(Duration time) {
        try {
        int timeScore = (int)time.toMillis()/4000;

        String timeFormat = new SimpleDateFormat("HH:mm:ss").format(time.toMillis()-3600000);

        System.out.println("--------- [Game Over] ---------");
        System.out.println("--------- "+timeFormat+" ---------");
        int finalScore = Math.max((score) - timeScore, 0);

        System.out.println("------ [Final score: "+finalScore+"]------");

        if(finalScore > 0) {
            Score newScore = new Score("Blackjack","player", finalScore, new Timestamp(System.currentTimeMillis()));
            scoreService.addScore(newScore);
        }

        System.out.println("Top Score:");
        List<Score> scores = scoreService.getTopScores("Blackjack");
        for(Score s: scores) System.out.println(s);
        } catch (ScoreException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }

    private Player getPlayer() {
        return  this.table.getPlayer();
    }
    private Player getDealer() {
        return  this.table.getDealer();
    }

    private void setNextRound() {
        score = score == 0 ? 2 : score*2;
        playerTurn = true;
        dealerTurn = true;
        table.tableReset();
        table.firstDeal();
        round++;
    }

}
