package project.finalproject1backend.domain;

public enum OrderStatus {
    PURCHASING("결제 대기"),
    PURCHASED("결제 완료"),
    DELIVERING("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("주문 취소");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}