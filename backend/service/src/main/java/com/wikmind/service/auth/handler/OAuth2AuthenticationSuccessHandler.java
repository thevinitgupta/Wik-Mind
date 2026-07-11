package com.wikmind.service.auth.handler;

import com.wikmind.service.auth.entity.TokenPair;
import com.wikmind.service.auth.service.IdentityService;
import com.wikmind.service.auth.service.TokenService;
import com.wikmind.service.common.utils.ResponseUtils;
import com.wikmind.service.users.entity.User;
import com.wikmind.service.users.entity.external.ExternalUser;
import com.wikmind.service.users.mapper.OAuth2Mapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Map<String, OAuth2Mapper> mappers;
    private final IdentityService identityService;
    private final TokenService tokenService;
    private final ResponseUtils responseUtils;

    @Value("${LOGIN_SUCCESS_URL}")
    private String loginSuccessURL;

    public OAuth2AuthenticationSuccessHandler(Map<String, OAuth2Mapper> mappers, IdentityService identityService, TokenService tokenService, ResponseUtils responseUtils) {
        this.mappers = mappers;
        this.identityService = identityService;
        this.tokenService = tokenService;
        this.responseUtils = responseUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthToken = (OAuth2AuthenticationToken) authentication;

        String provider = oAuth2AuthToken.getAuthorizedClientRegistrationId();
        OAuth2Mapper mapper = mappers.get(provider);

        ExternalUser externalUser = mapper.map(oAuth2AuthToken.getPrincipal());
        System.out.println("OAuth success");

        System.out.println("Provider = " + provider);

        System.out.println("External User = " + externalUser);

        Optional<User> user = identityService.login(externalUser);

        System.out.println("User Present = " + user.isPresent());

        if(user.isPresent()){
            System.out.println("Issuing JWT");
            TokenPair tokenPair = tokenService.issue(user.get());
            responseUtils.addAuthResponseHeader(response,tokenPair);
            response.sendRedirect(loginSuccessURL);
        }
        else {
            System.out.println("Login failed");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Login Failed");
        }

    }


}
