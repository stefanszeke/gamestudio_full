package sk.tuke.gamestudio_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private String player;
    private String game;
    private String comment;
}
