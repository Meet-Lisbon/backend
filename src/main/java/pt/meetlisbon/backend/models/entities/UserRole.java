package pt.meetlisbon.backend.models.entities;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    admin,
    user;

    public String getAuthority() {
        return name();
    }
}
