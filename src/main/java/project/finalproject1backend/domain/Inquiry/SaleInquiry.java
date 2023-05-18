package project.finalproject1backend.domain.Inquiry;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.util.Encrypt256;

import javax.persistence.*;
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
public class SaleInquiry {
    @Id
    private String id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private User saleInquiryId;

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
    @Convert(converter = Encrypt256.class)
    private String manufacturer;

    @Setter
    @Builder.Default
    private boolean mall = Boolean.FALSE;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String mallAddress;

    @Setter
    @OneToMany(mappedBy = "saleAttachment", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    private List<AttachmentFile> saleAttachmentList = new ArrayList<>();

    @Setter
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<SaleInquiryState> state = new HashSet<>();

}
