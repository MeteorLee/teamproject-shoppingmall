package project.finalproject1backend.dto.inquiry;

import lombok.*;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.Inquiry.SaleInquiry;
import project.finalproject1backend.domain.Inquiry.SaleInquiryState;
import project.finalproject1backend.dto.AttachmentDTO;
import project.finalproject1backend.util.Encrypt256;

import javax.persistence.Convert;
import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleInquiryResponseDTO {
    private String inquiryId;
    private String email;
    private String phoneNumber;
    private String company;
    private String companyAddress;
    private String manufacturer;
    private String mallAddress;
    private SaleInquiryState state;
    private List<AttachmentDTO> saleAttachmentList;
    public SaleInquiryResponseDTO(SaleInquiry s) {
        this.inquiryId = s.getId();
        this.email = s.getSaleInquiryId().getEmail();
        this.phoneNumber = s.getSaleInquiryId().getPhoneNumber();
        this.company = s.getCompany();
        this.companyAddress = s.getAddress()+" "+s.getDetailsAddress();
        this.manufacturer = s.getManufacturer();
        this.mallAddress = s.getMallAddress();
        this.state = s.getState().iterator().next();
        this.saleAttachmentList = s.getSaleAttachmentList().stream().map(AttachmentDTO::new).toList();
    }
}
/*
- 아이디
	- 이메일
	- 연락처
	- 기업명
	- 본사주소
	- 제조사
	- 쇼핑몰주소 (없으면 null)
	- 처리상태
	- 제품등록파일 (없으면 null)
 */