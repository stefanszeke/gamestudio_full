package sk.tuke.gamestudio_backend.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String username;

        private String password;

        @Column(unique = true)
        private String email;

        public User(String username, String hashedPassword, String email) {
                this.username = username;
                this.password = hashedPassword;
                this.email = email;
        }
}
