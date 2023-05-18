package project.finalproject1backend.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
/*
”company” :“기업명”
”companyAddress” : “본사 주소”
”address” :”기본 주소”
”detailsAddress” :“상세 주소”
”manufacturer” :”제조사”
”mall” :”자사 쇼핑몰 운영여부”
”mallAddress” :”자사 쇼핑몰 주소”
”saleAttachmentList” :”첨부파일”
 */
