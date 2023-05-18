package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.finalproject1backend.domain.Confirm;

import java.util.Optional;

public interface ConfirmRepository extends JpaRepository<Confirm,Long> {
    Optional<Confirm> findByEmail(String email);
}
