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
public class KakaoCancelRequestDTO {

    @NotBlank
    private String partner_order_id; // 가맹점 주문 번호
    @NotBlank
    @Min(0)
    private int cancel_amount; // 취소 금액
    // TODO: 2023-05-19 product 정보 추가 필요
}
