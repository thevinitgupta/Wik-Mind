package com.wikmind.service.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CookieService {
    public String extractAccessToken(HttpServletRequest request){
        Optional<Cookie> accessTokenCookie = Arrays.stream(request.getCookies()).filter(cookie -> {
            return cookie.getName().equalsIgnoreCase("access_token");
        }).findFirst();

        return accessTokenCookie.map(Cookie::getValue).orElse(null);
    }
    public String extractRefreshToken(HttpServletRequest request){
        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies()).filter(cookie -> {
            return cookie.getName().equalsIgnoreCase("refresh_token");
        }).findFirst();

        return refreshTokenCookie.map(Cookie::getValue).orElse(null);
    }
}
