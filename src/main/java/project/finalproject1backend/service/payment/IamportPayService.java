package project.finalproject1backend.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.finalproject1backend.domain.OrderStatus;
import project.finalproject1backend.domain.Orders;
import project.finalproject1backend.dto.pay.iamport.IamportCallbackDTO;
import project.finalproject1backend.dto.pay.iamport.IamportCancelRequestDTO;
import project.finalproject1backend.dto.pay.iamport.IamportVerificaitonDTO;
import project.finalproject1backend.exception.PaymentCancelAllException;
import project.finalproject1backend.exception.PaymentException;
import project.finalproject1backend.exception.PaymentRefundException;
import project.finalproject1backend.repository.OrderRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class IamportPayService {

    private final String api_key = "2160027041337455";
    private final String api_secret = "E5BLH8wqTt3JuwMsXGxfkrZiXPF2dwcUBKnUNhyh0gRfHblsiqnNrXC9SWDwXToLlC0LDZ68c2ZnvV24";

    private final OrderRepository orderRepository;

    /**
     * 아임포트 연결
     * 
     * @return
     */
    private IamportClient getClient() {

        return new IamportClient(api_key, api_secret);
    }

    /**
     * 유저 검증
     * 
     * @param email
     */
    public void verifyEmail(String email, String merchant_uid) {

        // DB 정보 얻어오기
        Orders order = this.getOrdersByMerchant_uid(merchant_uid);

        // 주문 DB, 로그인 eamil 비교
        String loginEmail = email;
        String pgEmail = order.getUser().getEmail();

        if (loginEmail.equals(pgEmail)) { // 다를 경우 주문 취소
            this.cancelPaymentByImpUid(order.getPgUid());
            throw new PaymentException();
        }

    }

    /**
     * 금액 검증
     * 
     * @param amount
     * @param imp_uid
     */
    public void verifyAmount(int amount, String imp_uid) {

        try {
            IamportClient client = getClient();

            // pg사 정보 받기
            IamportResponse<Payment> payment_response = client.paymentByImpUid(imp_uid);

            // pg사에 저장된 금액
            int iamportPaymentAmount = payment_response.getResponse().getAmount().intValue();

            if (amount != iamportPaymentAmount) { // 금액이 다를 경우
                this.cancelPaymentByImpUid(imp_uid);
                throw new PaymentException();
            }

        } catch (IamportResponseException | IOException e) {
            this.cancelPaymentByImpUid(imp_uid);
            throw new PaymentException();
        }

    }

    /**
     * 주문 번호 검증
     * 
     * @param requestDTO
     */
    public void verifyUid(IamportCallbackDTO requestDTO) {

        IamportClient client = getClient();

        try {

            // 아임포트 정보 받기
            IamportResponse<Payment> payment_response = client.paymentByImpUid(requestDTO.getImp_uid());

            // DB 주문 정보
            Orders order = this.getOrdersByMerchant_uid(requestDTO.getMerchant_uid());


            // pg사 주문 번호
            String pgMerchantUid = payment_response.getResponse().getMerchantUid();
            // db 주문 정보
            String orderMerchantUid = order.getPgUid();

            if (pgMerchantUid.equals(orderMerchantUid)) { // 다를 경우
                this.cancelPaymentByImpUid(requestDTO.getImp_uid());
                throw new PaymentException();
            }

        } catch (IamportResponseException | IOException e) {
            this.cancelPaymentByImpUid(requestDTO.getImp_uid());
            throw new PaymentException();
        }

    }

    /**
     * 특정 금액 환불 로직
     * 
     * @param requestDTO
     */
    public void cancelAmount(IamportCancelRequestDTO requestDTO) {

        IamportClient client = getClient();
        // 환불 금액
        BigDecimal cancelAmount = BigDecimal.valueOf(requestDTO.getCancel_amount());
        // 환불 데이터
        CancelData cancel_data = new CancelData(requestDTO.getMerchant_uid(), false, cancelAmount);
        // checksum 으로 검증 추가
        cancel_data.setChecksum(cancelAmount);

        // DB 검증
        Orders order = this.getOrdersByMerchant_uid(requestDTO.getMerchant_uid());
        int dbAmount = order.getTotalPrice();

        if (requestDTO.getCancel_amount() > dbAmount) { // DB 총 금액과 request 환불 금액 비교
            throw new PaymentRefundException();
        }

        try {
            // 환불 실행
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

            // DB 반영
            order.setTotalPrice(dbAmount - requestDTO.getCancel_amount());
            orderRepository.save(order);

            // TODO: 2023-05-19 특정 금액 환불 시 재고 DB반영 여부 모름

        } catch (IamportResponseException | IOException e) {
            throw new PaymentException();
        }

    }

    /**
     *  전액 환불 or 주문 취소
     *
     * @param imp_uid
     */
    private void cancelPaymentByImpUid(String imp_uid) {

        try {
            IamportClient client = getClient();

            // imp_uid를 이용한 전액 환불
            CancelData cancel_data = new CancelData(imp_uid, true);
            IamportResponse<Payment> cancel_response = client.cancelPaymentByImpUid(cancel_data);

            Orders order = this.getOrdersByPGUid(imp_uid);
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);

            // TODO: 2023-05-19 전액 환불 or 주문 취소 시 재고 DB 반영 추가 여부 모름

        } catch (Exception e) {
            // TODO: 2023-05-19 전액 환불 로직에서 문제가 생긴다면 어떻게 처리해야할까?
            throw new PaymentCancelAllException();
        }

    }


    /**
     * 가맹점 주문 번호를 이용하여 DB의 주문 테이블 정보 얻기
     *
     * @param merchant_uid
     * @return
     */
    private Orders getOrdersByMerchant_uid(String merchant_uid) {
        // DB 접근하여 주문 정보 가져오기
        Optional<Orders> result = orderRepository.findByNumber(merchant_uid);
        return result.orElseThrow(PaymentException::new);

    }

    /**
     * pg사 주문 번호를 이용하여 DB의 주문 테이블 정보 얻기
     *
     * @param pgUid
     * @return
     */
    private Orders getOrdersByPGUid(String pgUid) {
        // DB 접근하여 주문 정보 가져오기
        Optional<Orders> result = orderRepository.findByPgUid(pgUid);
        return result.orElseThrow(PaymentException::new);

    }

    /**
     * pg사 주문 번호 DB 반영
     * 
     * @param requestDTO
     */
    public void saveImpUid(IamportCallbackDTO requestDTO) {

        // 주문 정보 가져오기
        Orders order = this.getOrdersByMerchant_uid(requestDTO.getMerchant_uid());
        order.setPgUid(requestDTO.getImp_uid());
        orderRepository.save(order);

    }

    /**
     * 결제 완료 DB 반영
     * 
     * @param requestDTO
     */
    public void savePurchased(IamportVerificaitonDTO requestDTO) {

        // 주문 정보 가져오기
        Orders order = this.getOrdersByMerchant_uid(requestDTO.getMerchant_uid());
        order.setStatus(OrderStatus.PURCHASED);
        orderRepository.save(order);

        // TODO: 2023-05-20 결제 완료시 재고 관련 DB 작업 여부 모름
    }
}
