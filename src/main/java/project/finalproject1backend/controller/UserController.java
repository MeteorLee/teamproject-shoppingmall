package project.finalproject1backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.ModifyRequestDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.user.*;
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
                                              BindingResult bindingResult) {
        return userService.signUp(requestDTO);
    }
    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "로그인 메서드", description = "로그인 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserLoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequestDTO requestDTO,
                                    BindingResult bindingResult) {
        return userService.login(requestDTO);
    }

    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "회원탈퇴 메서드", description = "회원탈퇴 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/delete")
    public ResponseEntity<?> delete(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal) {
        return userService.delete(principal);
    }

    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
    @Operation(summary = "마이 페이지(account 정보 조회)", description = "마이 페이지(account 정보 조회) 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserInfoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/account")
    public ResponseEntity<?> getUser(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal) {
        return new ResponseEntity<>(new UserInfoResponseDTO(principal),HttpStatus.OK);
    }

    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
    @Operation(summary = "마이 페이지(account 정보수정)", description = "마이 페이지(account 정보수정) 메서드입니다.'password','phoneNumber','managerName','email' 수정 가능합니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ModifyRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/modify")
    public ResponseEntity<?> modify(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal, @RequestBody ModifyRequestDTO modifyRequestDTO) {
        return userService.modify(principal,modifyRequestDTO);
    }

    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
    @Operation(summary = "마이 페이지(account 정보수정) ⇒ 사업자등록증수정", description = "마이 페이지(account 정보수정)⇒ 사업자등록증수정 메서드입니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/modifyLicense")
    public ResponseEntity<?> modifyLicense(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDTO principal, @RequestBody UserModifyLicenseRequestDTO modifyRequestDTO) {
        return userService.modifyLicense(principal,modifyRequestDTO);
    }
}