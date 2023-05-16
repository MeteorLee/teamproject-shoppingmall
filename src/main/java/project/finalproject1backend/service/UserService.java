package project.finalproject1backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final AttachmentFileRepository attachmentFileRepository;
    private final UploadUtil uploadUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final JavaMailSender javaMailSender;

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

    public ResponseEntity<?> sendEmail(String email) throws IllegalArgumentException {
        //토큰 생성
        String token = UUID.randomUUID().toString();
        //email 에 해당되는 user에 토큰 값을 집어넣고
        Optional<User> user = userRepository.findById(1L);
        if(user.isEmpty()){
            throw new IllegalArgumentException("checkEmail");
        }
        user.get().setToken(token);
        userRepository.save(user.get());
        //email 에 링크 전달
        try{
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage,false,"UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("bizcuratorproject.shop 인증메일");
            mimeMessageHelper.setText("http://52.78.88.121:8080/confirm?token="+token);
            javaMailSender.send(mailMessage);
        }catch (MessagingException e) {
            throw new IllegalArgumentException("email 전송 실패");
        }
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
        //링크 에 들어가면 receiverEmail 값과  token값을 받는다
        //토큰값 검증후 맞으면 아이디값 전달 or 비밀번호 새로 설정

    }

    public ResponseEntity<?> confirm( String token) {
        Optional<User> user = userRepository.findById(1L);
        if(!user.get().getToken().equals(token)){
            throw new IllegalArgumentException("잘못된 토큰값");
        }
        user.get().setToken(null);
        userRepository.save(user.get());
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);

    }

    public ResponseEntity<?> findUserIdByManagerName(String email, String managerName) {
        //토큰 생성
        String token = UUID.randomUUID().toString();
        //email 에 해당되는 user에 토큰 값을 집어넣고
        List<User> user = userRepository.findByManagerName(managerName);
        if(user==null ||user.isEmpty()){
            throw new IllegalArgumentException("checkManagerName");
        }
        int count = 0;
        for (User u:user) {
            if(!(u.getEmail()==null)&& u.getEmail().equals(email)){
                    u.setToken(token);
                    userRepository.save(u);
                    count++;
            }
        }
        if(count==0){
            throw new IllegalArgumentException("checkEmail");
        }
        //email 에 링크 전달
        try{
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage,false,"UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("bizcuratorproject.shop 인증메일");
            mimeMessageHelper.setText("http://52.78.88.121:8080/findUserIdByManagerName/confirm?managerName="+managerName+"&email="+email+"&token="+token);
            javaMailSender.send(mailMessage);
        }catch (MessagingException e) {
            throw new IllegalArgumentException("email 전송 실패");
        }
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    public ResponseEntity<?> findUserIdByManagerNameConfirm(String managerName,String email, String token) {
        //email 에 해당되는 user에 토큰 값을 집어넣고
        List<User> user = userRepository.findByManagerName(managerName);
        if(user==null ||user.isEmpty()){
            throw new IllegalArgumentException("checkManagerName");
        }
        int count = 0;
        List<String> userId = new ArrayList<>();
        for (User u:user) {
            if(!(u.getEmail()==null)&& u.getEmail().equals(email)){
                if(u.getToken().equals(token)){
                    userId.add(u.getUserId());
                    u.setToken(null);
                    userRepository.save(u);
                    count++;
                }
            }
        }
        if(count==0){
            throw new IllegalArgumentException("checkEmail");
        }
        try{
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage,false,"UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("bizcuratorproject.shop 아이디찾기");
            mimeMessageHelper.setText("bizcuratorproject.shop 에 "+userId+" 아이디로 가입되어 있습니다.");
            javaMailSender.send(mailMessage);
        }catch (MessagingException e) {
            throw new IllegalArgumentException("email 전송 실패");
        }
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    public ResponseEntity<?> setRandomPassword(String email, String userId) {
        //토큰 생성
        String token = UUID.randomUUID().toString();
        //email 에 해당되는 user에 토큰 값을 집어넣고
        Optional<User>user = userRepository.findByUserId(userId);
        if(user.get()==null ||user.isEmpty()){
            throw new IllegalArgumentException("userId");
        }
        if(user.get().getEmail()==null||!(user.get().getEmail().equals(email))){
            throw new IllegalArgumentException("checkEmail");

        }
        user.get().setToken(token);
        userRepository.save(user.get());
        //email 에 링크 전달
        try{
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage,false,"UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("bizcuratorproject.shop 인증메일");
            mimeMessageHelper.setText("http://52.78.88.121:8080/setRandomPassword/confirm?userId="+userId+"&email="+email+"&token="+token);
            javaMailSender.send(mailMessage);
        }catch (MessagingException e) {
            throw new IllegalArgumentException("email 전송 실패");
        }
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    public ResponseEntity<?> setRandomPasswordConfirm(String userId, String email, String token) {
        Optional<User>user = userRepository.findByUserId(userId);
        if(user.get()==null ||user.isEmpty()){
            throw new IllegalArgumentException("userId");
        }
        if(user.get().getEmail()==null||!(user.get().getEmail().equals(email))){
            throw new IllegalArgumentException("checkEmail");

        }
        if(user.get().getToken()==null||!user.get().getToken().equals(token)){
            throw new IllegalArgumentException("tokenError");
        }
        String newPassword = UUID.randomUUID().toString().substring(1,8);
        user.get().setPassword(passwordEncoder.encode(newPassword));
        user.get().setToken(null);
        userRepository.save(user.get());
        //email 에 링크 전달
        try{
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage,false,"UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("bizcuratorproject.shop 비밀번호 초기화");
            mimeMessageHelper.setText("bizcuratorproject.shop 에 "+userId+" 아이디로 가입되어 있고, 새로운 비밀번호는 "+newPassword+" 입니다.");
            javaMailSender.send(mailMessage);
        }catch (MessagingException e) {
            throw new IllegalArgumentException("email 전송 실패");
        }
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    public ResponseEntity<?> checkId(String userId) {
        return new ResponseEntity<>(userRepository.existsByUserId(userId),HttpStatus.OK);
    }
}
