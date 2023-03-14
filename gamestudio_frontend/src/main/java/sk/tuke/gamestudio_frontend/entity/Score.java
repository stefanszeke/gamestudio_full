package sk.tuke.gamestudio_frontend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "scores")
public class Score implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String game;


    private String player;


    private int points;

    private Timestamp playedOn;

    public Score(String game, String player, int points, Timestamp playedOn) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
        return String.format("[Name: %s | Score: %d | Game: %s | PlayedOn: %s ]", player, points, game, playedOn.toString().substring(0,10));
    }
}
