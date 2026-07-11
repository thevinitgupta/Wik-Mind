package com.wikmind.service.auth.filter;

import com.wikmind.service.auth.entity.JwtPrincipal;
import com.wikmind.service.auth.service.CookieService;
import com.wikmind.service.auth.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtils jwtUtils;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final CookieService cookieService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, HandlerExceptionResolver handlerExceptionResolver, CookieService cookieService) {
        this.jwtUtils = jwtUtils;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.cookieService = cookieService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            Authentication existing =
                    SecurityContextHolder.getContext().getAuthentication();
            if (existing == null || !existing.isAuthenticated() || existing instanceof AnonymousAuthenticationToken) {
                LOGGER.info("Cookies: {}", Arrays.toString(request.getCookies()));
                String accessToken = cookieService.extractAccessToken(request);
                LOGGER.info("Access token: {}", accessToken);

                if (StringUtils.hasText(accessToken) && jwtUtils.isTokenValid(accessToken,"access")) {


                LOGGER.debug("Access Token for request : {}",accessToken);

                JwtPrincipal jwtPrincipal = jwtUtils.parseAccessToken(accessToken);

                Authentication authentication = createAuthenticationFromJwt(jwtPrincipal,request);

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                }
            }
            filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/api/auth/refresh") || path.equals("/api/auth/logout");
    }

    private Authentication createAuthenticationFromJwt(JwtPrincipal jwtPrincipal, HttpServletRequest request){
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        jwtPrincipal.getUserId(),
                        null,
                        jwtPrincipal.getAuthorities()
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request)
        );
        return authentication;
    }

}
