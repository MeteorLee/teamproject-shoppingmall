package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUserId(String userId);
    Optional<User> findByUserId(String userId);
    List<User> findByEmail(String email);

    List<User> findByRole(UserRole role);
    List<User> findByCompanyName(String companyName);
    List<User> findByManagerName(String managerName);
}
