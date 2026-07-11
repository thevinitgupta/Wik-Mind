package com.wikmind.service.auth.service;

import ch.qos.logback.core.util.StringUtil;
import com.wikmind.service.auth.entity.TokenPair;
import com.wikmind.service.auth.utils.JwtUtils;
import com.wikmind.service.users.entity.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService{
    private final JwtUtils jwtUtils;

    @Getter
    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private long refreshTokenExpiration;

    @Getter
    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private long accessTokenExpiration;

    public JwtService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public TokenPair issue(User user) {
        Map<String, Object> accessTokenMap = new HashMap<>();
        accessTokenMap.put("type", "access");

        Map<String, Object> refreshTokenMap = new HashMap<>();
        refreshTokenMap.put("type", "refresh");

        String refreshToken = jwtUtils.generateToken(refreshTokenMap, user, refreshTokenExpiration);
        String accessToken = jwtUtils.generateToken(accessTokenMap, user, accessTokenExpiration);
        if(StringUtil.notNullNorEmpty(refreshToken) && StringUtil.notNullNorEmpty(accessToken)){
            return new TokenPair(refreshToken, accessToken, Instant.now().plusMillis(refreshTokenExpiration), Instant.now().plusMillis(accessTokenExpiration));
        }
        return null;
    }

    public boolean isTokenValid(String token, String expectedType) {
        try {
            String actualType = jwtUtils.extractTokenType(token);
            return expectedType.equalsIgnoreCase(actualType) && jwtUtils.isTokenValid(token, expectedType);
        } catch (Exception e) {
            return false;
        }
    }


}
