package project.finalproject1backend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.finalproject1backend.domain.Inquiry;
import project.finalproject1backend.domain.Order;
import project.finalproject1backend.domain.UserRole;
import project.finalproject1backend.util.Encrypt256;

import javax.persistence.*;
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
    private List<Order> orders;
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
