package sk.tuke.gamestudio_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tuke.gamestudio_backend.entity.user.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
    User findByEmail(String email);
}
