package ru.itmo.weblab3.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordService {

    private static final int COST = 10;

    public String hash(String raw) {
        if (raw == null || raw.isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        return BCrypt.hashpw(raw, BCrypt.gensalt(COST));
    }

    public boolean matches(String raw, String hash) {
        if (raw == null || hash == null) return false;
        try {
            return BCrypt.checkpw(raw, hash);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}