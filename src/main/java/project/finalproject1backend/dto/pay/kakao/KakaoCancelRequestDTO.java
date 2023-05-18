package project.finalproject1backend.dto.pay.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoCancelRequestDTO {

    private String partner_order_id; // 가맹점 주문 번호
    private int cancel_amount; // 취소 금액
}
