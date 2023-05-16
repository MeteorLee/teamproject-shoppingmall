package project.finalproject1backend.dto.inquiry;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.Inquiry.SaleInquiryState;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleInquiryDTO {

    private String company;

    private String companyAddress;

    private String address;

    private String detailsAddress;

    private String manufacturer;

    private boolean mall;

    private String mallAddress;

}
