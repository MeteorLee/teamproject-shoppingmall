package project.finalproject1backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDTO {
    @Schema(defaultValue = "user1")
    private String userId;
    @Schema(defaultValue = "123456789")
    private String password;
}
