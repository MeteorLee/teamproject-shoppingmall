package project.finalproject1backend.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Orders extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 주문 ID

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 구매자

    @Setter
    private int totalPrice; // 결제 금액

    @Setter
    private String paymentMethod; // 결제 방식

    @Setter
    private long number; //

    @Setter
    private String deliveryName;

    @Setter
    private String deliveryPhoneNumber;

    @Setter
    private String deliveryMemo;

    @Setter
    private String deliveryZipCode;

    @Setter
    private String deliveryAddress;

    @Setter
    private String deliveryDetailedAddress;

    @Setter
    private String deliveryCompany;

    @Setter
    private int deliveryCharge;

    @Setter
    private long deliveryNumber;

    @Setter
    private OrderStatus status;

}
