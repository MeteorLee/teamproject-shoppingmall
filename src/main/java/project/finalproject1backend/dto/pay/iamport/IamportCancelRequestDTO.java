package project.finalproject1backend.dto.pay.iamport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IamportCancelRequestDTO {

    private String merchant_uid; // 가맹점 주문 번호
    private int cancel_amount; // 취소 금액

    // TODO: 2023-05-19 product 정보 추가 필요

}
