package project.finalproject1backend.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.Inquiry.SaleInquiry;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponseDTO {
    private String inquiryId;
    private String type;
    private String title;
    private String sub;
    private String state;

    public InquiryResponseDTO(BuyInquiry buyInquiry) {
        this.inquiryId=buyInquiry.getId();
        this.type = buyInquiry.getCategory();
        this.title = buyInquiry.getProduct();
        this.sub = buyInquiry.getAmount()+"";
        this.state = buyInquiry.getState().iterator().next().toString();
    }
    public InquiryResponseDTO(SaleInquiry saleInquiry) {
        this.inquiryId=saleInquiry.getId();
        this.type = "판매문의";
        this.title = saleInquiry.getCompany();
        this.sub = saleInquiry.getManufacturer();
        this.state = saleInquiry.getState().iterator().next().toString();
    }
}
