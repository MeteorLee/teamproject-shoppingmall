package project.finalproject1backend.domain;

public enum OrderStatus {
    PURCHASING("결제 대기"),
    PURCHASED("결제 완료"),
    KAKAO_VERIFICATION_1("카카오 결제 검증1"),
    KAKAO_VERIFICATION_2("카카오 결제 검증2"),
    IAMPORT_VERIFICATION_1("아임포트 결제 검증1"),
    IAMPORT_VERIFICATION_2("아임포트 결제 검증2"),
    ERROR("결제 오류"),
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