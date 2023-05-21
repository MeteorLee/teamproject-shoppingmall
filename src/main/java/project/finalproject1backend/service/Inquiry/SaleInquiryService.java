package project.finalproject1backend.service.Inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.Inquiry.BuyInquiryState;
import project.finalproject1backend.domain.Inquiry.SaleInquiry;
import project.finalproject1backend.domain.Inquiry.SaleInquiryState;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.UploadDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryResponseDTO;
import project.finalproject1backend.dto.inquiry.SaleInquiryDTO;
import project.finalproject1backend.dto.inquiry.SaleInquiryResponseDTO;
import project.finalproject1backend.repository.AttachmentFileRepository;
import project.finalproject1backend.repository.Inquiry.SaleInquiryRepository;
import project.finalproject1backend.repository.UserRepository;
import project.finalproject1backend.util.UploadUtil;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleInquiryService {

    private final SaleInquiryRepository saleInquiryRepository;

    private  final UserRepository userRepository;

    private final AttachmentFileRepository attachmentFileRepository;

    private final UploadUtil uploadUtil;

//    private String path = "C:\\Users\\user\\Downloads\\my\\파이널 프로젝트 저장 폴더";  //로컬 테스트용

    private String path = "/home/ubuntu/FinalProject/upload/inquiry";  // 배포용
    public ResponseEntity<?> saleInquiryCreat(SaleInquiryDTO requestDTO, List<MultipartFile> saleAttachmentList, PrincipalDTO principal) {

        Optional<User> user = userRepository.findByUserId(principal.getUserId());
        if (!user.isPresent()) {
            return new ResponseEntity<>(new ErrorDTO("400", "notExistId"), HttpStatus.BAD_REQUEST);
        }
        String id = LocalDateTime.now().toString()+ UUID.randomUUID().toString().substring(1,5);
        id.replace("-","_");

        Set<SaleInquiryState> saleInquiryStates = new HashSet<>();
        saleInquiryStates.add(SaleInquiryState.STATE_UNREAD);
        SaleInquiry saleInquiry = SaleInquiry.builder()
                .id(id)
                .saleInquiryId(user.get())
                .company(requestDTO.getCompany())
                .companyAddress(requestDTO.getCompanyAddress())
                .address(requestDTO.getAddress())
                .detailsAddress(requestDTO.getDetailsAddress())
                .manufacturer(requestDTO.getManufacturer())
                .mall(requestDTO.isMall())
                .mallAddress(requestDTO.getMallAddress())
                .state(saleInquiryStates)
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

    public ResponseEntity<?> saleInquiryFull(Pageable pageable, String type, String search) {
        Page<SaleInquiryResponseDTO> result;
        if(type==null){
            result = saleInquiryRepository.findAll(pageable).map(SaleInquiryResponseDTO::new);
        }else if(type.equals("기업명")){
            result =
                    saleInquiryRepository.findBySaleInquiryId_CompanyName(search,pageable).map(SaleInquiryResponseDTO::new);
        }else if(type.equals("상태")){
            if(search==null){
                throw new IllegalArgumentException("nullError");
            }
            result =
                    saleInquiryRepository.findByState(SaleInquiryState.valueOf(search),pageable).map(SaleInquiryResponseDTO::new);
        }else{
            result = saleInquiryRepository.findAll(pageable).map(SaleInquiryResponseDTO::new);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<?> saleInquiryState(String inquiryId, SaleInquiryState state) {

        state.getState();

        Set<SaleInquiryState> states = new HashSet<>();

        states.add(state);

        Optional<SaleInquiry> saleInquiry = saleInquiryRepository.findById(inquiryId);

        saleInquiry.get().setState(states);

        saleInquiryRepository.save(saleInquiry.get());

        return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
    }
}
