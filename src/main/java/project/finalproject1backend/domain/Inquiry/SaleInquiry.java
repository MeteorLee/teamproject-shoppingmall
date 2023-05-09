package project.finalproject1backend.domain.Inquiry;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.util.Encrypt256;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@ToString
@Getter
@Builder
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE user SET is_deleted = true, deleted_at=now() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SaleInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User saleInquiryId;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String category;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String product;

    @Setter
    private int amount;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String attachment;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String answerAttachment;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String content;

    @Setter
    private LocalDate estimateWishDate;

    @Setter
    private LocalDate deliveryWishDate;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String managerNumber;

    @Setter
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<SaleInquiryState> state = new HashSet<>();

}
