package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.finalproject1backend.domain.ProductImg;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

    List<ProductImg> findByProductIdOrderByIdAsc(Long productId);

    ProductImg findByProductIdAndRepimgYn(Long productId, String repimgYn);

}
