package project.finalproject1backend.dto.inquiry;

import lombok.*;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.Inquiry.BuyInquiryState;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.UploadDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyInquiryResponseDTO {

    private String userId;

    private String email;

    private String phoneNumber;

    private String companyName;

    private String category;

    private String product;

    private int amount;

    private List<buyImageListInfo> buyImageList = new ArrayList<>();

    private String content;

    private LocalDate estimateWishDate;

    private LocalDate deliveryWishDate;

    private Set<BuyInquiryState> state = new HashSet<>();

    @Getter
    @Setter
    public static class buyImageListInfo{
        private String fileName;
        private String filePath;
        private String originalFileName;

        public buyImageListInfo(AttachmentFile a) {
            this.fileName = a.getFileName();
            this.filePath = a.getFilePath();
            this.originalFileName = a.getOriginalFileName();
        }
    }

//    private List<BuyInquiry> buyInquiry;



//    public BuyInquiryResponseDTO(PrincipalDTO dto) {
//        this.userId = dto.getUserId();
//        this.email = dto.getEmail();
//        this.phoneNumber = dto.getPhoneNumber();
//        this.companyName = dto.getCompanyName();
//        category = dto.getBuyInquiry().g;
//        this.product = dto.pro;
//        this.amount = dto.;
//        this.buyImageList = buyImageList;
//        this.content = content;
//        this.estimateWishDate = estimateWishDate;
//        this.deliveryWishDate = deliveryWishDate;
//        this.state = state;
//    }
}
/*
=> 견적문의 전체조회 api
	- 구분(대량구매, 주문제작)
	- 아이디 0
	- 이메일 0
	- 연락처 0
	- 기업명 0
	- 구매희망제품명 0
	- 구매수량 0
	- 제품이미지리스트 0
	- 요청사항 0
	- 견적수령 희망일 0
	- 제품배송 희망일 0
	- 처리상태 0
 */