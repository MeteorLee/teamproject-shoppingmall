package project.finalproject1backend.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.finalproject1backend.exception.PaymentCancellationException;
import project.finalproject1backend.exception.PaymentException;
import project.finalproject1backend.service.UserService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class IamportPayService {

    private final String api_key = "2160027041337455";
    private final String api_secret = "E5BLH8wqTt3JuwMsXGxfkrZiXPF2dwcUBKnUNhyh0gRfHblsiqnNrXC9SWDwXToLlC0LDZ68c2ZnvV24";

    private final UserService userService;

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
     *  전액 환불
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


}
