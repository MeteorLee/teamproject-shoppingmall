package project.finalproject1backend.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private String status; // 결제 성공 여부
}
