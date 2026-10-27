package ru.itmo.weblab3.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import ru.itmo.weblab3.config.ConfigKeys;
import ru.itmo.weblab3.config.ConfigProvider;
import ru.itmo.weblab3.entity.Role;
import ru.itmo.weblab3.entity.User;
import ru.itmo.weblab3.exception.AuthenticationException;
import ru.itmo.weblab3.security.JwtPayload;

import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class JwtService {

    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";

    private final SecretKey key;
    private final long accessTtl;
    private final long refreshTtl;

    public JwtService() {
        String secret = ConfigProvider.get(ConfigKeys.JWT_SECRET);
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 32 bytes");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtl = ConfigProvider.getLong(ConfigKeys.JWT_ACCESS_TTL_SECONDS);
        this.refreshTtl = ConfigProvider.getLong(ConfigKeys.JWT_REFRESH_TTL_SECONDS);
    }

    public long getAccessTtlSeconds() { return accessTtl; }
    public long getRefreshTtlSeconds() { return refreshTtl; }

    public String generateAccess(User user) {
        return buildToken(user, TYPE_ACCESS, accessTtl);
    }

    public String generateRefresh(User user) {
        return buildToken(user, TYPE_REFRESH, refreshTtl);
    }

    private String buildToken(User user, String type, long ttlSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .claim("type", type)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(key)
                .compact();
    }

    public JwtPayload parseAccess(String token) {
        return parse(token, TYPE_ACCESS);
    }

    public JwtPayload parseRefresh(String token) {
        return parse(token, TYPE_REFRESH);
    }

    private JwtPayload parse(String token, String expectedType) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String type = claims.get("type", String.class);
            if (!expectedType.equals(type)) {
                throw new AuthenticationException("Invalid token type");
            }
            return new JwtPayload(
                    claims.getSubject(),
                    Role.valueOf(claims.get("role", String.class)),
                    type,
                    claims.getExpiration().toInstant()
            );
        } catch (JwtException | IllegalArgumentException ex) {
            throw new AuthenticationException("Invalid or expired token");
        }
    }

    public String hashToken(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
}