package project.finalproject1backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.ProductImg;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.product.ProductFormDto;
import project.finalproject1backend.repository.ProductImgRepository;
import project.finalproject1backend.repository.ProductRepository;
import project.finalproject1backend.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductImgService productImgService;
    private final ProductImgRepository productImgRepository;

    public ResponseEntity<?> saveProduct(PrincipalDTO principal, ProductFormDto productDto,List<MultipartFile> productImgFileList) throws Exception {
        Optional<User> user=userRepository.findById(principal.getId());
        if(!user.isPresent()){
            return new ResponseEntity<>(new ErrorDTO("400","notExistId"), HttpStatus.BAD_REQUEST);
        }

        //상품등록
        Product product = productDto.createProduct();
        productRepository.save(product);

        //이미지등록
        for(int i=0;i<productImgFileList.size();i++){
            ProductImg productImg = new ProductImg();
            productImg.setProduct(product);

            if(i == 0)
                productImg.setRepimgYn("Y");
            else
                productImg.setRepimgYn("N");

            productImgService.saveItemImg(productImg, productImgFileList.get(i));
        }

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }


    public ResponseEntity<?> modify(PrincipalDTO principal, Long productId, ProductFormDto productFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        Optional<User> user=userRepository.findById(principal.getId());
        if(!user.isPresent()){
            return new ResponseEntity<>(new ErrorDTO("400","notExistId"), HttpStatus.BAD_REQUEST);
        }
        Product id = productRepository.getReferenceById(productId);
        if(!id.getUser().getId().equals(principal.getId())) {
            return new ResponseEntity<>(new ErrorDTO("400","notMatchId"), HttpStatus.BAD_REQUEST);
        }
        try {
            // 상품 수정
            Product product = productRepository.findById(productFormDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
            product.updateProduct(productFormDto);

            // 이미지 수정
            List<Long> productImgIds = productFormDto.getProductImgIds();
            for (int i = 0; i < itemImgFileList.size(); i++) {
                productImgService.updateProductImg(productImgIds.get(i), itemImgFileList.get(i));
            }

            return ResponseEntity.ok(new ResponseDTO("200", "상품이 수정되었습니다."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorDTO("400", "해당 상품이 존재하지 않습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO("500", "서버 에러가 발생했습니다."));
        }

    }


    public ResponseEntity<?> delete(PrincipalDTO principal, Long productId) {
        Optional<User> user=userRepository.findById(principal.getId());
        if(!user.isPresent()){
            return new ResponseEntity<>(new ErrorDTO("400","notExistId"), HttpStatus.BAD_REQUEST);
        }
        Product id = productRepository.getReferenceById(productId);
        if(!id.getUser().getId().equals(principal.getId())) {
            return new ResponseEntity<>(new ErrorDTO("400","notMatchId"), HttpStatus.BAD_REQUEST);
        }
        productRepository.deleteById(productId);
        productImgRepository.deleteById(productId);
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);

    }

    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    public ResponseEntity<List<Product>> getProductsByCategory(MainCategory mainCategory, String subCategoryName) {
        List<Product> products = new ArrayList<>();

        if (StringUtils.hasText(subCategoryName)) {
            products = productRepository.findByMainCategoryAndSubCategoryName(mainCategory, subCategoryName);
        } else {
            products = productRepository.findByMainCategory(mainCategory);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    public ResponseEntity<List<Product>> getProductByRandom(String subCategory) {
        List<Product> products = productRepository.findRandomBySubCategoryName(subCategory);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
