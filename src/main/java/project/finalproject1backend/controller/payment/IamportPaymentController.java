package project.finalproject1backend.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.pay.iamport.IamportCallbackDTO;
import project.finalproject1backend.dto.pay.iamport.IamportVerificaitonDTO;
import project.finalproject1backend.service.payment.IamportPayService;

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
    public ResponseEntity<ResponseDTO> callback(IamportCallbackDTO requestDTO, Principal principal) {

        // 유저 검증 로직
        iamportPayService.verifyEmail(principal.getName());

        // 주문 번호 검증
        iamportPayService.verifyUid(requestDTO);

        PaymentResponseDTO paymentResponse = new PaymentResponseDTO();

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
    public ResponseEntity<ResponseDTO> verify(IamportVerificaitonDTO requestDTO, Principal principal) {

        iamportPayService.verifyEmail(principal.getName());

        iamportPayService.verifyAmount(requestDTO.getAmount(), requestDTO.getImp_uid());

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    @PostMapping("/refund")
    public ResponseEntity<ResponseDTO> refund() {



        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

}
