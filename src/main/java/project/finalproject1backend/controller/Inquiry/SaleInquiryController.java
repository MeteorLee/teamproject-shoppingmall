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
import project.finalproject1backend.domain.Inquiry.SaleInquiry;
import project.finalproject1backend.domain.Inquiry.SaleInquiryState;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryResponseDTO;
import project.finalproject1backend.dto.inquiry.SaleInquiryDTO;
import project.finalproject1backend.dto.inquiry.SaleInquiryResponseDTO;
import project.finalproject1backend.service.Inquiry.SaleInquiryService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/account")
@RestController
public class SaleInquiryController {

    private final SaleInquiryService saleInquiryService;

    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "판매 문의생성 메서드", description = "판매 문의생성 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping(value = "/saleInquiry/register",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saleInquiryCreat (@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal, @RequestPart(value = "requestDTO") @Valid SaleInquiryDTO requestDTO,
                                              BindingResult bindingResult, @RequestPart(required = false) List<MultipartFile> saleAttachmentList) {

        return saleInquiryService.saleInquiryCreat(requestDTO, saleAttachmentList, principal);
    }

    @Transactional
    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "관리자 페이지(판매 문의 전체조회)", description = "관리자 페이지(판매 문의 전체조회) 메서드입니다.", security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SaleInquiryResponseDTO.class))),
    })
    @GetMapping("/admin/saleInquiry")
    public ResponseEntity<?> saleInquiryFull(@Parameter(example = "{\n" +
            "  \"page\": 0,\n" +
            "  \"size\": 15,\n" +
            "  \"sort\" : \"id\"\n" +
            "}")@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(required = false) String type, @RequestParam(required = false) String search) {
        return saleInquiryService.saleInquiryFull(pageable,type,search);
    }

    @Transactional
    @Tag(name = "API 문의하기", description = "문의하기 api 입니다.")
    @Operation(summary = "관리자 페이지(판매문의 처리 상태변경)", description = "관리자 페이지(판매문의 처리 상태변경) 메서드입니다.", security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/admin/saleInquiry/{inquiryId}")
    public ResponseEntity<?> saleInquiryState(@PathVariable String inquiryId, @RequestParam SaleInquiryState state){
        return saleInquiryService.saleInquiryState(inquiryId, state);
    }
}
