package com.cdwater.movietimecommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Jwt工具
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Long id, Map<String, Object> claims, long tokenTTL) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setSubject(id.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenTTL))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getTokenRemainingTime(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    public String getJti(String token) {
        return parseToken(token).getId();
    }


//    public String generateAccessToken(Long userId, String phone) {
//        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//
//        return Jwts.builder()
//                .setId(UUID.randomUUID().toString())
//                .setSubject(userId.toString())
//                .claim(TextConstant.PHONE, phone)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + adminTokenTTL))
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public Claims parseToken(String token) {
//        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public Claims parseTokenAllowExpired(String token) {
//        try {
//            return parseToken(token);
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }
}
