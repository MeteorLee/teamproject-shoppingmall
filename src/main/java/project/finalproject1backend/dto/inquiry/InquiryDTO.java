package project.finalproject1backend.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDTO {

    private String category;

    private String product;

    private int amount;

    private String content;

    private LocalDate estimateWishDate;

    private LocalDate deliveryWishDate;


    private String company;

    private String companyAddress;

    private String address;

    private String detailsAddress;

    private String manufacturer;

    private boolean mall;

    private String mallAddress;

    public InquiryDTO(BuyInquiryDTO buyInquiryDTO, SaleInquiryDTO saleInquiryDTO) {
        this.category = category;
        this.product = product;
        this.amount = amount;
        this.content = content;
        this.estimateWishDate = estimateWishDate;
        this.deliveryWishDate = deliveryWishDate;
        this.company = company;
        this.companyAddress = companyAddress;
        this.address = address;
        this.detailsAddress = detailsAddress;
        this.manufacturer = manufacturer;
        this.mall = mall;
        this.mallAddress = mallAddress;
    }
}
