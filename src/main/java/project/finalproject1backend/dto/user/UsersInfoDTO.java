package project.finalproject1backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.UserRole;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersInfoDTO {
    private String managerName;
    private UserRole role;
    private String userId;
    private String companyName;

    public UsersInfoDTO(User user) {
        this.managerName = user.getManagerName();
        this.role = user.getRole().iterator().next();
        this.userId = user.getUserId();
        this.companyName = user.getCompanyName();
    }
}
/*
”managerName”:”[담당자명]”,
”role”:”[승인상태]ROLE_USER’
”userId”:”[아이디]”
””:”[업체명]”
 */