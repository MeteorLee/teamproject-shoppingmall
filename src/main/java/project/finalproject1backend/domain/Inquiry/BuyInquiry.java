package project.finalproject1backend.domain.Inquiry;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE user SET is_deleted = true, deleted_at=now() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BuyInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
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
    @OneToMany(mappedBy = "buyImage", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    private List<AttachmentFile> buyImageList = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "answerAttachment", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    private List<AttachmentFile> answerAttachmentList = new ArrayList<>();

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
