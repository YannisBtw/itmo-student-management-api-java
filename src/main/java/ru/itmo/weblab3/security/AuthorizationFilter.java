package ru.itmo.weblab3.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.weblab3.exception.AccessDeniedException;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    private static final String STUDENTS_PREFIX = "students";

    @Override
    public void filter(ContainerRequestContext ctx) {
        String path = ctx.getUriInfo().getPath();
        if (path == null) return;
        if (path.startsWith("/")) path = path.substring(1);
        if (!path.startsWith(STUDENTS_PREFIX)) return;

        SecurityContext sc = ctx.getSecurityContext();
        if (sc == null || sc.getUserPrincipal() == null) {
            throw new AccessDeniedException("Authentication required");
        }
        String method = ctx.getMethod();
        boolean readOnly = "GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method);

        if (readOnly) {
            if (!sc.isUserInRole("USER") && !sc.isUserInRole("ADMIN")) {
                throw new AccessDeniedException("Insufficient role");
            }
            return;
        }
        if (!sc.isUserInRole("ADMIN")) {
            throw new AccessDeniedException("ADMIN role required");
        }
    }
}