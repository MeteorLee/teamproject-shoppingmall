package project.finalproject1backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDTO {
    @Schema(defaultValue = "user1")
    private String userId;
    @Schema(defaultValue = "123456789")
    private String password;
    @Schema(defaultValue = "000회사")
    private String companyName;
    @Schema(defaultValue = "김대표")
    private String ownerName;
    private LocalDate openingDate;
    @Schema(defaultValue = "corporateNumber")
    private String corporateNumber;
    @Schema(defaultValue = "강사원")
    private String managerName;
    @Schema(defaultValue = "010-4321-1234")
    private String phoneNumber;
    @Schema(defaultValue = "user1@email.com")
    private String email;
}
/*
{
”userId”:”id”
”managerName”:”managerName”
”phoneNumber”:”phoneNumber”
”email”:”email”
”password”:”password”
”conpanyName”:”conpany_name”
”ownerName”:”대표자이름”
”openingDate”:”개업일시”
”corporateNumber”:”사업자등록번호”
}
 */