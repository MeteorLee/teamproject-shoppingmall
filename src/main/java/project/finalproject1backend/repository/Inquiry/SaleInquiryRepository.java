package project.finalproject1backend.repository.Inquiry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.Inquiry.BuyInquiryState;
import project.finalproject1backend.domain.Inquiry.SaleInquiry;
import project.finalproject1backend.domain.Inquiry.SaleInquiryState;
import project.finalproject1backend.domain.User;

import java.util.List;
import java.util.Optional;

public interface SaleInquiryRepository  extends JpaRepository<SaleInquiry, String> {
    List<SaleInquiry> findBySaleInquiryId_UserId(String userId);

    Page<SaleInquiry> findBySaleInquiryId_CompanyName(String companyName, Pageable pageable);

    Page<SaleInquiry> findByState(SaleInquiryState state, Pageable pageable);
}
