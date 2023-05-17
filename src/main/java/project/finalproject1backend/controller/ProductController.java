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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.product.ProductFormDto;
import project.finalproject1backend.dto.subCategory.SubCategoryRequestDTO;
import project.finalproject1backend.dto.subCategory.SubCategoryResponseDTO;
import project.finalproject1backend.service.ProductService;


import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping
public class ProductController {

    private final ProductService productService;

    @Tag(name = "관리자 페이지 (상품관련)", description = "관리자 페이지 (상품관련)")
    @Operation(summary = "서브카테고리 생성 메서드", description = "등록되어 있는 MainCategory GUEST_ROOM_SUPPLIES(\"객실용품\"),\n" +
            "    BATHROOM_SUPPLIES(\"욕실용품\"),\n" +
            "    BEVERAGE(\"식음료\"),\n" +
            "    HYGIENE_SUPPLIES(\"위생용품\"),\n" +
            "    BEDDING(\"침구류\"),\n" +
            "    SAFETY_EQUIPMENT(\"소방/안전설비\"),\n" +
            "    ELECTRONIC_APPLIANCES(\"전자제품\"),\n" +
            "    CLEANING_FACILITY_MANAGEMENT(\"청소/시설관리\"),\n" +
            "    OFFICE_SUPPLIES(\"사무용품\")",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/admin/product/createSubCategory")
    public ResponseEntity<?> createSubCategory(@RequestBody @Valid SubCategoryRequestDTO requestDTO,
                                               BindingResult bindingResult){
        return productService.createSubCategory(requestDTO);
    }

    @Tag(name = "관리자 페이지 (상품관련)", description = "관리자 페이지 (상품관련)")
    @Operation(summary = "서브카테고리 조회 메서드", description = "등록되어 있는 MainCategory GUEST_ROOM_SUPPLIES(\"객실용품\"),\n" +
            "    BATHROOM_SUPPLIES(\"욕실용품\"),\n" +
            "    HYGIENE_SUPPLIES(\"위생용품\"),\n" +
            "    BEDDING(\"침구류\"),\n" +
            "    ELECTRONIC_APPLIANCES(\"전자제품\"),\n" +
            "    CLEANING_FACILITY_MANAGEMENT(\"청소/시설관리\");",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SubCategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/account/admin/product/getSubCategory")
    public ResponseEntity<?> getSubCategory(@RequestParam MainCategory mainCategory){
        return productService.getSubCategory(mainCategory);
    }

    @Tag(name = "관리자 페이지 (상품관련)", description = "관리자 페이지 (상품관련)")
    @Operation(summary = "상품등록 메서드", description = "상품등록 메서드입니다. (등록되어 있는 subcategory일 경우 product 등록, " +
            "등록되어 있지 않은 subcategory일 경우 ,error 리턴)",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping(value = "/account/admin/product/save",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                           @RequestPart @Valid ProductFormDto productDto,
                                           BindingResult bindingResult,
                                           @RequestPart(value="productImgFile",required = false) List<MultipartFile> itemImgFileList) throws Exception {
        return productService.createProduct(principal,productDto, itemImgFileList);
    }

//    @Tag(name = "관리자 페이지 (상품관련)", description = "관리자 페이지 (상품관련)")
//    @Operation(summary = "추천 TRUE/FALSE 변경 메서드", description = "추천변경 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @PostMapping("/account/admin/recommended/{productId}")
//    public ResponseEntity<?> setProductRecommended(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
//                                                   @PathVariable Long productId) {
//        return productService.setProductRecommended(principal,productId);
//    }



//    @Tag(name = "API 상품", description = "상품 api 입니다.")
//    @Operation(summary = "상품수정 메서드", description = "상품수정 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProductFormDto.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @PostMapping("/account/admin/product/{productId}")
//    public ResponseEntity<?> modify(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDTO principal,
//                                           @PathVariable("productId") Long productId,
//                                           @RequestBody ProductFormDto productDto,
//                                           @RequestPart(value="productImgFile",required = false) List<MultipartFile> itemImgFileList) throws Exception {
//        return productService.modify(principal,productId, productDto, itemImgFileList);
//    }
//
//    @Tag(name = "API 상품", description = "상품 api 입니다.")
//    @Operation(summary = "상품삭제 메서드", description = "상품삭제 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @PostMapping("/account/product/{productId}/delete")
//    public ResponseEntity<?> delete(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
//                                    @PathVariable("productId") Long productId) {
//        return productService.delete(principal,productId);
//    }


    @Tag(name = "API 상품조회", description = "상품 조회 api 입니다.")
    @Operation(summary = "상품 전체항목 조회 메서드", description = "상품 전체 조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProductFormDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/account/products/all")
    public ResponseEntity<Page<ProductFormDto>> getAllProducts(@PageableDefault(size = 24, sort = "id", direction = Sort.Direction.DESC) Pageable pageable     ) {
         return productService.getAllProducts(pageable);
    }

//    @Tag(name = "API 상품조회", description = "상품 조회 api 입니다.")
//    @Operation(summary = "상품 랜덤 5개조회 메서드", description = "상품 랜덤 5개조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProductFormDto.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @GetMapping("/account/products/random")
//    public ResponseEntity<List<Product>> getProductByRandom(@RequestParam("subCategory") String subCategory) {
//
//        return productService.getProductByRandom(subCategory);
//    }

    @Tag(name = "API 상품조회", description = "상품 조회 api 입니다.")
    @Operation(summary = "상품id별 상세 조회 메서드", description = "상품 상세 조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProductFormDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/account/products/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") Long productId) {
        return productService.getProduct(productId);
    }

    @Tag(name = "API 상품조회", description = "상품 조회 api 입니다.")
    @Operation(summary = "상품명,판매가,소매가,추천여부항목만 조회 메서드", description = "상품 요약 조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProductFormDto.ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/account/products/summar")
    public ResponseEntity<?> getProductDetails() {
        return productService.getProductDetails();
    }


}
