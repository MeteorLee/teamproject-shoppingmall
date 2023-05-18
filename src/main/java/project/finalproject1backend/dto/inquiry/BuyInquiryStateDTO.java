package project.finalproject1backend.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.Inquiry.BuyInquiryState;
import project.finalproject1backend.domain.User;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyInquiryStateDTO {

    private User buyInquiryId;

    private Set<BuyInquiryState> state = new HashSet<>();

//    public BuyInquiryStateDTO() {
//        this.buyInquiryId = buyInquiryId;
//        this.state = state;
//    }
}
