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
import project.finalproject1backend.dto.pay.iamport.IamportCancelRequestDTO;
import project.finalproject1backend.exception.PaymentCancellationException;
import project.finalproject1backend.exception.PaymentException;
import project.finalproject1backend.repository.OrderRepository;
import project.finalproject1backend.service.UserService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class IamportPayService {

    private final String api_key = "2160027041337455";
    private final String api_secret = "E5BLH8wqTt3JuwMsXGxfkrZiXPF2dwcUBKnUNhyh0gRfHblsiqnNrXC9SWDwXToLlC0LDZ68c2ZnvV24";

    private final UserService userService;

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

        // TODO: 2023-05-18 주문번호 -> orders레포지토리를 통해서 접근해서 user 비교 검증

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
                this.cancelPayment(imp_uid);
                throw new PaymentException();
            }

        } catch (IamportResponseException | IOException e) {
            this.cancelPayment(imp_uid);
            throw new PaymentException();
        }

    }

    /**
     * 주문 번호 검증
     * 
     * @param imp_uid
     */
    public void verifyUid(String imp_uid) {

        IamportClient client = getClient();

        try {

            // 아임포트 정보 받기
            IamportResponse<Payment> payment_response = client.paymentByImpUid(imp_uid);

            // TODO: 2023-05-18 가맹점 번호 검증 로직 구현 필요
            // 아임포트 가맹점 주문 번호
            String iamportMerchantUid = payment_response.getResponse().getMerchantUid();

            // DB 가맹점 주문 번호
//            String storeMerchantUid = DB작업 필요

//
//            if (iamportMerchantUid != ) {
//                this.cancelPayment(imp_uid);
//                throw new PaymentException();
//            }


        } catch (IamportResponseException | IOException e) {
            this.cancelPayment(imp_uid);
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

        try {
            // 환불 실행
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

            // TODO: 2023-05-19 환불 관련 DB 추가 product, orders

        } catch (IamportResponseException | IOException e) {
            throw new PaymentException();
        }


    }

    /**
     *  전액 환불 or 주문 취소
     *
     * @param imp_uid
     */
    private void cancelPayment(String imp_uid) {

        try {
            IamportClient client = getClient();

            // imp_uid를 이용한 전액 환불
            CancelData cancel_data = new CancelData(imp_uid, true);
            IamportResponse<Payment> cancel_response = client.cancelPaymentByImpUid(cancel_data);

            // TODO: 2023-05-19 DB작업 필요


        } catch (Exception e) {
            // TODO: 2023-05-19 전액 환불 로직에서 문제가 생긴다면 어떻게 처리해야할까?
            throw new PaymentCancellationException();
        }

    }

    /**
     * 주문 성공 시 DB에 반영
     * 
     * @param merchant_uid
     */
    public void sendSuccessDB(String merchant_uid) {

        // db 정보 가져오기
        Orders order = this.getOrdersByPartnerOrderId(merchant_uid);
        // 결제 완료 반영
        order.setStatus(OrderStatus.PURCHASED);
        orderRepository.save(order);

        // TODO: 2023-05-19 재고 작업 필요 유무 

    }

    /**
     * 주문 성공 시 DB에 반영
     *
     * @param merchant_uid
     */
    public void sendRefundAllDB(String merchant_uid) {

        // db 정보 가져오기
        Orders order = this.getOrdersByPartnerOrderId(merchant_uid);
        // 결제 완료 반영
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        // TODO: 2023-05-19 재고 작업 필요 유무

    }

    /**
     * 주문 번호를 이용하여 DB의 주문 테이블 정보 얻기
     *
     * @param merchant_uid
     * @return
     */
    private Orders getOrdersByPartnerOrderId(String merchant_uid) {
        // DB 접근하여 주문 정보 가져오기
        Optional<Orders> result = orderRepository.findByNumber(merchant_uid);
        return result.orElseThrow(PaymentException::new);

    }



}
