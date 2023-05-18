package project.finalproject1backend.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.finalproject1backend.dto.pay.PaymentResponseDTO;
import project.finalproject1backend.dto.pay.kakao.KakaoApproveResponse;
import project.finalproject1backend.dto.pay.kakao.KakaoReadyResponse;
import project.finalproject1backend.service.payment.KakaoPayService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account/pay/kakao")
public class KakaoPaymentController {

    private final KakaoPayService kakaoPayService;

    /**
     * 결제 준비 요청
     *
     * @return
     */
    @PostMapping("/ready")
    public KakaoReadyResponse readyToKakaoPay() {

        return kakaoPayService.kakaoPayReady();
    }

    /**
     * 결제 성공
     *
     * @param pgToken
     * @return
     */
    @GetMapping("/success")
    public PaymentResponseDTO afterPayRequest(@RequestParam("pg_token") String pgToken) {

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);

        return new PaymentResponseDTO("결제 성공");
    }

    /**
     * 결제 취소
     *
     * @return
     */
    @GetMapping("/cancel")
    public PaymentResponseDTO cancel() {

        return new PaymentResponseDTO("결제 취소");
    }

    /**
     * 결제 실패
     *
     * @return
     */
    @GetMapping("/fail")
    public PaymentResponseDTO fail() {

        return new PaymentResponseDTO("결제 실패");
    }


}
