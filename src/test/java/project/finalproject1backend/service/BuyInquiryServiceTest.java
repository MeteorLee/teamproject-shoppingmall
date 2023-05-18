package project.finalproject1backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.repository.Inquiry.BuyInquiryRepository;
import project.finalproject1backend.service.Inquiry.BuyInquiryService;

import java.util.stream.IntStream;

@SpringBootTest
public class BuyInquiryServiceTest {

    @Autowired
    BuyInquiryRepository buyInquiryRepository;

    @Test
    public void 문의생성(){

        IntStream.rangeClosed(1,20).forEach(i->{
            BuyInquiry buyInquiry =BuyInquiry.builder()
                    .content("Test" + i)
                    .category("판매문의")
                    .build();
            buyInquiryRepository.save(buyInquiry);
        });
    }
}
