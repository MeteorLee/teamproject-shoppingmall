package project.finalproject1backend.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_STANDBY("ROLE_STANDBY"),
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_REFUSE("ROLE_REFUSE");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
