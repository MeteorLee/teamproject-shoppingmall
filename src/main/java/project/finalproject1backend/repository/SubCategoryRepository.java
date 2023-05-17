package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.SubCategory;
import project.finalproject1backend.domain.constant.MainCategory;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    Optional<SubCategory> findByName(String name);

    List<SubCategory> findAllByMainCategory(MainCategory mainCategory);

//    @Query(value = "SELECT * FROM sub_category WHERE sub_category = :name ORDER BY RAND() LIMIT 5", nativeQuery = true)
//    List<Product> findRandomByName(@Param("subCategoryName") String subCategoryName);

}
