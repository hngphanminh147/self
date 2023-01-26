package com.self.uaa.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper implements Serializable {

    @Value("${jwt.validity.access}")
    public long ACCESS_TOKEN_VALIDITY;

    @Value("${jwt.validity.access}")
    public long REFRESH_TOKEN_VALIDITY;

    @Value("${jwt.secret.access}")
    private String ACCESS_TOKEN_SECRET;

    @Value("${jwt.secret.refresh}")
    private String REFRESH_TOKEN_SECRET;

    public String getSubject(String token) {
        return getTokenClaim(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return getTokenClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return (new Date()).after(getExpirationDate(token));
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities());
        return generateToken(claims, userDetails.getUsername(), ACCESS_TOKEN_VALIDITY, ACCESS_TOKEN_SECRET);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities());
        return generateToken(claims, userDetails.getUsername(), REFRESH_TOKEN_VALIDITY, REFRESH_TOKEN_SECRET);
    }

    public String regenerateRefreshToken(String refreshToken) {
        Claims claims = getAllTokenClaims(refreshToken);
        String subject = claims.getSubject();
        return generateToken(claims, subject, REFRESH_TOKEN_VALIDITY, REFRESH_TOKEN_SECRET);
    }

    private String generateToken(Map<String, Object> claims, String subject, Long validity, String secret) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName()))
                .compact();
    }

    private String generateToken(Claims claims, String subject, Long validity, String secret) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName()))
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(getSubject(token)) && !isTokenExpired(token);
    }

    public <T> T getTokenClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllTokenClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllTokenClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(new SecretKeySpec(REFRESH_TOKEN_SECRET.getBytes(),
                SignatureAlgorithm.HS256.getJcaName())).build().parseClaimsJws(token).getBody();
    }
}

