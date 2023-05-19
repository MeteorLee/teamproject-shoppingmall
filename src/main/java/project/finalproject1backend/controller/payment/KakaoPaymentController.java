package project.finalproject1backend.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.pay.kakao.*;
import project.finalproject1backend.service.payment.KakaoPayService;

import javax.validation.Valid;
import java.security.Principal;

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
    public ResponseEntity<KakaoReadyResponse> readyToKakaoPay(@RequestBody @Valid KakaoPayRequsetDTO requsetDTO) {

        KakaoReadyResponse response = kakaoPayService.kakaoPayReady(requsetDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 결제 성공
     *
     * @param pgToken
     * @return
     */
    @GetMapping("/success")
    public ResponseEntity<ResponseDTO> afterPayRequest(@RequestParam("pg_token") String pgToken,
                                                       @RequestParam("partner_order_id") String partner_order_id) {

        kakaoPayService.approveResponse(pgToken, partner_order_id);

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    /**
     * 결제 취소
     *
     * @return
     */
    @GetMapping("/cancel")
    public ResponseEntity<ResponseDTO> cancel() {

        return new ResponseEntity<>(new ResponseDTO("400","fail"), HttpStatus.OK);
    }

    /**
     * 결제 실패
     *
     * @return
     */
    @GetMapping("/fail")
    public ResponseEntity<ResponseDTO> fail() {

        return new ResponseEntity<>(new ResponseDTO("400","fail"), HttpStatus.OK);
    }

    /**
     * 결제 환불
     *
     * @return
     */
    @PostMapping("/refund")
    public ResponseEntity<ResponseDTO> refund(@RequestBody @Valid KakaoCancelRequestDTO requestDTO, Principal principal) {

        kakaoPayService.kakaoCancel(requestDTO);

        return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
    }


}
