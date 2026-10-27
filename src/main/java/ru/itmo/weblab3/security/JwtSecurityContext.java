package ru.itmo.weblab3.security;

import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class JwtSecurityContext implements SecurityContext {

    private final JwtPayload payload;
    private final boolean secure;

    public JwtSecurityContext(JwtPayload payload, boolean secure) {
        this.payload = payload;
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> payload.username();
    }

    @Override
    public boolean isUserInRole(String role) {
        return payload.role() != null && payload.role().name().equals(role);
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }

    public JwtPayload getPayload() {
        return payload;
    }
}