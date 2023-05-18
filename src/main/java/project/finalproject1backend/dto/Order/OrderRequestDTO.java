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
    public String deliveryName;
    public String deliveryZipcode;
    public String deliveryAddress;
    public String deliveryDetailedAddress;
    public String deliveryPhoneNumber;
    public String deliveryMemo;
    private long productId;
    private int count;
    private String paymentMethod;

}
