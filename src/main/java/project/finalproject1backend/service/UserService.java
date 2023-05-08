package project.finalproject1backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.UserRole;
import project.finalproject1backend.dto.*;
import project.finalproject1backend.dto.user.*;
import project.finalproject1backend.jwt.JwtTokenProvider;
import project.finalproject1backend.repository.AttachmentFileRepository;
import project.finalproject1backend.repository.UserRepository;
import project.finalproject1backend.util.UploadUtil;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final AttachmentFileRepository attachmentFileRepository;
    private final UploadUtil uploadUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final JwtTokenProvider jwtTokenProvider;

//    private String path = "C:\\upload";  //로컬 테스트용
    private String path = "/home/ubuntu/FinalProject/upload/users";  // 배포용

    public ResponseEntity<?> signUp(UserSignUpRequestDTO requestDTO, List<MultipartFile> businessLicense){
        if(userRepository.existsByUserId(requestDTO.getUserId())) {
            throw new IllegalArgumentException("existId");
//            return new ResponseEntity<>(new ErrorDTO  ("400","existId"), HttpStatus.BAD_REQUEST);
        }
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ROLE_STANDBY);
        User user = User.builder()
                .userId(requestDTO.getUserId())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .companyName(requestDTO.getCompanyName())
                .ownerName(requestDTO.getOwnerName())
                .corporateNumber(requestDTO.getCorporateNumber())
                .openingDate(requestDTO.getOpeningDate())
                .managerName(requestDTO.getManagerName())
                .email(requestDTO.getEmail())
                .phoneNumber(requestDTO.getPhoneNumber())
                .role(roles)
                .build();
        userRepository.save(user);
        if(!(businessLicense==null || businessLicense.isEmpty())){
            for (MultipartFile i: businessLicense){
                UploadDTO u = uploadUtil.upload(i,path);
                AttachmentFile attachmentFile = AttachmentFile.builder()
                        .fileName(u.getFileName())
                        .filePath(path)
                        .originalFileName(u.getOriginalName())
                        .userBusinessLicense(user)
                        .build();
                attachmentFileRepository.save(attachmentFile);
            }
        }
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    public ResponseEntity<?> login(UserLoginRequestDTO requestDTO) {
        if(!userRepository.existsByUserId(requestDTO.getUserId())) {
            throw new IllegalArgumentException("checkId");
//            return new ResponseEntity<>(new ErrorDTO("400","checkId"), HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findByUserId(requestDTO.getUserId());
        if(!passwordEncoder.matches(requestDTO.getPassword(),user.get().getPassword() )){
            throw new IllegalArgumentException("checkPassword");
//            return new ResponseEntity<>(new ErrorDTO("400","checkPassword"), HttpStatus.BAD_REQUEST);
        }
        String token = jwtTokenProvider.createToken(user.get().getUserId());
        return new ResponseEntity<>(new UserLoginResponseDTO("200",token),HttpStatus.OK);
    }

    public ResponseEntity<?> delete(PrincipalDTO principal) {
        userRepository.deleteById(principal.getId());
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    public ResponseEntity<?> modify(PrincipalDTO principal, ModifyRequestDTO modifyRequestDTO) {
        if(modifyRequestDTO.content==null){
            throw new IllegalArgumentException("checkContent");
//            return new ResponseEntity<>(new ErrorDTO("400","checkContent"), HttpStatus.BAD_REQUEST);
        }
        String[] contents = {"password","phoneNumber","managerName","email","companyName"};
        int count =0;
        for (String s:contents) {
            if(s.equals(modifyRequestDTO.content)){
                count++;
            }
        }
        if(count==0){
            throw new IllegalArgumentException("checkContent");
//            return new ResponseEntity<>(new ErrorDTO("400","checkContent"), HttpStatus.BAD_REQUEST);
        }
        Optional<User> user=userRepository.findById(principal.getId());
        switch (modifyRequestDTO.content){
            case "password":
                user.get().setPassword(passwordEncoder.encode(modifyRequestDTO.value));
                break;
            case "phoneNumber":
                user.get().setPhoneNumber(modifyRequestDTO.value);
                break;
            case "managerName":
                user.get().setManagerName(modifyRequestDTO.value);
                break;
            case "email":
                user.get().setEmail(modifyRequestDTO.value);
                break;
            case "companyName":
                user.get().setCompanyName(modifyRequestDTO.value);
                break;
        }
        userRepository.save(user.get());
        return new ResponseEntity<>(modifyRequestDTO, HttpStatus.OK);
    }


    public ResponseEntity<?> modifyLicense(PrincipalDTO principal, UserModifyLicenseRequestDTO modifyRequestDTO,List<MultipartFile> businessLicense) {
        if(!modifyRequestDTO.nullCheck()){
            throw new IllegalArgumentException("checkNull");
//            return new ResponseEntity<>(new ErrorDTO("400","checkNull"), HttpStatus.BAD_REQUEST);
        }
        Optional<User> user=userRepository.findById(principal.getId());
        user.get().setOwnerName(modifyRequestDTO.getOwnerName());
        user.get().setOpeningDate(modifyRequestDTO.getOpeningDate());
        user.get().setCorporateNumber(modifyRequestDTO.getCorporateNumber());
        userRepository.save(user.get());
        if(!(businessLicense==null || businessLicense.isEmpty())){
            if(!(user.get().getBusinessLicense()==null || user.get().getBusinessLicense().isEmpty())) {
                //업로드 파일이 있을시 저장된 파일 삭제 / 업로드할 파일 등록
                for (AttachmentFile a : user.get().getBusinessLicense()) {
                    uploadUtil.deleteFile(a.getFileName(), path);
                    //getA().getListOfB.remove(getA().getListOfB().get(someIndex));
//                    user.get().getBusinessLicense().remove(a);
                    a.setUserBusinessLicense(null);
                    attachmentFileRepository.save(a);
                    attachmentFileRepository.delete(a);
                }
//                attachmentFileRepository.deleteAllByUserBusinessLicense(user.get().getId());
            }for (MultipartFile i: businessLicense){
                UploadDTO u = uploadUtil.upload(i,path);
                AttachmentFile attachmentFile = AttachmentFile.builder()
                        .fileName(u.getFileName())
                        .filePath(path)
                        .originalFileName(u.getOriginalName())
                        .userBusinessLicense(user.get())
                        .build();
                attachmentFileRepository.save(attachmentFile);
            }
        }
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

//    public ResponseEntity<?> getUsers(Pageable pageable,String select,String value) {
    public ResponseEntity<?> getUsers(String select,String value) {
        // list로 주기
        List<UsersInfoDTO> userList ;
        if(select==null){
            userList=userRepository.findAll().stream().map(UsersInfoDTO::new).toList();
        }else if(select.equals("ROLE_USER")){
            userList=userRepository.findByRole(UserRole.ROLE_USER).stream().map(UsersInfoDTO::new).toList();
        }else if(select.equals("ROLE_STANDBY")){
            userList=userRepository.findByRole(UserRole.ROLE_STANDBY).stream().map(UsersInfoDTO::new).toList();
        }else if(select.equals("ROLE_REFUSE")){
            userList=userRepository.findByRole(UserRole.ROLE_REFUSE).stream().map(UsersInfoDTO::new).toList();
        }else if(select.equals("업체명")){
            userList=userRepository.findByCompanyName(value).stream().map(UsersInfoDTO::new).toList();
        }else if(select.equals("담당자명")){
            userList=userRepository.findByManagerName(value).stream().map(UsersInfoDTO::new).toList();
        }else {
            userList=userRepository.findAll().stream().map(UsersInfoDTO::new).toList();
        }

        return new ResponseEntity<>(userList,HttpStatus.OK);
        // page로 주기
//        Page<UserInfoResponseDTO>  userPage = userRepository.findAll(pageable).map(user -> UserInfoResponseDTO.from(user));
//        Page<UserInfoResponseDTO>  userPage = userRepository.findAll(pageable).map(UserInfoResponseDTO::new);
//        return new ResponseEntity<>(new UsersGetResponseDTO(userList,userPage), HttpStatus.OK);
    }

    public ResponseEntity<?> getUserInfo(String userId) {
        return new ResponseEntity<>(userRepository.findByUserId(userId).map(UserInfoResponseDTO::new), HttpStatus.OK);
    }
    public ResponseEntity<?> roleUser(String userId){
        Set<UserRole> roleUser = new HashSet<>();
        roleUser.add(UserRole.ROLE_USER);
        Optional<User> user = userRepository.findByUserId(userId);
        user.get().setRole(roleUser);
        userRepository.save(user.get());
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }
    public ResponseEntity<?> roleStandby(String userId){
        Set<UserRole> roleStandby = new HashSet<>();
        roleStandby.add(UserRole.ROLE_STANDBY);
        Optional<User> user = userRepository.findByUserId(userId);
        user.get().setRole(roleStandby);
        userRepository.save(user.get());
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }
    public ResponseEntity<?> roleRefuse(String userId){
        Set<UserRole> roleRefuse = new HashSet<>();
        roleRefuse.add(UserRole.ROLE_REFUSE);
        Optional<User> user = userRepository.findByUserId(userId);
        user.get().setRole(roleRefuse);
        userRepository.save(user.get());
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    public ResponseEntity<?> getUserCount() {
        int standbyCount=userRepository.findByRole(UserRole.ROLE_STANDBY).size();;
        int userCount=userRepository.findByRole(UserRole.ROLE_USER).size();;
        int refuseCount=userRepository.findByRole(UserRole.ROLE_REFUSE).size();;
        return new ResponseEntity<>(new UserCountResponseDTO(standbyCount,userCount,refuseCount),HttpStatus.OK);
    }

    public ResponseEntity<?> booleanBusinessLicense(PrincipalDTO principal) {
        Boolean result;
        if(principal.getBusinessLicense().isEmpty()||principal.getBusinessLicense()==null) {
            result = false;
        }
        else {result=true;
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
