package com.wikmind.service.auth.entity;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Data
@Builder
public class AuthenticatedUser {
    UUID userId;
    String email;
    @Nullable
    Collection<? extends GrantedAuthority> authorities;
    boolean isValid;
    String tokenType;
}

