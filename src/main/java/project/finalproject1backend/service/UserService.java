package project.finalproject1backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.UserRole;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.user.UserLoginRequestDTO;
import project.finalproject1backend.dto.user.UserSignUpRequestDTO;
import project.finalproject1backend.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> signUp(UserSignUpRequestDTO requestDTO){
        if(userRepository.existsByUserId(requestDTO.getUserId())) {
            return new ResponseEntity<>(ErrorDTO.builder().code("400").message("existId").build(), HttpStatus.BAD_REQUEST);
        }
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ROLE_STANDBY);
        User user = User.builder()
                .userId(requestDTO.getUserId())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .ownerName(requestDTO.getOwnerName())
                .corporateNumber(requestDTO.getCorporateNumber())
                .openingDate(requestDTO.getOpeningDate())
                .businessLicense(requestDTO.getBusinessLicense())
                .managerName(requestDTO.getManagerName())
                .email(requestDTO.getEmail())
                .phoneNumber(requestDTO.getPhoneNumber())
                .role(roles)
                .build();
        userRepository.save(user);
        return new ResponseEntity<>(ResponseDTO.builder().code("200").status("success").build(), HttpStatus.OK);
    }

    public ResponseEntity<?> login(UserLoginRequestDTO requestDTO) {
        if(!userRepository.existsByUserId(requestDTO.getUserId())) {
            return new ResponseEntity<>(ErrorDTO.builder().code("400").message("checkId").build(), HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findByUserId(requestDTO.getUserId());
        if(!passwordEncoder.matches(requestDTO.getPassword(),user.get().getPassword() )){
            return new ResponseEntity<>(ErrorDTO.builder().code("400").message("checkPassword").build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
