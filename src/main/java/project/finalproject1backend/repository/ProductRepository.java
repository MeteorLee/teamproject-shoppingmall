package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.constant.MainCategory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsById(Long id); //아이디 중복확인
    Optional<Product> findById(Long id); //아이디로 찾기

    @Query(value = "SELECT * FROM product ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Product> findRandomThreeProducts();    //랜덤으로 3개 상품 뽑기

    List<Product> findByMainCategory(MainCategory mainCategory); //메인카테고리로 찾기
    List<Product> findBySubCategoryName(String subCategoryName); //서브카테고리로 찾기
    List<Product> findByDetailCategoryName(String detailCategoryName); //디테일카테고리로 찾기

    List<Product> findByKeyword(String keyword, String category);  //키워드로 찾기

    default List<Product> findByKeywordAndCategory(String keyword, String category) {
        switch (category) {
            case "대분류":
                return findByMainCategory(MainCategory.valueOf(keyword));
            case "중분류":
                return findBySubCategoryName(keyword);
            case "소분류":
                return findByDetailCategoryName(keyword);
            default:
                throw new IllegalArgumentException("잘못된 카테고리입니다.");
        }
    }



}
