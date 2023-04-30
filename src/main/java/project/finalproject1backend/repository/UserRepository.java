package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.finalproject1backend.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUserId(String userId);
    Optional<User> findByUserId(String userId);
}
