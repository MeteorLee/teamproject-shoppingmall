package project.finalproject1backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.domain.UserRole;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = null; // db에서 유저조회
        try {
            user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new UsernameNotFoundException(userId + " -> 데이터베이스에서 찾을 수 없습니다."));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // role셋팅
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (UserRole role: user.getRole()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
        }

        return PrincipalDTO.builder()
                .id(user.getId())
                .password(user.getPassword())
                .userId(user.getUserId())
                .companyName(user.getCompanyName())
                .ownerName(user.getOwnerName())
                .corporateNumber(user.getCorporateNumber())
                .openingDate(user.getOpeningDate())
                .businessLicense(user.getBusinessLicense())
                .managerName(user.getManagerName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .modifiedAt(user.getModifiedAt())
                .createdAt(user.getCreatedAt())
                .carts(user.getCarts())
                .buyInquiry(user.getBuyInquiry())
                .saleInquiry(user.getSaleInquiry())
                .authorities(grantedAuthorities)
                .build();

    }
}
