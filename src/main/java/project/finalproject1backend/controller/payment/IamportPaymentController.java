package project.finalproject1backend.controller.payment;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.pay.iamport.IamportCallbackDTO;
import project.finalproject1backend.dto.pay.iamport.IamportCancelRequestDTO;
import project.finalproject1backend.dto.pay.iamport.IamportVerificaitonDTO;
import project.finalproject1backend.service.payment.IamportPayService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account/pay/iamport")
public class IamportPaymentController {

    private final IamportPayService iamportPayService;

    /**
     * 결제 콜백 처리 + 검증
     *
     * @param requestDTO
     * @param principal
     * @return
     */
    @PostMapping("/callback")
    public ResponseEntity<ResponseDTO> callback(@RequestBody @Valid IamportCallbackDTO requestDTO,
                                                @Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal) {

        // 유저 검증
        iamportPayService.verifyEmail(principal.getEmail(), requestDTO.getMerchant_uid());

        // 주문 번호 검증
        iamportPayService.verifyUid(requestDTO);

        // TODO: 2023-05-19 DB 반영 필요
        iamportPayService.saveImpUid(requestDTO);

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    /**
     * 결제 검증 구현
     *
     * @param requestDTO
     * @param principal
     * @return
     */
    @PostMapping("/verify")
    public ResponseEntity<ResponseDTO> verify(@RequestBody @Valid IamportVerificaitonDTO requestDTO,
                                              @Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal) {

        // 유저 검증
        iamportPayService.verifyEmail(principal.getEmail(), requestDTO.getMerchant_uid());

        // 금액 검증
        iamportPayService.verifyAmount(requestDTO.getAmount(), requestDTO.getImp_uid());

        // 결제 DB 반영
        iamportPayService.savePurchased(requestDTO);

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    /**
     * 결제 환불
     *
     * @param requestDTO
     * @param principal
     * @return
     */
    @PostMapping("/refund")
    public ResponseEntity<ResponseDTO> refund(@RequestBody @Valid IamportCancelRequestDTO requestDTO,
                                              @Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal) {

        // 유저 검증
        iamportPayService.verifyEmail(principal.getEmail(), requestDTO.getMerchant_uid());

        // 환불
        iamportPayService.cancelAmount(requestDTO);

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

}
