package project.finalproject1backend.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String ownerName;

    @Setter
    private LocalDate openingDate;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String corporateNumber;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String businessLicense;

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
    @OneToMany(mappedBy = "orderUser", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Orders> orders = new ArrayList<>();
    //order 로 했다가 domain의 Order말고 다른 Order 들어가서 오류가 많이 나서 orders로 변경...

    @OneToMany(mappedBy = "inquiryUser", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Inquiry> inquiries = new ArrayList<>();

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
