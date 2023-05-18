package project.finalproject1backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.UserRole;
import project.finalproject1backend.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Test
    public void 관리자회원가입(){
        Set<UserRole> adminRole = new HashSet<>();
        adminRole.add(UserRole.ROLE_ADMIN);
        User user = User.builder()
                .userId("admin")
                .password(passwordEncoder.encode("123456789"))
                .role(adminRole)
                .build();
        userRepository.save(user);
    }
    @Test
    public void 유저60명생성(){
        Set<UserRole> roleUser = new HashSet<>();
        roleUser.add(UserRole.ROLE_USER);
        Set<UserRole> roleStandby = new HashSet<>();
        roleStandby.add(UserRole.ROLE_STANDBY);
        Set<UserRole> roleRefused = new HashSet<>();
        roleRefused.add(UserRole.ROLE_REFUSE);
        IntStream.rangeClosed(1,20).forEach(i->{
            User user =User.builder()
                    .userId("testUser"+i)
                    .companyName("xxx회사")
                    .managerName("김사원")
                    .role(roleUser)
                    .password(passwordEncoder.encode("123456789"))
                    .build();
            userRepository.save(user);
        });
        IntStream.rangeClosed(1,20).forEach(i->{
            User user =User.builder()
                    .userId("testUser"+(i+20))
                    .companyName("~~~회사")
                    .managerName("강사원")
                    .role(roleStandby)
                    .password(passwordEncoder.encode("123456789"))
                    .build();
            userRepository.save(user);
        });
        IntStream.rangeClosed(1,20).forEach(i->{
            User user =User.builder()
                    .userId("testuser"+(i+40))
                    .companyName("???회사")
                    .managerName("박사원")
                    .role(roleRefused)
                    .password(passwordEncoder.encode("123456789"))
                    .build();
            userRepository.save(user);
        });
    }
}
