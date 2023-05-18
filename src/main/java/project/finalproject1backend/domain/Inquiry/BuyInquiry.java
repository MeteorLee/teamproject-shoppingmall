package project.finalproject1backend.domain.Inquiry;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.util.Encrypt256;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BuyInquiry {

    @Id
    private String id;


    @ManyToOne(fetch = FetchType.EAGER)
    private User buyInquiryId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product buyInquiryProduct;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String category;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String product;

    @Setter
    private int amount;

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "buyImage", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    private Set<AttachmentFile> buyImageList = new HashSet<>();

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "answerAttachment", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    private Set<AttachmentFile> answerAttachmentList = new HashSet<>();

    @Setter
    @Convert(converter = Encrypt256.class)
    private String content;

    @Setter
    private LocalDate estimateWishDate;

    @Setter
    private LocalDate deliveryWishDate;

    @Setter
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<BuyInquiryState> state = new HashSet<>();

}
