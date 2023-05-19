package project.finalproject1backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<User> findByRole(UserRole role, Pageable pageable);
    List<User> findByCompanyName(String companyName);
    Page<User> findByCompanyName(String companyName, Pageable pageable);
    Page<User> findByCompanyNameAndRole(String companyName,UserRole role, Pageable pageable);
    List<User> findByManagerName(String managerName);
    Page<User> findByManagerName(String managerName, Pageable pageable);
    Page<User> findByManagerNameAndRole(String managerName,UserRole role, Pageable pageable);

}
