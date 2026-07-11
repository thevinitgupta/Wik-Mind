package com.wikmind.service.auth.service;


import com.wikmind.service.auth.entity.RefreshToken;
import com.wikmind.service.auth.repository.RefreshTokenRepository;
import com.wikmind.service.auth.service.interfaces.TokenHashService;
import com.wikmind.service.users.entity.User;
import com.wikmind.service.auth.entity.TokenPair;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    private final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final TokenHashService tokenHashService;

    public TokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService, TokenHashService tokenHashService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.tokenHashService = tokenHashService;
    }

    public TokenPair issue(User user){
        TokenPair tokenPair = jwtService.issue(user);
        if(tokenPair!=null){
            RefreshToken refreshToken = RefreshToken.builder()
                    .expiresAt(tokenPair.refreshTokenExpiresAt())
                    .user(user)
                    .revoked(false)
                    .tokenHash(tokenHashService.hash(tokenPair.refreshToken()))
                    .build();
            refreshTokenRepository.save(refreshToken);
        }
        return tokenPair;
    }


    public Optional<TokenPair> refresh(String refreshToken){
        if(!jwtService.isTokenValid(refreshToken,"refresh")){
            return Optional.empty();
        }

        String refreshTokenHash = tokenHashService.hash(refreshToken);
        Optional<RefreshToken> storedTokenOpt = refreshTokenRepository.findByTokenHash(refreshTokenHash);

        if(storedTokenOpt.isEmpty()){
            return Optional.empty();
        }

        RefreshToken storedToken = storedTokenOpt.get();
        User user = storedToken.getUser();
        if(storedToken.isRevoked()){
            logger.error("Breach Detected for user : {}, revoking all tokens", user.getEmail());
            revokeAll(user);
            return Optional.empty();
        }

        // refresh token rotation
        storedToken.setRevoked(true);
        storedToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(storedToken);

        TokenPair tokenPair = jwtService.issue(user);
        return Optional.ofNullable(tokenPair);
    }

    @Transactional
    public void revokeAll(User user){
        refreshTokenRepository.revokeAllByUserId(user.getId(), Instant.now());
    }

}
