package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.finalproject1backend.domain.SubCategory;
import project.finalproject1backend.domain.constant.MainCategory;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    Optional<SubCategory> findByName(String name);
    List<SubCategory> findAllByMainCategory(MainCategory mainCategory);
}
