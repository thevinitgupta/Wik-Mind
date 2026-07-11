package com.wikmind.service.users.service;

import com.wikmind.service.users.entity.User;
import com.wikmind.service.users.entity.external.ExternalUser;
import com.wikmind.service.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createNewUserFromExternalUser(ExternalUser externalUser){
        User user = new User();
        user.setEmail(externalUser.getEmail());
        user.setFirstName(externalUser.getFirstName());
        user.setLastName(externalUser.getLastName());
        user.setProfileImageUrl(externalUser.getAvatarUrl());
        return userRepository.save(user);
    }

    public Optional<User> fetchUserById(UUID userId){
        return userRepository.findById(userId);
    }
}
