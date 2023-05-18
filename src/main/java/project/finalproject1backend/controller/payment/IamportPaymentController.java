package project.finalproject1backend.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.finalproject1backend.dto.pay.PaymentResponseDTO;
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
    public PaymentResponseDTO callback(IamportCallbackDTO requestDTO, Principal principal) {

        iamportPayService.verifyEmail(principal.getName());

        iamportPayService.verifyUid(requestDTO);

        PaymentResponseDTO paymentResponse = new PaymentResponseDTO();

        return paymentResponse;
    }

    /**
     * 결제 검증 구현
     *
     * @param requestDTO
     * @param principal
     * @return
     */
    @PostMapping("/verify")
    public PaymentResponseDTO verify(IamportVerificaitonDTO requestDTO, Principal principal) {

        iamportPayService.verifyEmail(principal.getName());

        iamportPayService.verifyAmount(requestDTO.getAmount(), requestDTO.getImp_uid());

        PaymentResponseDTO paymentResponse = new PaymentResponseDTO();

        return paymentResponse;
    }
}
