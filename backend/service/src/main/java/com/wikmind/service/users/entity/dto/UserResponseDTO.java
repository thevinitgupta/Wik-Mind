package com.wikmind.service.users.entity.dto;

import com.wikmind.service.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public record UserResponseDTO(
        String id,
        String email,
        String displayName,
        String avatarUrl,
        List<String> roles
) {

    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(
                user.getId().toString(),
                user.getEmail(),
                (user.getFirstName() + " " + user.getLastName()).trim(),
                user.getProfileImageUrl(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }
}
