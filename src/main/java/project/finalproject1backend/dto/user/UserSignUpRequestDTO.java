package project.finalproject1backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDTO {
    @Schema(defaultValue = "user1")
    private String userId;
    @Schema(defaultValue = "123456789")
    private String password;
    @Schema(defaultValue = "김대표")
    private String ownerName;
    private LocalDate openingDate;
    @Schema(defaultValue = "corporateNumber")
    private String corporateNumber;
    @Schema(defaultValue = "businessLicense")
    private String businessLicense;
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
”password”:”password”
”owner_name”:”대표자이름”
”opening_date”:”개업일시”
”corporate_number”:”사업자등록번호”
”businessLicense”:”businessLicense”
”manager_name”:”manager_name”
”phone_number”:”phone_number”
”email”:”email”
”role”
}
 */