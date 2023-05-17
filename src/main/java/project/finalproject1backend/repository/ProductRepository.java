package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.constant.MainCategory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsById(Long id); //아이디 중복확인
    Optional<Product> findById(Long id); //아이디로 찾기




    List<Product> findByMainCategory(MainCategory category); //메인카테고리로 찾기

    List<Product> findByMainCategoryAndProductSubcategory(MainCategory mainCategory, String subCategoryName);
//    @Query(value = "SELECT * FROM product WHERE sub_category_name = :subCategoryName ORDER BY RAND() LIMIT 5", nativeQuery = true)
//    List<Product> findRandomBySubCategoryName(@Param("subCategoryName") String subCategoryName);
}
