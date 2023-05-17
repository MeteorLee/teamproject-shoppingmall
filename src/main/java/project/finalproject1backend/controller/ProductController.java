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
                                           @RequestPart("productImgFile") List<MultipartFile> itemImgFileList) throws Exception {
        return productService.createProduct(principal,productDto, itemImgFileList);
    }


//    @Tag(name = "API 상품", description = "상품/수정 api 입니다.")
//    @Operation(summary = "상품수정 메서드", description = "상품수정 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProductFormDto.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    // 상품수정
//    @PostMapping("/account/admin/product/{productId}")
//    public ResponseEntity<?> modify(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDTO principal,
//                                           @PathVariable("productId") Long productId,
//                                           @RequestBody ProductFormDto productDto,
//                                           @RequestParam("productImgFile") List<MultipartFile> itemImgFileList) throws Exception {
////        return productService.modify(principal,productId, productDto, itemImgFileList);
//        return null;
//    }
//
//    @Tag(name = "API 상품", description = "상품/삭제 api 입니다.")
//    @Operation(summary = "상품삭제 메서드", description = "상품삭제 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @PostMapping("/account/products/{productId}/delete")
//    public ResponseEntity<?> delete(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
//                                    @PathVariable("productId") Long productId) {
////        return productService.delete(principal,productId);
//        return null;
//    }
//
//
//
//    @Tag(name = "API 상품전체조회", description = "상품전체조회 api 입니다.")
//    @Operation(summary = "상품전체조회 메서드", description = "상품전체조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProductFormDto.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @GetMapping("/account/products")
//    public ResponseEntity<List<ProductFormDto>> getAllProducts() {
////        return productService.getAllProducts();
//        return null;
//    }
//
//    @Tag(name = "API 상품카테고리별조회", description = "상풍카테고리별조회 api 입니다.")
//    @Operation(summary = "상품카테고리별조회 메서드", description = "상품카테고리별조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @GetMapping("/account/products/category")
//    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam("mainCategory") MainCategory mainCategory,
//                                                               @RequestParam("subCategory") String subCategory) {
////        return productService.getProductsByCategory(mainCategory, subCategory);
//        return null;
//    }
//
//    @Tag(name = "API 상품랜덤5개조회", description = "상품랜덤5개조회 api 입니다.")
//    @Operation(summary = "상품랜덤5개조회 메서드", description = "상품랜덤5개조회 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @GetMapping(params = "/account/products/random")
//    public ResponseEntity<List<Product>> getProductByRandom(@RequestParam("subCategory") String subCategory) {
//
////        return productService.getProductByRandom(subCategory);
//        return null;
//    }
//
//    @GetMapping(params = "/account/product/{productId}")
//    public ResponseEntity<?> getProduct(@PathVariable("productId") Long productId, Model model) {
////        return productService.getProduct(productId);
//        return null;
//    }


}
