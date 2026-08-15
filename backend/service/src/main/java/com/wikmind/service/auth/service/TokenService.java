package com.wikmind.service.auth.service;


import com.wikmind.service.auth.entity.RefreshToken;
import com.wikmind.service.auth.entity.TokenPair;
import com.wikmind.service.auth.repository.RefreshTokenRepository;
import com.wikmind.service.auth.service.interfaces.TokenHashService;
import com.wikmind.service.users.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

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

    public TokenPair issue(User user) {
        TokenPair tokenPair = jwtService.issue(user);
        if (tokenPair != null) {
            RefreshToken refreshToken = RefreshToken.builder().expiresAt(tokenPair.refreshTokenExpiresAt()).user(user).revoked(false).tokenHash(tokenHashService.hash(tokenPair.refreshToken())).build();
            refreshTokenRepository.save(refreshToken);
        }
        return tokenPair;
    }


    @Transactional
    public Optional<TokenPair> refresh(String refreshToken) {

        if (!jwtService.isTokenValid(refreshToken, "refresh")) {
            return Optional.empty();
        }

        String hash = tokenHashService.hash(refreshToken);

        Optional<RefreshToken> storedTokenOpt = refreshTokenRepository.findByTokenHash(hash);

        if (storedTokenOpt.isEmpty()) {
            logger.warn("Refresh token not found");
            return Optional.empty();
        }

        RefreshToken storedToken = storedTokenOpt.get();
        User user = storedToken.getUser();

        if (storedToken.isRevoked()) {
            logger.error("Refresh token reuse detected for user {}", user.getEmail());

            revokeAll(user);
            return Optional.empty();
        }

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            return Optional.empty();
        }
        storedToken.setRevoked(true);
        storedToken.setRevokedAt(Instant.now());

        refreshTokenRepository.save(storedToken);

        return Optional.ofNullable(issue(user));
    }

    @Transactional
    public void revokeAll(User user) {
        refreshTokenRepository.revokeAllByUserId(user.getId(), Instant.now());
    }

}
