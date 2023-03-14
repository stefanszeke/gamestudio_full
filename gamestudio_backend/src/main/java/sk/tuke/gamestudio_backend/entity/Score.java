package sk.tuke.gamestudio_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "scores")
public class Score implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String game;

    @NotNull
    @Size(min = 0, max = 50, message = "Player name must be between 0 and 50 characters long.")
    private String player;

    @Min(value = 0, message = "Points must be greater than 0.")
    private int points;

    @Column(name = "playedon")
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
