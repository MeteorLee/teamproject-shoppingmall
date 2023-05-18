package project.finalproject1backend.service.Inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.Inquiry.SaleInquiry;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.UploadDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryDTO;
import project.finalproject1backend.dto.inquiry.SaleInquiryDTO;
import project.finalproject1backend.repository.AttachmentFileRepository;
import project.finalproject1backend.repository.Inquiry.BuyInquiryRepository;
import project.finalproject1backend.repository.Inquiry.SaleInquiryRepository;
import project.finalproject1backend.repository.UserRepository;
import project.finalproject1backend.util.UploadUtil;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleInquiryService {

    private final SaleInquiryRepository saleInquiryRepository;

    private  final UserRepository userRepository;

    private final AttachmentFileRepository attachmentFileRepository;

    private final UploadUtil uploadUtil;

    private String path = "C:\\Users\\user\\Downloads\\my";  //로컬 테스트용

//    private String path = "/home/ubuntu/FinalProject/upload/inquiry";  // 배포용

    public ResponseEntity<?> saleInquiryCreat(SaleInquiryDTO requestDTO, List<MultipartFile> saleAttachmentList, PrincipalDTO principal) {

        Optional<User> user = userRepository.findByUserId(principal.getUserId());
        if (!user.isPresent()) {
            return new ResponseEntity<>(new ErrorDTO("400", "notExistId"), HttpStatus.BAD_REQUEST);
        }

        SaleInquiry saleInquiry = SaleInquiry.builder()
                .saleInquiryId(user.get())
                .company(requestDTO.getCompany())
                .companyAddress(requestDTO.getCompanyAddress())
                .address(requestDTO.getAddress())
                .detailsAddress(requestDTO.getDetailsAddress())
                .manufacturer(requestDTO.getManufacturer())
                .mall(requestDTO.isMall())
                .mallAddress(requestDTO.getMallAddress())
                .build();

        saleInquiryRepository.save(saleInquiry);

        if (saleAttachmentList == null || saleAttachmentList.isEmpty()) {
//            throw new IllegalArgumentException("checkSaleAttachmentList");
            return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
        }

        for (MultipartFile m : saleAttachmentList) {
            UploadDTO uploadDTO = uploadUtil.upload(m, path);

            attachmentFileRepository.save(AttachmentFile.builder()
                    .fileName(uploadDTO.getFileName())
                    .filePath(path)
                    .originalFileName(uploadDTO.getOriginalName())
                    .thumbFileName(uploadDTO.getThumbFileName())
                    .saleAttachment(saleInquiry)
                    .build());

        }

        return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
    }
}
