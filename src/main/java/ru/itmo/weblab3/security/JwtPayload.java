package ru.itmo.weblab3.security;

import ru.itmo.weblab3.entity.Role;

import java.time.Instant;

public record JwtPayload(String username, Role role, String type, Instant expiresAt) {
}