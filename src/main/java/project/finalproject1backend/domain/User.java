package project.finalproject1backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.Inquiry.SaleInquiry;
import project.finalproject1backend.util.Encrypt256;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@ToString
@Getter
@Builder
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE user SET is_deleted = true, deleted_at=now() WHERE id = ?")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String userId;

    @Setter
    private String password;

    //사업자 관련
    @Setter
    @Convert(converter = Encrypt256.class)
    private String companyName;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String ownerName;

    @Setter
    private LocalDate openingDate;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String corporateNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "userBusinessLicense", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    private Set<AttachmentFile> businessLicense = new HashSet<>();

    //담당자관련
    @Setter
    @Convert(converter = Encrypt256.class)
    private String managerName;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String email;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String phoneNumber;

    //유저의 현황 관련
    @JsonIgnore
    @OneToMany(mappedBy = "cartUser", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private Set<Cart> carts = new HashSet<>();
    //order 로 했다가 domain의 Order말고 다른 Order 들어가서 오류가 많이 나서 orders로 변경...

    @JsonIgnore
    @OneToMany(mappedBy = "buyInquiryId", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private Set<BuyInquiry> buyInquiry = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "saleInquiryId", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private Set<SaleInquiry> saleInquiry = new HashSet<>();

    @Setter
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<UserRole> role = new HashSet<>();

    @Setter
    private LocalDate deletedAt;

    @Setter
    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE;

    @Setter
    private String token;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
/*
user_id varchar          //로그인id
  password varchar        //비밀번호
  //사업자등록증 관련
  owner_name varchar      //대표자이름
  opening_date date       //개업일시
  corporate_number  varchar //사업자등록번호
  business_license varchar //사업자등록증
  //담당자 관련
  manager_name varchar    //담당자이름
  email varchar           //이메일
  phone_number varchar          //연락처

  role varchar            //ROLE_STANDBY/ROLE_USER/ROLE_ADMIN
  order varchar            // 오더 현황
  inquiry
 */
