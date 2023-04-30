package project.finalproject1backend.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.finalproject1backend.domain.Inquiry;
import project.finalproject1backend.domain.Orders;
import project.finalproject1backend.domain.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Builder
@Data

public class PrincipalDTO implements UserDetails {
    private Long id;
    private String userId;
    private String password;
    private String ownerName;
    private LocalDate openingDate;
    private String corporateNumber;
    private String businessLicense;
    private String managerName;
    private String email;
    private String phoneNumber;
    private List<Orders> orders;
    private List<Inquiry> inquiries;
    private Set<UserRole> role ;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Collection<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return  true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
