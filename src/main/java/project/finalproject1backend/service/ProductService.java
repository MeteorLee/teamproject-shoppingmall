package project.finalproject1backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.ProductImg;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.product.ProductFormDto;
import project.finalproject1backend.repository.ProductImgRepository;
import project.finalproject1backend.repository.ProductRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImgService productImgService;
    private final ProductImgRepository productImgRepository;

    public ResponseEntity<?> saveProduct( ProductFormDto productDto,List<MultipartFile> productImgFileList) throws Exception {
        if(productRepository.existsById(productDto.getId())) {
            return new ResponseEntity<>(new ErrorDTO("400","existId"), HttpStatus.BAD_REQUEST);
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



    public ResponseEntity<?>  updateProduct(PrincipalDTO principal, Long productId, ProductFormDto productFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        Product id = productRepository.getReferenceById(productId);
        if(id == null) {
                return new ResponseEntity<>(new ErrorDTO("400","notExistId"), HttpStatus.BAD_REQUEST);
            }
        if(!id.getUser().getId().equals(principal.getId())) {
                return new ResponseEntity<>(new ErrorDTO("400","notMatchId"), HttpStatus.BAD_REQUEST);
        }
            //상품 수정
            Product product = productRepository.findById(productFormDto.getId())
                    .orElseThrow(EntityNotFoundException::new);
            product.updateProduct(productFormDto);


            //이미지 수정
            List<Long> productImgIds = productFormDto.getProductImgIds();
            for (int i = 0; i < itemImgFileList.size(); i++) {
                productImgService.updateProductImg(productImgIds.get(i),
                        itemImgFileList.get(i));
            }
             return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);

    }


}
