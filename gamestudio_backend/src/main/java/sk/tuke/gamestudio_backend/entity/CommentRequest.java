package sk.tuke.gamestudio_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    private String player;
    private String game;
    private String comment;

    public CommentRequest(String player, String game, String comment, Timestamp commentedOn) {
        this.player = player;
        this.game = game;
        this.comment = comment;
    }

}
