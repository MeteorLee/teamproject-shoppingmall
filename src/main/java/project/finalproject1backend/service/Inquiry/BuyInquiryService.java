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
import project.finalproject1backend.domain.User;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.UploadDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryResponseDTO;
import project.finalproject1backend.repository.AttachmentFileRepository;
import project.finalproject1backend.repository.Inquiry.BuyInquiryRepository;
import project.finalproject1backend.repository.UserRepository;
import project.finalproject1backend.util.UploadUtil;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BuyInquiryService {

    private final BuyInquiryRepository buyInquiryRepository;

    private final UserRepository userRepository;

    private final AttachmentFileRepository attachmentFileRepository;

    private final UploadUtil uploadUtil;

    private String path = "C:\\Users\\user\\Downloads\\my\\파이널 프로젝트 저장 폴더";  //로컬 테스트용

//    private String path = "/home/ubuntu/FinalProject/upload/inquiry";  // 배포용

    public ResponseEntity<?> buyInquiryCreat(BuyInquiryDTO requestDTO, List<MultipartFile> buyImageList, PrincipalDTO principal) {

        Optional<User> user = userRepository.findByUserId(principal.getUserId());
        if (!user.isPresent()) {
            return new ResponseEntity<>(new ErrorDTO("400", "notExistId"), HttpStatus.BAD_REQUEST);
        }
        Set<BuyInquiryState> buyInquiryStates = new HashSet<>();
        buyInquiryStates.add(BuyInquiryState.STATE_UNREAD);
        BuyInquiry buyInquiry = BuyInquiry.builder()
                .buyInquiryId(user.get())
                .category(requestDTO.getCategory())
                .product(requestDTO.getProduct())
                .amount(requestDTO.getAmount())
                .content(requestDTO.getContent())
                .estimateWishDate(requestDTO.getEstimateWishDate())
                .deliveryWishDate(requestDTO.getDeliveryWishDate())
                .state(buyInquiryStates)
                .build();

        buyInquiryRepository.save(buyInquiry);

        if (buyImageList == null || buyImageList.isEmpty()) {
//            throw new IllegalArgumentException("checkBuyImageList");
            return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
        }

        for (MultipartFile m : buyImageList) {
            UploadDTO uploadDTO = uploadUtil.upload(m, path);

            attachmentFileRepository.save(AttachmentFile.builder()
                    .fileName(uploadDTO.getFileName())
                    .filePath(path)
                    .originalFileName(uploadDTO.getOriginalName())
                    .thumbFileName(uploadDTO.getThumbFileName())
                    .buyImage(buyInquiry)
                    .build());

        }

        return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
    }

    public ResponseEntity<?> buyInquiryFull(Pageable pageable, String type, String search) {
        Page<BuyInquiryResponseDTO> result;
        if(type==null){
            result = buyInquiryRepository.findAll(pageable).map(BuyInquiryResponseDTO::new);
        }else if(type.equals("기업명")){
            result =
                    buyInquiryRepository.findByBuyInquiryId_CompanyName(search,pageable).map(BuyInquiryResponseDTO::new);
        }else{
            result = buyInquiryRepository.findAll(pageable).map(BuyInquiryResponseDTO::new);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    public ResponseEntity<?> buyInquiryState(Long inquiryId, BuyInquiryState state) {

        state.getState();

        Set<BuyInquiryState> states = new HashSet<>();

        states.add(state);

        Optional<BuyInquiry> buyInquiry = buyInquiryRepository.findById(inquiryId);

        buyInquiry.get().setState(states);

        buyInquiryRepository.save(buyInquiry.get());

        return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
    }

    public ResponseEntity<?> buyInquiryAnswerAttachment(Long inquiryId, List<MultipartFile> answerAttachmentList) {

        Optional<BuyInquiry> buyInquiry = buyInquiryRepository.findById(inquiryId);

        if (answerAttachmentList == null || answerAttachmentList.isEmpty()) {
            throw new IllegalArgumentException("checkAnswerAttachmentList");
//            return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
        }

        for (MultipartFile m : answerAttachmentList) {
            UploadDTO uploadDTO = uploadUtil.upload(m, path);

            attachmentFileRepository.save(AttachmentFile.builder()
                    .fileName(uploadDTO.getFileName())
                    .filePath(path)
                    .originalFileName(uploadDTO.getOriginalName())
                    .thumbFileName(uploadDTO.getThumbFileName())
                    .answerAttachment(buyInquiry.get())
                    .build());

        }

        return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
    }
}

