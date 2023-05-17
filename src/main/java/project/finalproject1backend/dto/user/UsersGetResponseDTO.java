package project.finalproject1backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersGetResponseDTO {
    private List<UserInfoResponseDTO> usersList;
    private Page<UserInfoResponseDTO> usersPage;
}
