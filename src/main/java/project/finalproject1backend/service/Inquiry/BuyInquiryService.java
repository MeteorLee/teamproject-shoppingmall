package project.finalproject1backend.service.Inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import project.finalproject1backend.domain.User;
import project.finalproject1backend.dto.*;
import project.finalproject1backend.dto.inquiry.BuyInquiryDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryResponseDTO;
import project.finalproject1backend.dto.inquiry.InquiryResponseDTO;
import project.finalproject1backend.dto.inquiry.SaleInquiryResponseDTO;
import project.finalproject1backend.repository.AttachmentFileRepository;
import project.finalproject1backend.repository.Inquiry.BuyInquiryRepository;
import project.finalproject1backend.repository.Inquiry.SaleInquiryRepository;
import project.finalproject1backend.repository.UserRepository;
import project.finalproject1backend.util.UploadUtil;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Comparator.naturalOrder;

@Service
@Transactional
@RequiredArgsConstructor
public class BuyInquiryService {

    private final BuyInquiryRepository buyInquiryRepository;
    private final SaleInquiryRepository saleInquiryRepository;

    private final UserRepository userRepository;

    private final AttachmentFileRepository attachmentFileRepository;

    private final UploadUtil uploadUtil;

//    private String path = "C:\\Users\\user\\Downloads\\my\\파이널 프로젝트 저장 폴더";  //로컬 테스트용

    private String path = "/home/ubuntu/FinalProject/upload/inquiry";  // 배포용

    public ResponseEntity<?> buyInquiryCreat(BuyInquiryDTO requestDTO, List<MultipartFile> buyImageList, PrincipalDTO principal) {

        Optional<User> user = userRepository.findByUserId(principal.getUserId());
        if (!user.isPresent()) {
            return new ResponseEntity<>(new ErrorDTO("400", "notExistId"), HttpStatus.BAD_REQUEST);
        }
        String id = LocalDateTime.now().toString()+UUID.randomUUID().toString().substring(1,5);
        Set<BuyInquiryState> buyInquiryStates = new HashSet<>();
        buyInquiryStates.add(BuyInquiryState.STATE_UNREAD);
        BuyInquiry buyInquiry = BuyInquiry.builder()
                .id(id)
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
        }else if(type.equals("상태")){
            if(search==null){
                throw new IllegalArgumentException("nullError");
            }
            result =
                    buyInquiryRepository.findByState(BuyInquiryState.valueOf(search),pageable).map(BuyInquiryResponseDTO::new);
        }else{
            result = buyInquiryRepository.findAll(pageable).map(BuyInquiryResponseDTO::new);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    public ResponseEntity<?> buyInquiryState(String inquiryId, BuyInquiryState state) {

        state.getState();

        Set<BuyInquiryState> states = new HashSet<>();

        states.add(state);

        Optional<BuyInquiry> buyInquiry = buyInquiryRepository.findById(inquiryId);

        buyInquiry.get().setState(states);

        buyInquiryRepository.save(buyInquiry.get());

        return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
    }

    public ResponseEntity<?> buyInquiryAnswerAttachment(String inquiryId, List<MultipartFile> answerAttachmentList) {

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

    public ResponseEntity<?> getBuyInquiry(Pageable pageable, PrincipalDTO principal) {
        List<InquiryResponseDTO> result = new ArrayList<>();
//        result=buyInquiryRepository.findByBuyInquiryId_UserId(principal.getUserId()).stream().map(InquiryResponseDTO::new).toList();
        List<BuyInquiry> buyInquiries = buyInquiryRepository.findByBuyInquiryId_UserId(principal.getUserId());
        if(!(buyInquiries==null || buyInquiries.isEmpty())){
            for (BuyInquiry b:buyInquiries) {
                result.add(new InquiryResponseDTO(b));
            }
        }
        List<SaleInquiry> saleInquiries = saleInquiryRepository.findBySaleInquiryId_UserId(principal.getUserId());
        if(!(saleInquiries==null || saleInquiries.isEmpty())){
            for (SaleInquiry s:saleInquiries) {
                result.add(new InquiryResponseDTO(s));
            }
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), result.size());
        Page<InquiryResponseDTO> page = new PageImpl<>(result.subList(start, end), pageable, result.size());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    public ResponseEntity<?> getInquiryInfo( String inquiryId) {
        Optional<BuyInquiry> buyInquiry = buyInquiryRepository.findById(inquiryId);
        if (buyInquiry.isPresent()) {
            return new ResponseEntity<>(new BuyInquiryResponseDTO(buyInquiry.get()), HttpStatus.OK);
        }
        Optional<SaleInquiry> saleInquiry = saleInquiryRepository.findById(inquiryId);
        if (saleInquiry.isPresent()) {
            return new ResponseEntity<>(new SaleInquiryResponseDTO(saleInquiry.get()), HttpStatus.OK);
        }
        throw new IllegalArgumentException("checkInquiryId");
    }

    public ResponseEntity<?> getAnswerAttachment(String inquiryId) {
        Optional<BuyInquiry> buyInquiry = buyInquiryRepository.findById(inquiryId);
        if(!buyInquiry.isPresent()){
            throw new IllegalArgumentException("checkInquiryId");
        }
        List<AttachmentDTO> result = buyInquiry.get().getAnswerAttachmentList().stream().map(AttachmentDTO::new).toList();
        if (result==null||result.isEmpty()) {
            throw new IllegalArgumentException("answerAttachment is null");
        }
        return new ResponseEntity<>(buyInquiryRepository.findById(inquiryId).get().getAnswerAttachmentList().stream().map(AttachmentDTO::new), HttpStatus.OK);

    }
}

