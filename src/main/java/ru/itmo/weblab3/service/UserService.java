package ru.itmo.weblab3.service;

import ru.itmo.weblab3.dto.auth.RegisterRequestDto;
import ru.itmo.weblab3.dto.auth.UserResponseDto;
import ru.itmo.weblab3.entity.Role;
import ru.itmo.weblab3.entity.User;
import ru.itmo.weblab3.exception.ConflictException;
import ru.itmo.weblab3.repository.UserRepository;
import ru.itmo.weblab3.util.Logs;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class UserService {

    private static final Logger LOG =
            Logger.getLogger(UserService.class.getName());

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordService passwordService;

    @Transactional
    public UserResponseDto register(RegisterRequestDto req) {
        String username = req.getUsername().trim();
        String email = req.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("Username already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already registered");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordService.hash(req.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        LOG.info(
                "User registered: username="
                        + Logs.clean(username)
        );

        return toDto(user);
    }

    @Transactional
    public User createAdminIfNotExists(String username, String email, String rawPassword) {
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            return existing.get();
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email.toLowerCase(Locale.ROOT));
        user.setPasswordHash(passwordService.hash(rawPassword));
        user.setRole(Role.ADMIN);

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}