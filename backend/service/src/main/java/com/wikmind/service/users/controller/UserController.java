package com.wikmind.service.users.controller;

import com.wikmind.service.users.entity.User;
import com.wikmind.service.users.entity.dto.UserResponseDTO;
import com.wikmind.service.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication){
        System.out.println("REQUEST RECEIVED FOR USER");
        try {
            if(authentication.isAuthenticated()){
                UUID authenticatedUserID = UUID.fromString(Objects.requireNonNull(authentication.getPrincipal()).toString());

                Optional<User> user = userService.fetchUserById(authenticatedUserID);
                if(user.isEmpty()) {
                    throw new UsernameNotFoundException("User with ID does not exist or has been deleted");
                }

                UserResponseDTO userResponseDTO = UserResponseDTO.fromUser(user.get());
                System.out.println("USER FETCHED : "+user.get().getEmail()+","+userResponseDTO);
                return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

            } else {
                throw new AuthenticationServiceException("User not authenticated");
            }
        } catch (NullPointerException | AuthenticationServiceException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (UsernameNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
