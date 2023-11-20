package com.jwt.impl.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import java.util.UUID;

@Component
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public String generateToken(String email) {
        return Jwts.builder()
                .header()
                .keyId(UUID.randomUUID().toString())
                .and()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token, UserPrincipal userPrincipal) {
        Claims payload = claims(token);
        String email = payload.getSubject();
        Date expirationClaim = payload.getExpiration();
        return email.equals(userPrincipal.getEmail()) && !isTokenExpired(expirationClaim);
    }

    public String extractEmail(String token) {
        return claims(token).getSubject();
    }

    private Boolean isTokenExpired(Date expirationClaim) {
        return expirationClaim.before(new Date());
    }

    private Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
