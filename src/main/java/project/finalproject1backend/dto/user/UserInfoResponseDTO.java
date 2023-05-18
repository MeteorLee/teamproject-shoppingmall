package project.finalproject1backend.dto.user;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.UserRole;
import project.finalproject1backend.dto.PrincipalDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDTO {
    private String userId;
    private String companyName;
    private String ownerName;
    private LocalDate openingDate;
    private String corporateNumber;
    private List<businessLicenseInfo> businessLicense;
    private List<businessLicenseInfo> additionalData;
    private String managerName;
    private String phoneNumber;
    private String email;
    private UserRole role;

    @Getter
    @Setter
    private class businessLicenseInfo{
        private String fileName;
        private String filePath;
        private String originalFileName;

        private businessLicenseInfo(AttachmentFile a) {
            this.fileName = a.getFileName();
            this.filePath = a.getFilePath();
            this.originalFileName = a.getOriginalFileName();
        }
    }

    public UserInfoResponseDTO(PrincipalDTO dto ,List<businessLicenseInfo> additionalData ) {
        this.userId = dto.getUserId();
        this.companyName=dto.getCompanyName();
        this.ownerName = dto.getOwnerName();
        this.openingDate = dto.getOpeningDate();
        this.corporateNumber = dto.getCorporateNumber();
        if(!(dto.getBusinessLicense()==null||dto.getBusinessLicense().isEmpty())){
            this.businessLicense = dto.getBusinessLicense().stream().map(businessLicenseInfo::new).collect(Collectors.toList());
        }
        this.additionalData = additionalData;
//        this.businessLicense = dto.getBusinessLicense();
        this.managerName = dto.getManagerName();
        this.phoneNumber = dto.getPhoneNumber();
        this.email = dto.getEmail();
        this.role = dto.getRole().iterator().next();
    }
    public UserInfoResponseDTO(User u,List<businessLicenseInfo> additionalData ) {
        this.userId = u.getUserId();
        this.companyName=u.getCompanyName();
        this.ownerName = u.getOwnerName();
        this.openingDate = u.getOpeningDate();
        this.corporateNumber = u.getCorporateNumber();
        if(!(u.getBusinessLicense()==null||u.getBusinessLicense().isEmpty())){
            this.businessLicense = u.getBusinessLicense().stream().map(businessLicenseInfo::new).collect(Collectors.toList());
        }
        this.additionalData = additionalData;
//        this.businessLicense = u.getBusinessLicense();
        this.managerName = u.getManagerName();
        this.phoneNumber = u.getPhoneNumber();
        this.email = u.getEmail();
        this.role = u.getRole().iterator().next();
    }
    public UserInfoResponseDTO from(User u, List<AttachmentFile> additionalData){
        List<businessLicenseInfo> list = new ArrayList<>();
        for (AttachmentFile a:additionalData) {
            list.add(new businessLicenseInfo(a));
        }
        return new UserInfoResponseDTO(u,list);
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