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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.UserRole;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.ModifyRequestDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.user.*;
import project.finalproject1backend.service.UserService;

import javax.validation.Valid;
import java.util.List;

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
    @PostMapping(value = "/signup",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestPart(value = "requestDTO") @Valid UserSignUpRequestDTO requestDTO,
                                              BindingResult bindingResult,@RequestPart(required = false) List<MultipartFile> businessLicense) {
        return userService.signUp(requestDTO,businessLicense);
    }
    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "중복 ID 체크 메서드", description = "중복 ID 체크 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    @GetMapping( "/signup/checkId")
    public ResponseEntity<?> checkId(@RequestParam String userId) {
        return userService.checkId(userId);
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

    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "이메일 전송 메서드", description = "이메일 전송 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestParam String email) {
        return userService.sendEmail(email);
    }

    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "인증 메서드", description = "인증 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/sendEmail/confirm")
    public ResponseEntity<?> confirm(@RequestParam String email, @RequestParam String randomValue) {
        return userService.confirm(email,randomValue);
    }

    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "담당자 이름과 이메일로 id 찾기 메서드", description = "담당자 이름과 이메일로 id 찾기 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/findUserIdByManagerName")
    public ResponseEntity<?> findUserIdByManagerName(@RequestParam String email,@RequestParam String managerName) {
        return userService.findUserIdByManagerName(email,managerName);
    }

    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "managerName , email로 인증 및 기존 userId 이메일로 알려주기", description = "managerName , email로 인증 및 기존 userId 이메일로 알려주기 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/findUserIdByManagerName/confirm")
    public ResponseEntity<?> findUserIdByManagerNameConfirm(@RequestParam String email,@RequestParam String managerName, @RequestParam String token) {
        return userService.findUserIdByManagerNameConfirm(managerName,email,token);
    }

    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "임시 비밀번호로 생성 인증 메서드", description = "임시 비밀번호로 생성 인증 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/setRandomPassword")
    public ResponseEntity<?> setRandomPassword(@RequestParam String email,@RequestParam String userId) {
        return userService.setRandomPassword(email,userId);
    }

    @Tag(name = "API 로그인/회원가입", description = "로그인/회원가입 api 입니다.")
    @Operation(summary = "임시로 비밀번호 생성 이메일로 알려주기", description = "임시로 비밀번호 생성 이메일로 알려주기 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/setRandomPassword/confirm")
    public ResponseEntity<?> setRandomPasswordConfirm(@RequestParam String email,@RequestParam String userId, @RequestParam String token) {
        return userService.setRandomPasswordConfirm(userId,email,token);
    }



    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
    @Operation(summary = "마이 페이지(account 정보 조회)", description = "마이 페이지(account 정보 조회) 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserInfoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/account")
    public ResponseEntity<?> getUser(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal) {
//        return new ResponseEntity<>(new UserInfoResponseDTO(principal),HttpStatus.OK);
        return userService.getUserInfo(principal.getUserId());
    }


    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
    @Operation(summary = "마이 페이지(account 정보수정)", description = "마이 페이지(account 정보수정) 메서드입니다.'password','phoneNumber','managerName','email','companyName' 수정 가능합니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/modify")
    public ResponseEntity<?> modify(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal, @RequestBody UserModifyRequestDTO modifyRequestDTO) {
        return userService.modify(principal,modifyRequestDTO);
    }


    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
    @Operation(summary = "마이 페이지(account 정보수정) ⇒ 사업자등록증수정", description = "마이 페이지(account 정보수정)⇒ 사업자등록증수정 메서드입니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping(value = "/account/modifyLicense",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifyLicense(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDTO principal, @RequestPart(value = "requestDTO") UserModifyLicenseRequestDTO modifyRequestDTO,@RequestPart(required = false) List<MultipartFile> businessLicense) {
        return userService.modifyLicense(principal,modifyRequestDTO,businessLicense);
    }
    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
    @Operation(summary = "마이 페이지(account 추가첨부파일 등록)", description = "마이 페이지(account 추가첨부파일 등록) 메서드입니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping(value = "/account/additionalData",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> additionalData(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDTO principal,@RequestPart(required = false) List<MultipartFile> additionalData) {
        return userService.additionalData(principal,additionalData);
    }
    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
    @Operation(summary = "마이 페이지(사업자등록 첨부여부 확인)", description = "마이 페이지(사업자등록 첨부여부 확인) 메서드입니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    @PostMapping(value = "/account/businessLicense")
    public ResponseEntity<?> booleanBusinessLicense(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDTO principal) {
        return userService.booleanBusinessLicense(principal);
    }



    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
    @Operation(summary = "관리자 페이지(고객관리) 전체조회", description = "관리자 페이지(고객관리) 전체조회 메서드입니다.select : “업체명”, “담당자명”",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserInfoResponseDTO.class))),
    })
    @GetMapping("/account/admin/users")
    public ResponseEntity<?> getUsers( @Parameter(example = "{\n" +
            "  \"page\": 0,\n" +
            "  \"size\": 15,\n" +
            "  \"sort\" : \"id\"\n" +
            "}")@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false) String select, @RequestParam(required = false) String value) {
        return userService.getUsers(pageable,select,value);
    }
    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
    @Operation(summary = "관리자 페이지(고객관리) 전체조회(ROLE_USER)", description = "관리자 페이지(고객관리) 전체조회(ROLE_USER) 메서드입니다.select : “업체명”, “담당자명”",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UsersInfoDTO.class))),
    })
    @GetMapping("/account/admin/users/getRoleUser")
    public ResponseEntity<?> getUsersRoleUser( @Parameter(example = "{\n" +
            "  \"page\": 0,\n" +
            "  \"size\": 15,\n" +
            "  \"sort\" : \"id\"\n" +
            "}")@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false) String select, @RequestParam(required = false) String value) {
        return userService.getUsersByRole(pageable, UserRole.ROLE_USER,select,value);
    }
    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
    @Operation(summary = "관리자 페이지(고객관리) 전체조회(ROLE_STANDBY)", description = "관리자 페이지(고객관리) 전체조회(ROLE_STANDBY) 메서드입니다.select : “업체명”, “담당자명”",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UsersInfoDTO.class))),
    })
    @GetMapping("/account/admin/users/getRoleStandby")
    public ResponseEntity<?> getUsersRoleStandby( @Parameter(example = "{\n" +
            "  \"page\": 0,\n" +
            "  \"size\": 15,\n" +
            "  \"sort\" : \"id\"\n" +
            "}")@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false) String select, @RequestParam(required = false) String value) {
        return userService.getUsersByRole(pageable, UserRole.ROLE_STANDBY,select,value);
    }
    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
    @Operation(summary = "관리자 페이지(고객관리) 전체조회(ROLE_REFUSE)", description = "관리자 페이지(고객관리) 전체조회(ROLE_REFUSE) 메서드입니다.select : “업체명”, “담당자명”",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UsersInfoDTO.class))),
    })
    @GetMapping("/account/admin/users/getRoleRefuse")
    public ResponseEntity<?> getUsersRoleRefused( @Parameter(example = "{\n" +
            "  \"page\": 0,\n" +
            "  \"size\": 15,\n" +
            "  \"sort\" : \"id\"\n" +
            "}")@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false) String select, @RequestParam(required = false) String value) {
        return userService.getUsersByRole(pageable, UserRole.ROLE_REFUSE,select,value);

    }


