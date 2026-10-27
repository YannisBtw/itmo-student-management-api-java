package ru.itmo.weblab3.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.itmo.weblab3.dto.auth.LoginRequestDto;
import ru.itmo.weblab3.dto.auth.TokenResponseDto;
import ru.itmo.weblab3.entity.RefreshToken;
import ru.itmo.weblab3.entity.User;
import ru.itmo.weblab3.exception.AuthenticationException;
import ru.itmo.weblab3.repository.RefreshTokenRepository;
import ru.itmo.weblab3.repository.UserRepository;
import ru.itmo.weblab3.util.Logs;


import java.time.Instant;
import java.util.Locale;
import java.util.logging.Logger;

@ApplicationScoped
public class AuthService {

    private static final Logger LOG =
            Logger.getLogger(AuthService.class.getName());

    @Inject
    UserRepository userRepository;

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    @Inject
    PasswordService passwordService;

    @Inject
    JwtService jwtService;

    @Transactional
    public TokenResponseDto login(LoginRequestDto req) {
        String login = req.getUsernameOrEmail().trim();

        User user = userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(
                        login.toLowerCase(Locale.ROOT)
                ))
                .orElseThrow(() -> {
                    LOG.warning(
                            "Login failed: user=" + Logs.clean(login)
                    );

                    return new AuthenticationException(
                            "Invalid credentials"
                    );
                });

        if (!passwordService.matches(
                req.getPassword(),
                user.getPasswordHash()
        )) {
            LOG.warning(
                    "Login failed: invalid password for user="
                            + Logs.clean(user.getUsername())
            );

            throw new AuthenticationException(
                    "Invalid credentials"
            );
        }

        LOG.info(
                "Login successful: user="
                        + Logs.clean(user.getUsername())
                        + ", role="
                        + user.getRole()
        );

        return issueTokens(user);
    }

    @Transactional
    public TokenResponseDto refresh(String refreshTokenRaw) {
        jwtService.parseRefresh(refreshTokenRaw);
        String hash = jwtService.hashToken(refreshTokenRaw);
        RefreshToken stored = refreshTokenRepository.findByHash(hash)
                .orElseThrow(() -> new AuthenticationException("Refresh token not recognized"));

        if (stored.isRevoked()) {
            throw new AuthenticationException("Refresh token revoked");
        }
        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new AuthenticationException("Refresh token expired");
        }

        // rotation: revoke old, issue new
        refreshTokenRepository.markRevoked(stored);
        LOG.info(
                "Refresh token rotated: user="
                        + Logs.clean(stored.getUser().getUsername())
        );
        return issueTokens(stored.getUser());
    }

    private TokenResponseDto issueTokens(User user) {
        String access = jwtService.generateAccess(user);
        String refresh = jwtService.generateRefresh(user);

        RefreshToken stored = new RefreshToken();
        stored.setUser(user);
        stored.setTokenHash(jwtService.hashToken(refresh));
        stored.setExpiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()));
        stored.setRevoked(false);
        refreshTokenRepository.save(stored);

        return new TokenResponseDto(access, refresh, jwtService.getAccessTtlSeconds());
    }
}