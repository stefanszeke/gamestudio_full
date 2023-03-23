package sk.tuke.gamestudio_backend.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    private String username;
    private String password;
    private String password2;
    private String email;
}
