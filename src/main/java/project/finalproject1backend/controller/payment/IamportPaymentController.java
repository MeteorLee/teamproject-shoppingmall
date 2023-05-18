package project.finalproject1backend.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.pay.iamport.IamportCallbackDTO;
import project.finalproject1backend.dto.pay.iamport.IamportVerificaitonDTO;
import project.finalproject1backend.service.payment.IamportPayService;

import javax.validation.Valid;
import java.security.Principal;

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
    public ResponseEntity<ResponseDTO> callback(@RequestBody @Valid IamportCallbackDTO requestDTO, Principal principal) {

        // 유저 검증
        iamportPayService.verifyEmail(principal.getName());

        // 주문 번호 검증
        iamportPayService.verifyUid(requestDTO);

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
    public ResponseEntity<ResponseDTO> verify(@RequestBody @Valid IamportVerificaitonDTO requestDTO, Principal principal) {

        // 유저 검증
        iamportPayService.verifyEmail(principal.getName());

        // 금액 검증
        iamportPayService.verifyAmount(requestDTO.getAmount(), requestDTO.getImp_uid());

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    @PostMapping("/refund")
    public ResponseEntity<ResponseDTO> refund(Principal principal) {

        iamportPayService.verifyEmail(principal.getName());

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

}