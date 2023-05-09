package project.finalproject1backend.domain.Inquiry;

import lombok.Getter;

@Getter
public enum BuyInquiryState {

    STATE_UNREAD("STATE_UNREAD"),
    STATE_REGISTER("STATE_REGISTER"),
    STATE_SAMPLE("STATE_SAMPLE"),
    STATE_APPROVAL("STATE_APPROVAL"),
    STATE_REFUND("STATE_REFUND"),
    STATE_DELIVERY("STATE_DELIVERY"),
    STATE_COMPLETED("STATE_COMPLETED");

    private final String state;

    BuyInquiryState(String state) {
        this.state = state;
    }

}
