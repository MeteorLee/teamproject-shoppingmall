package project.finalproject1backend.dto;

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
    private String userId;
    private String password;
    private String ownerName;
    private LocalDate openingDate;
    private String corporateNumber;
    private String businessLicense;
    private String managerName;
    private String phoneNumber;
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