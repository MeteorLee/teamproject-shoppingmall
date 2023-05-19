package project.finalproject1backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyRequestDTO {
    private String password;
    private String phoneNumber;
    private String managerName;
    private String email;
}
/*
'password',
'phoneNumber',
'managerName',
'email',
 */