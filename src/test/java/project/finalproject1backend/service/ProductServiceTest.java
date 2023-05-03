package project.finalproject1backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.constant.ItemSellStatus;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.product.ProductFormDto;
import project.finalproject1backend.repository.ProductImgRepository;
import project.finalproject1backend.repository.ProductRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
            ProductRepository productRepository;

    @Autowired
    ProductImgRepository productImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception{

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0;i<5;i++){
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveProduct() throws Exception {
        ProductFormDto productFormDto = new ProductFormDto();
        productFormDto.setProductName("테스트상품");
        productFormDto.setItemSellStatus(ItemSellStatus.SELL);
        productFormDto.setProductPrice(1000);
        productFormDto.setStockNumber(100);
        productFormDto.setMaincategory(MainCategory.BATHROOM_SUPPLIES);


        List<MultipartFile> multipartFileList = createMultipartFiles();
        ResponseEntity<?> responseEntity = productService.saveProduct(productFormDto, multipartFileList);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ResponseDTO);
        ResponseDTO responseDTO = (ResponseDTO) responseEntity.getBody();
        assertEquals("200", responseDTO.getCode());

    }

    private void assertEquals(String s, String code) {
    }

    private void assertEquals(HttpStatus ok, HttpStatus statusCode) {
    }
}