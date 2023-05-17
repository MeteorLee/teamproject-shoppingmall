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
    @Query(value = "SELECT * FROM product p WHERE p.productSubcategory.category = :subCategory ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Product> findRandomProductsBySubCategory(String subCategory);
//    @Query(value = "SELECT * FROM sub_category  ORDER BY RAND() LIMIT 5", nativeQuery = true)
//    List<Product> findRandomByName(@Param("subCategoryName") String subCategoryName);

}
