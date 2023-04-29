package project.finalproject1backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.finalproject1backend.config.SecurityConfig;
import project.finalproject1backend.dto.user.UserSignUpRequestDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @DisplayName("회원 가입")
    @Test
    public void SetAccountRoleTest1() throws Exception {
        UserSignUpRequestDTO dto = UserSignUpRequestDTO.builder()
                .userId("user2")
//                .businessLicense("test")
//                .corporateNumber("test")
//                .email("test")
//                .managerName("test")
//                .openingDate(LocalDate.now())
//                .ownerName("test")
                .password("1234")
                .phoneNumber("test")
                .build();

        mvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
