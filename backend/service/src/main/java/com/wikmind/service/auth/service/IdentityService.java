package com.wikmind.service.auth.service;

import com.wikmind.service.users.entity.ConnectedAccount;
import com.wikmind.service.users.entity.User;
import com.wikmind.service.users.entity.external.ExternalUser;
import com.wikmind.service.users.repository.ConnectedAccountRepository;
import com.wikmind.service.users.repository.UserRepository;
import com.wikmind.service.users.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class IdentityService {

    private final UserService userService;
    private final ConnectedAccountRepository connectedAccountRepository;

    public IdentityService(UserService userService, ConnectedAccountRepository connectedAccountRepository) {
        this.userService = userService;
        this.connectedAccountRepository = connectedAccountRepository;
    }

    public Optional<User> login(@NotNull ExternalUser externalUser){
        Optional<ConnectedAccount> connectedAccountOptional = connectedAccountRepository.findByProviderAndProviderId(externalUser.getProvider(), externalUser.getProviderUserId());
        System.out.println("Connected Account check for : "+ externalUser.getEmail());

        if(connectedAccountOptional.isEmpty()) {
            System.out.println("Connected Account empty : "+ externalUser.getEmail());
            return this.register(externalUser);
        }

        User user = connectedAccountOptional.get().getUser();
        System.out.println("Connected Account user : "+ (user!=null ? user.getEmail() : "NOT FOUND"));
        return Optional.of(user);
    }

    public Optional<User> register(@NotNull ExternalUser externalUser) {
        User savedUser = userService.createNewUserFromExternalUser(externalUser);
        ConnectedAccount connectedAccount = createConnectedAccount(savedUser, externalUser);
        return Optional.of(savedUser);
    }

    private ConnectedAccount createConnectedAccount(User user, ExternalUser externalUser){
        ConnectedAccount connectedAccount = new ConnectedAccount();
        connectedAccount.setProvider(externalUser.getProvider());
        connectedAccount.setProviderUserId(externalUser.getProviderUserId());
        connectedAccount.setUser(user);
        connectedAccount.setConnectedAt(LocalDateTime.now());
        connectedAccount.setRefreshToken("");
        return connectedAccountRepository.save(connectedAccount);
    }


}
