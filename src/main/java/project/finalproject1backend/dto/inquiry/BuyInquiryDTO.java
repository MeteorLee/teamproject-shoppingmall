package project.finalproject1backend.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.Inquiry.BuyInquiryState;
import project.finalproject1backend.domain.User;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyInquiryDTO {

    private String category;

    private String product;

    private int amount;

    private String content;

    private LocalDate estimateWishDate;

    private LocalDate deliveryWishDate;

}
/*
”category” :”대량구매, 주문제작”
”product” :”구매 희망 제품”
”amount” :”구매 수량”
”buyImageList” :”이미지 첨부파일”
”answerAttachment” :”답변 첨부파일”
”content” :”요청사항 작성”
”estimateWishDate” :”견적 수령 희망일”
”deliveryWishDate” :”제품 배송 희망일”
 */