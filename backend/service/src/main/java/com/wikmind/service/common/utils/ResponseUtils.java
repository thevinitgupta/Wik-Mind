package com.wikmind.service.common.utils;

import com.wikmind.service.auth.entity.TokenPair;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ResponseUtils {
    public void addAuthResponseHeader(HttpServletResponse response, TokenPair tokenPair){
        ResponseCookie accessCookie =
                ResponseCookie.from("access_token",
                                tokenPair.accessToken())
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(Duration.ofMinutes(15))
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                accessCookie.toString()
        );

        ResponseCookie refreshCookie =
                ResponseCookie.from("refresh_token",
                                tokenPair.refreshToken())
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("Strict")
                        .path("/api/auth/refresh")
                        .maxAge(Duration.ofDays(30))
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
        );
    }

    private void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie clearRefresh = ResponseCookie.from("refresh_token", "")
                .httpOnly(true).secure(true).path("/api/v1/auth").maxAge(0).sameSite("Strict").build();

        ResponseCookie clearAccess = ResponseCookie.from("access_token", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).sameSite("Strict").build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());
    }

}
