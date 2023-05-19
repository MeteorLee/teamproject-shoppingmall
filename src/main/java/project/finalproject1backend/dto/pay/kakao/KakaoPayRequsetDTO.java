package project.finalproject1backend.dto.pay.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayRequsetDTO {

    @NotBlank
    private String partner_order_id; // 가맹점 주문 번호
    @Min(1)
    private int total_amount; // 상품 총액

    // TODO: 2023-05-19 product 정보 추가 필요

}
