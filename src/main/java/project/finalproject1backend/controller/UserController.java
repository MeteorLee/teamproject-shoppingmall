package project.finalproject1backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.UserSignUpRequestDTO;
import project.finalproject1backend.service.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class UserController {

    private final UserService userService;
    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "회원가입 메서드", description = "회원가입 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpRequestDTO requestDTO,
                                              BindingResult bindingResult) throws Exception {
        return new ResponseEntity<>(userService.signUp(requestDTO), HttpStatus.OK);
    }
}
