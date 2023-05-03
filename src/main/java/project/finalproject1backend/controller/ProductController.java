package project.finalproject1backend.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.product.ProductFormDto;
import project.finalproject1backend.service.ProductService;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping
public class ProductController {

    private final ProductService productService;


    // 상품등록
    @PostMapping("/admin/products/new")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductFormDto productDto,
                                           BindingResult bindingResult,
                                           @RequestParam("productImgFile") List<MultipartFile> itemImgFileList) throws Exception {
        return productService.saveProduct(productDto, itemImgFileList);
    }

    // 상품수정
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<?> updateProduct(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDTO principal,
                                           @PathVariable("productId") Long productId,
                                           @RequestBody ProductFormDto productDto,
                                           @RequestParam("productImgFile") List<MultipartFile> itemImgFileList) throws Exception {
        return productService.updateProduct(principal,productId, productDto, itemImgFileList);
    }

    // 상품삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        // TODO: 상품 삭제 로직 구현
        return ResponseEntity.ok().build();
    }

    // 상품재고수정
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Void> updateProductStock(@PathVariable("productId") Long productId,
                                                   @RequestParam("stock") int stock) {
        // TODO: 상품 재고 수정 로직 구현
        return ResponseEntity.ok().build();
    }

    // 상품판매여부수정
    @PatchMapping("/{productId}/sell")
    public ResponseEntity<Void> updateProductSellStatus(@PathVariable("productId") Long productId,
                                                        @RequestParam("sell") boolean sell) {
        // TODO: 상품 판매 여부 수정 로직 구현
        return ResponseEntity.ok().build();
    }

    // 전체조회
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        // TODO: 전체 상품 조회 로직 구현
        return ResponseEntity.ok().build();
    }

    // 카테고리별조회
    @GetMapping(params = {"category"})
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam("category") String category) {
        // TODO: 카테고리별 상품 조회 로직 구현
        return ResponseEntity.ok().build();
    }

    // TO DO : 이미지로직 확인


}
