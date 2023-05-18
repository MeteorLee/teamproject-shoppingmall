package project.finalproject1backend.repository.Inquiry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.User;

import java.util.List;
import java.util.Optional;

public interface BuyInquiryRepository extends JpaRepository<BuyInquiry, Long> {
    Page<BuyInquiry> findByBuyInquiryId_CompanyName(String companyName,Pageable pageable);

}
