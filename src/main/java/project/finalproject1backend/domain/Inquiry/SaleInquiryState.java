package project.finalproject1backend.domain.Inquiry;

import lombok.Getter;

@Getter
public enum SaleInquiryState {

    STATE_UNREAD("STATE_UNREAD"),
    STATE_UNANSWERED("STATE_UNANSWERED"),
    STATE_COMPLETED("STATE_COMPLETED");

    private final String state;

    SaleInquiryState(String state) {
        this.state = state;
    }

}