//    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
//    @Operation(summary = "관리자 페이지(고객관리) 선택조회", description = "관리자 페이지(고객관리) 선택조회 메서드입니다.",
//            security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserInfoResponseDTO.class))),
//    })
//    @GetMapping("/account/admin/usersInfo/{userId}")
//    public ResponseEntity<?> getUserInfo(@PathVariable String userId) {
//        return userService.getUserInfo(userId);
//    }


    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
    @Operation(summary = "관리자 페이지(고객관리)ROLE_USER로 변경", description = "관리자 페이지(고객관리)ROLE_USER로 변경 메서드입니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/account/admin/users/roleUser/{userId}")
    public ResponseEntity<?> setRoleUser(@PathVariable String userId) {
        return userService.roleUser(userId);
    }


    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
    @Operation(summary = "관리자 페이지(고객관리)ROLE_STANDBY로 변경", description = "관리자 페이지(고객관리)ROLE_STANDBY로 변경 메서드입니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/account/admin/users/roleStandby/{userId}")
    public ResponseEntity<?> setRoleStandby(@PathVariable String userId) {
        return userService.roleStandby(userId);
    }

    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
    @Operation(summary = "관리자 페이지(고객관리)ROLE_REFUSE로 변경", description = "관리자 페이지(고객관리)ROLE_REFUSE로 변경 메서드입니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/account/admin/users/roleRefuse/{userId}")
    public ResponseEntity<?> setRoleRefuse(@PathVariable String userId) {
        return userService.roleRefuse(userId);
    }

    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
    @Operation(summary = "관리자 페이지(고객관리)회원 수 조회", description = "관리자 페이지(고객관리)회원 수 조회 메서드입니다.",
            security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserCountResponseDTO.class))),
    })
    @GetMapping("/account/admin/users/getUserCount/{content}")
    public ResponseEntity<?> getUserCount() {
        return userService.getUserCount();
    }
}