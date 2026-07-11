package com.wikmind.service.auth.entity;

import java.time.Instant;

public record TokenPair(
        String refreshToken,
        String accessToken,
        Instant refreshTokenExpiresAt,
        Instant accessTokenExpiresAt
) {}
