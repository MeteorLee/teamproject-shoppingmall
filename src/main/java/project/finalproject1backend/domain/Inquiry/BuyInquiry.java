package project.finalproject1backend.domain.Inquiry;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.util.Encrypt256;

import javax.persistence.*;
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
public class BuyInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User buyInquiryId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product buyInquiryProduct;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String company;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String companyAddress;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String address;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String detailsAddress;

    @Setter
    @Builder.Default
    private boolean mall = Boolean.FALSE;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String mallAddress;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String attachment;

    @Setter
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<BuyInquiryState> state = new HashSet<>();

}
