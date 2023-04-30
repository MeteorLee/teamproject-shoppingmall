package project.finalproject1backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.UserRole;
import project.finalproject1backend.dto.PrincipalDTO;

import java.time.LocalDate;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDTO {
    private String userId;
    private String ownerName;
    private LocalDate openingDate;
    private String corporateNumber;
    private String businessLicense;
    private String managerName;
    private String phoneNumber;
    private String email;
    private UserRole role;

    public UserInfoResponseDTO(PrincipalDTO dto) {
        this.userId = dto.getUserId();
        this.ownerName = dto.getOwnerName();
        this.openingDate = dto.getOpeningDate();
        this.corporateNumber = dto.getCorporateNumber();
        this.businessLicense = dto.getBusinessLicense();
        this.managerName = dto.getManagerName();
        this.phoneNumber = dto.getPhoneNumber();
        this.email = dto.getEmail();
        this.role = dto.getRole().iterator().next();
    }
}
/*
{
”userId”:”id”
”ownerName”:”대표자이름”
”openingDate”:”개업일시”
”corporateNumber”:”사업자등록번호””businessLicense”:”businessLicense”
”managerName”:”managerName”
”phoneNumber”:”phoneNumber”
”email”:”email”
”role”:”ROLE_STANDBY” —인증실패한회원  or
”role”:”ROLE_USER’  —-인증성공한 회원
}
 */