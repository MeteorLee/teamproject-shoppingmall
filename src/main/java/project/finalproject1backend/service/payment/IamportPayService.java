package project.finalproject1backend.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.finalproject1backend.dto.pay.iamport.IamportCallbackDTO;
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
    public void verifyAmount(Integer amount, String imp_uid) {

        IamportClient client = getClient();

        try {
            IamportResponse<Payment> payment_response = client.paymentByImpUid(imp_uid);

            Integer iamportPaymentAmount = payment_response.getResponse().getAmount().intValue();

            if (amount != iamportPaymentAmount) {
                // TODO: 2023-05-18 값이 안맞을 경우 로직 필요 (DB정보 삭제 + 결제 취소 로직 필요)

//                // imp_uid를 통한 전액취소
//                CancelData cancel_data = new CancelData(imp_uid, true);
//                IamportResponse<Payment> cancel_response = client.cancelPaymentByImpUid(cancel_data);

            }

        } catch (IamportResponseException | IOException e) {
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

        String imp_uid = requestDTO.getImp_uid();

        try {
            IamportResponse<Payment> payment_response = client.paymentByImpUid(imp_uid);

            // TODO: 2023-05-18 가맹점 번호 검증 로직 구현 필요
            String iamportMerchantUid = payment_response.getResponse().getMerchantUid();
            // String requestDTOMerchantUid = ...


        } catch (IamportResponseException | IOException e) {
            throw new PaymentException();
        }

    }



}
