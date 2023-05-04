package project.finalproject1backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.product.ProductFormDto;
import project.finalproject1backend.dto.user.UserInfoResponseDTO;
import project.finalproject1backend.service.ProductService;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping
public class ProductController {

    private final ProductService productService;

    @Tag(name = "API 상품", description = "상품/등록 api 입니다.")
    @Operation(summary = "상품등록 메서드", description = "상품등록 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProductFormDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/admin/products/new")
    public ResponseEntity<?> createProduct(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                           @RequestBody @Valid ProductFormDto productDto,
                                           BindingResult bindingResult,
                                           @RequestParam("productImgFile") List<MultipartFile> itemImgFileList) throws Exception {
        return productService.saveProduct(principal,productDto, itemImgFileList);
    }


    @Tag(name = "API 상품", description = "상품/수정 api 입니다.")
    @Operation(summary = "상품수정 메서드", description = "상품수정 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProductFormDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    // 상품수정
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<?> modify(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDTO principal,
                                           @PathVariable("productId") Long productId,
                                           @RequestBody ProductFormDto productDto,
                                           @RequestParam("productImgFile") List<MultipartFile> itemImgFileList) throws Exception {
        return productService.modify(principal,productId, productDto, itemImgFileList);
    }

    @Tag(name = "API 상품", description = "상품/삭제 api 입니다.")
    @Operation(summary = "상품삭제 메서드", description = "상품삭제 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/admin/products/{productId}/delete")
    public ResponseEntity<?> delete(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                    @PathVariable("productId") Long productId) {
        return productService.delete(principal,productId);
    }



    @Tag(name = "API 상품전체조회", description = "상품전체조회 api 입니다.")
    @Operation(summary = "상품전체조회 메서드", description = "상품전체조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/admin/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return productService.getAllProducts();
    }

    @Tag(name = "API 상품카테고리별조회", description = "상풍카테고리별조회 api 입니다.")
    @Operation(summary = "상품카테고리별조회 메서드", description = "상품카테고리별조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/admin/category")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam("mainCategory") MainCategory mainCategory,
                                                               @RequestParam("subCategory") String subCategory) {
        return productService.getProductsByCategory(mainCategory, subCategory);
    }

    @Tag(name = "API 상품랜덤5개조회", description = "상품랜덤5개조회 api 입니다.")
    @Operation(summary = "상품랜덤5개조회 메서드", description = "상품랜덤5개조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping(params = {"category"})
    public ResponseEntity<List<Product>> getProductByRandom(@RequestParam("subCategory") String subCategory) {

        return productService.getProductByRandom(subCategory);
    }


}
