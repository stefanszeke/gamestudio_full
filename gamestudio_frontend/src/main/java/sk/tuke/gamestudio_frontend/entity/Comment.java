package sk.tuke.gamestudio_frontend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String player;
    private String game;
    private String comment;

    @Column(name = "commentedon")
    private Timestamp commentedOn;

    public Comment(String player, String game, String comment, Timestamp commentedOn) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", player, comment);
    }
}
