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
    public Long id;
    @Setter
    public String fileName;
    @Setter
    public String originalFileName;
    @Setter
    public String thumbFileName;
    @Setter
    public String filePath;

    @ManyToOne(fetch = FetchType.EAGER)
    public User userAdditionalData;
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    public User userBusinessLicense;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    public Product productAttachment;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    public BuyInquiry buyImage;

    @JsonBackReference
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