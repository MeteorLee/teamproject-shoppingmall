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

    @NotBlank(message = "결제 오류")
    private String imp_uid;
    @NotBlank(message = "결제 오류")
    private String merchant_uid;

}
