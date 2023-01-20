package com.self.uaa.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    public String getSubject(String token) {
        return getTokenClaim(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return getTokenClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return (new Date()).after(getExpirationDate(token));
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails.getUsername());
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .claim("authorities", Collections.singletonList("ADMIN"))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
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
        return Jwts.parserBuilder().setSigningKey(new SecretKeySpec(secret.getBytes(),
                SignatureAlgorithm.HS256.getJcaName())).build().parseClaimsJws(token).getBody();
    }
}

