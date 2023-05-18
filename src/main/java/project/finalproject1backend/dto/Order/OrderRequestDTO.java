package project.finalproject1backend.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.Product;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private String deliveryName;
    private String deliveryZipcode;
    private String deliveryAddress;
    private String deliveryDetailedAddress;
    private String deliveryPhoneNumber;
    private String deliveryMemo;
    private long productId;
    private int count;
    private String paymentMethod;

}
