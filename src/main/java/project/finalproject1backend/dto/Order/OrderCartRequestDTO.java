package project.finalproject1backend.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCartRequestDTO {
    private List<Long> cartItems = new ArrayList<>();
    private String deliveryName;
    private String deliveryZipcode;
    private String deliveryAddress;
    private String deliveryDetailedAddress;
    private String deliveryPhoneNumber;
    private String deliveryMemo;
    private String paymentMethod;
}
