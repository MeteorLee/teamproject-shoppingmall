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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.Order.OrderCartRequestDTO;
import project.finalproject1backend.dto.Order.OrderRequestDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.service.OrderService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping

public class OrderController {

    private final OrderService orderService;

    @Tag(name = "API 상품구매 및 문의하기", description = "API 상품구매 및 문의하기 api입니다.")
    @Operation(summary = "단일 상품 주문하기", description = "상품 상세 페이지의 '구매하기' 버튼으로 상품을 주문하는 api입니다",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/directPurchasing")
    public ResponseEntity<?> directPurchasing(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                              @RequestBody @Valid OrderRequestDTO requestDTO,
                                              BindingResult bindingResult){
        return orderService.purchaseOne(principal,requestDTO);
    }
    @Tag(name = "API 상품구매 및 문의하기", description = "API 상품구매 및 문의하기 api입니다.")
    @Operation(summary = "장바구니 상품 주문하기", description = "장바구니에서 구매하기 버튼으로 상품을 주문하는 api입니다",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/cartPurchasing")
    public ResponseEntity<?> cartPurchasing(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                              @RequestBody @Valid OrderCartRequestDTO requestDTO,
                                              BindingResult bindingResult){
        return orderService.purchaseInCart(principal,requestDTO);
    }
}
