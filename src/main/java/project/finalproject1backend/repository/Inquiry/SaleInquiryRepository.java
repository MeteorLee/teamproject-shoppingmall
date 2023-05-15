package project.finalproject1backend.repository.Inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import project.finalproject1backend.domain.Inquiry.SaleInquiry;
import project.finalproject1backend.domain.User;

import java.util.Optional;

public interface SaleInquiryRepository  extends JpaRepository<SaleInquiry, Long> {

}
