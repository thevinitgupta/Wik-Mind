package com.wikmind.service.users.controller;

import com.wikmind.service.auth.entity.AuthenticatedUser;
import com.wikmind.service.users.entity.dto.UserResponseDTO;
import com.wikmind.service.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal AuthenticatedUser principal){
        UserResponseDTO userResponseDTO = userService.fetchUserResponseByID(principal.getUserId());
        return ResponseEntity.ok(userResponseDTO);
    }

}
