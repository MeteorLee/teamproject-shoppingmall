package project.finalproject1backend.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import project.finalproject1backend.dto.pay.kakao.*;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
    static final String admin_Key = "54d374f18f8a15516b5fa7b6930c2640"; // ADMIN 키
    private KakaoReadyResponse response;

    /**
     * 결제 요청
     *
     * @return
     */
    public KakaoReadyResponse kakaoPayReady(KakaoPayRequsetDTO requsetDTO) {

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);

        // 서버와 주고 받을 정보
        parameters.add("partner_order_id", requsetDTO.getPartner_order_id());
        parameters.add("partner_user_id", requsetDTO.getPartner_user_id());
        parameters.add("item_name", requsetDTO.getItem_name());
        parameters.add("quantity", String.valueOf(requsetDTO.getQuantity()));
        parameters.add("total_amount", String.valueOf(requsetDTO.getTotal_amount()));
        
//        parameters.add("vat_amount", "0"); // 상품 부가세 금액 필수 아님
        
        parameters.add("tax_free_amount", "0"); // 상품 비과세 금액 일단 0으로 설정
        // TODO: 2023-05-12 url 수정 필요  
        parameters.add("approval_url", "http://locahost8080/account/pay/kakao/success"); // 성공 시 redirect url
        parameters.add("cancel_url", "http://locahost8080/account/pay/kakao/cancel"); // 취소 시 redirect url
        parameters.add("fail_url", "http://locahost8080/account/pay/kakao/fail"); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        response = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        return response;
    }

    /**
     * 결제 승인
     *
     * @param pgToken
     * @return
     */
    public void approveResponse(String pgToken) {

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", response.getTid());
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        // TODO: 2023-05-18 결제 완료 DB 작업 필요

    }

    /**
     * 결제 환불
     *
     * @return
     */
    public void kakaoCancel(KakaoCancelRequestDTO requestDTO) {

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        // TODO: 2023-05-18 결제 고유 번호 확인 + 찾아서 넣기
        parameters.add("tid", "환불할 결제 고유 번호");

        // TODO: 2023-05-18 결제 금액보다 환불금액이 큰지 확인해야함
        int cancelAmount = requestDTO.getCancel_amount();

        parameters.add("cancel_amount", String.valueOf(requestDTO.getCancel_amount()));

        parameters.add("cancel_tax_free_amount", "0"); // 환불 비과세 금액 일단 0으로 설정
        parameters.add("cancel_vat_amount", "0"); // 환불 부가세 금액 일단 0으로 설정

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);

    }

    /**
     * 카카오 요청 헤드
     *
     * @return
     */
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + admin_Key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }

}