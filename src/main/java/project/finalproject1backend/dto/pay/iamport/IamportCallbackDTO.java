package project.finalproject1backend.dto.pay.iamport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IamportCallbackDTO {

    @NotBlank
    private String imp_uid; // 고유 주문 번호
    @NotBlank
    private String merchant_uid; // 가맹점 주문 번호
    // TODO: 2023-05-19 product 정보 추가 필요

}
