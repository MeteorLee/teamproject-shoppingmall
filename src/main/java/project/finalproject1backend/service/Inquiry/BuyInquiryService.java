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
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.UserRole;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.UploadDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryResponseDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryStateDTO;
import project.finalproject1backend.dto.user.UsersInfoDTO;
import project.finalproject1backend.repository.AttachmentFileRepository;
import project.finalproject1backend.repository.Inquiry.BuyInquiryRepository;
import project.finalproject1backend.repository.UserRepository;
import project.finalproject1backend.util.UploadUtil;

import java.util.*;
import java.util.stream.Collectors;

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

        BuyInquiry buyInquiry = BuyInquiry.builder()
                .buyInquiryId(user.get())
                .category(requestDTO.getCategory())
                .product(requestDTO.getProduct())
                .amount(requestDTO.getAmount())
                .content(requestDTO.getContent())
                .estimateWishDate(requestDTO.getEstimateWishDate())
                .deliveryWishDate(requestDTO.getDeliveryWishDate())
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

    public ResponseEntity<?> buyInquiryFull(Pageable pageable) {

        List<BuyInquiryResponseDTO> buyInquiryResponseDTOList = new ArrayList<>();

        Page<BuyInquiry> buyInquiries = buyInquiryRepository.findAll(pageable);

        for (BuyInquiry buyInquiry : buyInquiries) {

            buyInquiryResponseDTOList.add(BuyInquiryResponseDTO.builder()
                    .userId(buyInquiry.getBuyInquiryId().getUserId())
                    .email(buyInquiry.getBuyInquiryId().getEmail())
                    .phoneNumber(buyInquiry.getBuyInquiryId().getPhoneNumber())
                    .companyName(buyInquiry.getBuyInquiryId().getCompanyName())
                    .category(buyInquiry.getCategory())
                    .product(buyInquiry.getProduct())
                    .amount(buyInquiry.getAmount())
                    .buyImageList(buyInquiry.getBuyImageList().stream().map(BuyInquiryResponseDTO.buyImageListInfo::new).toList())
                    .content(buyInquiry.getContent())
                    .estimateWishDate(buyInquiry.getEstimateWishDate())
                    .deliveryWishDate(buyInquiry.getDeliveryWishDate())
                    .state(buyInquiry.getState())
                    .build());
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), buyInquiryResponseDTOList.size());
        Page<BuyInquiryResponseDTO> page = new PageImpl<>(buyInquiryResponseDTOList.subList(start, end), pageable, buyInquiryResponseDTOList.size());

        return new ResponseEntity<>(buyInquiries, HttpStatus.OK);

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

//    Page<BuyInquiry> buyInquiries;
//        buyInquiries = buyInquiryRepository.findAll(pageable);
//                if(){
//                buyInquiries = userRepository.findByCompanyName().get().
//                }

//        List<BuyInquiryStateDTO> buyInquiryStateDTOList;
//
//        if(inquiryId==null){
//            buyInquiryStateDTOList=buyInquiryRepository.findAll().stream().map(BuyInquiryStateDTO::new).toList();
//        }else if(inquiryId.equals("STATE_UNREAD")){
//            buyInquiryStateDTOList=buyInquiryRepository.findByRole(UserRole.ROLE_USER).stream().map(UsersInfoDTO::new).toList();
//        }else if(inquiryId.equals("STATE_REGISTER")){
//            buyInquiryStateDTOList=buyInquiryRepository.findByRole(UserRole.ROLE_STANDBY).stream().map(UsersInfoDTO::new).toList();
//        }else if(inquiryId.equals("STATE_SAMPLE")){
//            buyInquiryStateDTOList=buyInquiryRepository.findByRole(UserRole.ROLE_REFUSE).stream().map(UsersInfoDTO::new).toList();
//        }else if(inquiryId.equals("STATE_APPROVAL")){
//            buyInquiryStateDTOList=buyInquiryRepository.findByCompanyName(value).stream().map(UsersInfoDTO::new).toList();
//        }else if(inquiryId.equals("STATE_REFUND")) {
//            buyInquiryStateDTOList = buyInquiryRepository.findByManagerName(value).stream().map(UsersInfoDTO::new).toList();
//        }else if(inquiryId.equals("STATE_DELIVERY")) {
//            buyInquiryStateDTOList = buyInquiryRepository.findByManagerName(value).stream().map(UsersInfoDTO::new).toList();
//        }else if(inquiryId.equals("STATE_COMPLETED")){
//            buyInquiryStateDTOList=buyInquiryRepository.findByManagerName(value).stream().map(UsersInfoDTO::new).toList();
//        }else {
//            buyInquiryStateDTOList=buyInquiryRepository.findAll().stream().map(UsersInfoDTO::new).toList();
//        }
