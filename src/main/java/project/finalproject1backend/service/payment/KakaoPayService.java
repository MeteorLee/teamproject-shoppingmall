package project.finalproject1backend.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import project.finalproject1backend.domain.OrderStatus;
import project.finalproject1backend.domain.Orders;
import project.finalproject1backend.dto.pay.kakao.*;
import project.finalproject1backend.exception.PaymentException;
import project.finalproject1backend.repository.OrderRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    private final OrderRepository orderRepository;

    static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
    static final String admin_Key = "54d374f18f8a15516b5fa7b6930c2640"; // ADMIN 키

    /**
     * 결제 요청
     *
     * @return
     */
    public KakaoReadyResponse kakaoPayReady(KakaoPayRequsetDTO requsetDTO) {

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        String partner_order_id = requsetDTO.getPartner_order_id();
        Orders order = this.getOrdersByPartnerOrderId(partner_order_id);

        // 입력 받은 금액과 DB 금액이 비교
        int dbAmount = order.getTotalPrice();
        if (dbAmount != requsetDTO.getTotal_amount()) {
            throw new PaymentException();
        }

        // 서버와 주고 받을 정보
        parameters.add("cid", cid);
        parameters.add("partner_order_id", partner_order_id);
        parameters.add("partner_user_id", order.getUser().getEmail());
        parameters.add("total_amount", String.valueOf(order.getTotalPrice()));

        // TODO: 2023-05-19 아이템 이름, 갯수 작업 추후 필요
        parameters.add("item_name", "아이템 이름");
        parameters.add("quantity", "5"); // 아이템 갯수

        // 부가세, 비과세 금액으로 현재는 0으로 설정
        parameters.add("tax_free_amount", "0"); // 상품 비과세 금액 일단 0으로 설정
//        parameters.add("vat_amount", "0"); // 상품 부가세 금액 필수 아님, 없으면 0 설정
        
        // TODO: 2023-05-12 url 잘 작동하는 지 확인 필요
        parameters.add("approval_url", "http://52.78.88.121:8080/account/pay/kakao/success?partner_order_id=" +
                partner_order_id); // 성공 시 redirect url, 주문 번호 쿼리 스트링 추가
        parameters.add("cancel_url", "http://52.78.88.121:8080/account/pay/kakao/cancel"); // 취소 시 redirect url
        parameters.add("fail_url", "http://52.78.88.121:8080/account/pay/kakao/fail"); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoReadyResponse response = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        // pg사 주문 번호 db 저장
        order.setPgUid(response.getTid());
        // db 반영
        orderRepository.save(order);

        return response;
    }

    /**
     * 결제 승인
     *
     * @param pgToken
     * @return
     */
    public void approveResponse(String pgToken, String partner_order_id) {

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        // TODO: 2023-05-19 tid 주고 받는 방식 결정

        // DB 접근하여 주문 정보 가져오기
        Orders orders = this.getOrdersByPartnerOrderId(partner_order_id);

        parameters.add("tid", orders.getPgUid());

        parameters.add("partner_order_id", String.valueOf(orders.getNumber()));
        parameters.add("partner_user_id", orders.getUser().getEmail());
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        orders.setStatus(OrderStatus.PURCHASED);
        orderRepository.save(orders);
        // TODO: 2023-05-19 재고 관련 로직이 필요한지 아직 모름

    }

    /**
     * 결제 환불
     *
     * @return
     */
    public void kakaoCancel(KakaoCancelRequestDTO requestDTO) {

        // DB 접근하여 주문 정보 가져오기
        Orders orders = this.getOrdersByPartnerOrderId(requestDTO.getPartner_order_id());

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", orders.getPgUid());

        int cancelAmount = requestDTO.getCancel_amount();
        int totalAmount = orders.getTotalPrice();
        if (cancelAmount > totalAmount) {
            throw new PaymentException();
        }

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

        orders.setTotalPrice(totalAmount - cancelAmount);
        orderRepository.save(orders);
    }

    /**
     * 카카오 요청 header 생성
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

    // TODO: 2023-05-19 주문 당사자와 DB 주문자가 같은지 확인하는 로직 필요(진짜 필요한가? Security에서 일단 확인은 해줌)

    /**
     * 주문 번호를 이용하여 DB의 주문 테이블 정보 얻기
     * 
     * @param partner_order_id
     * @return
     */
    private Orders getOrdersByPartnerOrderId(String partner_order_id) {
        // DB 접근하여 주문 정보 가져오기

        Optional<Orders> result = orderRepository.findByNumber(partner_order_id);
        return result.orElseThrow(PaymentException::new);

    }
}