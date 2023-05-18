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
    private String partner_order_id;
    @NotBlank
    private String partner_user_id;
    @NotBlank
    private String item_name;
    @NotBlank
    @Min(1)
    private int quantity;
    @Min(1)
    private int total_amount;

}
