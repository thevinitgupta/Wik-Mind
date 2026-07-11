package com.wikmind.service.auth.utils;

import com.wikmind.service.auth.entity.JwtPrincipal;
import com.wikmind.service.users.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    public String extractTokenType(String token) {
        Claims tokenClaims = extractAllClaims(token);
        return tokenClaims.get("type", String.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> extraClaims, User user, Long jwtExpiration) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    public JwtPrincipal parseAccessToken(String token){
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("roles", List.class);

        Collection<GrantedAuthority> authorities =
                roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return JwtPrincipal.builder()
                .userId(UUID.fromString(claims.getSubject()))
                .tokenType("access")
                .isValid(!claims.getExpiration().before(new Date()))
                .authorities(authorities)
                .build();
    }

    public JwtPrincipal parseRefreshToken(String token){
        Claims claims = extractAllClaims(token);

        return JwtPrincipal.builder()
                .userId(UUID.fromString(claims.getSubject()))
                .tokenType("refresh")
                .isValid(!claims.getExpiration().before(new Date()))
                .build();
    }



    private String buildToken(
            Map<String, Object> extraClaims,
            User user,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim(
                        "roles",
                        user.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList()
                )
                .signWith(getSecretKeyHash(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String type) {
        try {
            return !isTokenExpired(token);
        }catch (Exception e){
            LOGGER.error("Token validation failed : {}", extractTokenType(token));
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKeyHash())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKeyHash(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
