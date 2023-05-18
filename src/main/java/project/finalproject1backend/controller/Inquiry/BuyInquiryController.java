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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.Inquiry.BuyInquiryState;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryResponseDTO;
import project.finalproject1backend.dto.inquiry.InquiryResponseDTO;
import project.finalproject1backend.service.Inquiry.BuyInquiryService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/account")
@RestController
public class BuyInquiryController {

    private final BuyInquiryService buyInquiryService;

    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "구매 문의생성 메서드", description = "구매 문의생성 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping(value = "/buyInquiry/register",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buyInquiryCreat (@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal, @RequestPart(value = "requestDTO") @Valid BuyInquiryDTO requestDTO,
                                              BindingResult bindingResult, @RequestPart(required = false) List<MultipartFile> buyImageList) {

        return buyInquiryService.buyInquiryCreat(requestDTO, buyImageList, principal);
    }

    @Transactional
    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "관리자 페이지(구매 문의 전체조회)", description = "관리자 페이지(구매 문의 전체조회) 메서드입니다.", security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = BuyInquiryResponseDTO.class))),
    })
    @GetMapping("/admin/buyInquiry")
    public ResponseEntity<?> buyInquiryFull(@Parameter(example = "{\n" +
            "  \"page\": 0,\n" +
            "  \"size\": 15,\n" +
            "  \"sort\" : \"id\"\n" +
            "}")@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,@RequestParam(required = false) String type,@RequestParam(required = false) String search) {
        return buyInquiryService.buyInquiryFull(pageable,type,search);
    }

    @Transactional
    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "관리자 페이지(구매문의 처리 상태변경)", description = "관리자 페이지(구매문의 처리 상태변경) 메서드입니다.", security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/admin/buyInquiry/{inquiryId}")
    public ResponseEntity<?> buyInquiryState(@PathVariable String inquiryId, @RequestParam BuyInquiryState state){
        return buyInquiryService.buyInquiryState(inquiryId, state);
    }

    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "관리자 페이지(구매문의 답변첨부파일)", description = "관리자 페이지(구매문의 답변첨부파일) 메서드입니다.", security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping(value = "/admin/buyInquiry/answerAttachment/{inquiryId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buyInquiryAnswerAttachment (@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal, @PathVariable  String inquiryId,
                                               @RequestPart(required = false) List<MultipartFile> answerAttachmentList) {

        return buyInquiryService.buyInquiryAnswerAttachment(inquiryId, answerAttachmentList);
    }

    @Transactional
    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "마이페이지(문의 전체조회)", description = "마이페이지(구매 문의 전체조회) 메서드입니다.", security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = InquiryResponseDTO.class))),
    })
    @GetMapping("/inquiry")
    public ResponseEntity<?> getInquiry(@Parameter(example = "{\n" +
            "  \"page\": 0,\n" +
            "  \"size\": 15,\n" +
            "  \"sort\" : \"id\"\n" +
            "}")@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal) {
        return buyInquiryService.getBuyInquiry(pageable,principal);
    }

    @Transactional
    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "마이페이지(문의 상세보기)", description = "마이페이지(구매 상세보기) 메서드입니다.", security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = BuyInquiryResponseDTO.class))),
    })
    @GetMapping("/inquiry/{inquiryId}")
    public ResponseEntity<?> getInquiryInfo(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,@PathVariable String inquiryId) {
        return buyInquiryService.getInquiryInfo(inquiryId);
    }

    @Transactional
    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "마이페이지(견적서 확인하기)", description = "마이페이지(견적서 확인하기) 메서드입니다.", security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = BuyInquiryResponseDTO.class))),
    })
    @GetMapping("/inquiry/getAnswerAttachment/{inquiryId}")
    public ResponseEntity<?> getAnswerAttachment(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,@PathVariable String inquiryId) {
        return buyInquiryService.getAnswerAttachment(inquiryId);
    }


}
