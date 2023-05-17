package project.finalproject1backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.Inquiry.SaleInquiry;

import javax.persistence.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AttachmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id; //파일번호
    @Setter
    public String fileName;  //파일명
    @Setter
    public String originalFileName; //원본파일명
    @Setter
    public String thumbFileName; //썸네일파일명
    @Setter
    public String filePath;  //파일경로

    @ManyToOne(fetch = FetchType.LAZY)
    public User userAdditionalData;  //추가정보
    @Setter
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    public User userBusinessLicense;  //사업자등록증

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    public Product productAttachment; //상품이미지

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    public BuyInquiry buyImage;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    public BuyInquiry answerAttachment;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    public SaleInquiry saleAttachment;
}
/*
id long [pk, increment]
    file_name vahchar
    origal_file_name vahchar
    file_path vahchar
 */