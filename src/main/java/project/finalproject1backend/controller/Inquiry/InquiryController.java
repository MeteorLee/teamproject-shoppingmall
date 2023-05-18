package project.finalproject1backend.controller.Inquiry;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryDTO;
import project.finalproject1backend.dto.inquiry.InquiryDTO;
import project.finalproject1backend.dto.inquiry.SaleInquiryDTO;
import project.finalproject1backend.dto.user.UserInfoResponseDTO;
import project.finalproject1backend.dto.user.UsersInfoDTO;

@RequiredArgsConstructor
@RequestMapping("/account")
@RestController
public class InquiryController {

//    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
//    @Operation(summary = "마이 페이지(문의현황 전체조회)", description = "마이 페이지(문의현황 전체조회) 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key")})
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserInfoResponseDTO.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @GetMapping("/inquiry")
//    public ResponseEntity<?> getInquiry(@Parameter(hidden = true)@AuthenticationPrincipal BuyInquiryDTO buyInquiryDTO) {
//        return new ResponseEntity<>(new InquiryDTO(buyInquiryDTO, saleInquiryDTO), HttpStatus.OK);
//    }
//
//    @Tag(name = "API 관리자페이지", description = "관리자페이지 api 입니다.")
//    @Operation(summary = "관리자 페이지(고객관리) 전체조회", description = "관리자 페이지(고객관리) 전체조회 메서드입니다.select : “업체명”,”ROLE_REFUSE “ROLE_USER”,”ROLE_STANDBY, “담당자명”",
//            security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UsersInfoDTO.class))),
//    })
//    @GetMapping("/account/admin/users")
////    public ResponseEntity<?> getUsers(@Parameter(hidden = true) @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String select,@RequestParam String value) {
//    public ResponseEntity<?> getUsers(@RequestParam(required = false) String select, @RequestParam(required = false) String value) {
////        return userService.getUsers(pageable,select,value);
//        return InquiryService.getUsers(select,value);
//    }
}
