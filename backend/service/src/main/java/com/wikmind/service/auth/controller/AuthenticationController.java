package com.wikmind.service.auth.controller;

import com.wikmind.service.auth.entity.TokenPair;
import com.wikmind.service.auth.service.TokenService;
import com.wikmind.service.common.utils.ResponseUtils;
import com.wikmind.service.common.exceptions.RequestCookieException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final TokenService tokenService;
    private final ResponseUtils responseUtils;

    public AuthenticationController(TokenService tokenService, ResponseUtils responseUtils) {
        this.tokenService = tokenService;
        this.responseUtils = responseUtils;
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refershAuthToken(@CookieValue(value = "refresh_token") String refreshToken, HttpServletResponse httpServletResponse) throws ServletRequestBindingException{
        System.out.println("REFRESH TOKEN ENDPOINT HIT ");
        if(!StringUtils.hasLength(refreshToken)){
            throw new RequestCookieException("Refresh Token");
        }
        Optional<TokenPair> tokenPairOptional = tokenService.refresh(refreshToken);

        if(tokenPairOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        TokenPair tokenPair = tokenPairOptional.get();

        responseUtils.addAuthResponseHeader(httpServletResponse, tokenPair);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<String> requestCookieMissingHandler(ServletRequestBindingException ex){
        String cookieName = "unknown";
        String message = ex.getMessage();

        if (ex instanceof MissingRequestCookieException builtinEx) {
            cookieName = builtinEx.getCookieName();
        } else if (ex instanceof RequestCookieException customEx) {
            cookieName = customEx.getCookieName();
        }
        logger.error("Request did not contain required cookie : {}",cookieName, ex);

        return new ResponseEntity<>("Request Missing cookie : "+cookieName, HttpStatus.BAD_REQUEST);

    }
}
