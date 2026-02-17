package com.cdwater.movietimecommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

    /**
     * 生成token
     *
     * @param id       主题
     * @param claims   载荷
     * @param tokenTTL token有效期
     * @return 生成的token
     */
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

    /**
     * 解析token
     *
     * @param token 要解析的token
     * @return 解析后的载荷
     */
    public Claims parseToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token（即使过期）
     *
     * @param token 要解析的token
     * @return 解析后的载荷
     */
    public Claims parseTokenAllowExpired(String token) {
        try {
            return parseToken(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
