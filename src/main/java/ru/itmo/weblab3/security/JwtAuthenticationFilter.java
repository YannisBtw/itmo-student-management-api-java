package ru.itmo.weblab3.security;

import ru.itmo.weblab3.exception.AuthenticationException;
import ru.itmo.weblab3.service.JwtService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    @Inject
    JwtService jwtService;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext ctx) {
        // Предварительные запросы CORS пропускаем
        if ("OPTIONS".equalsIgnoreCase(ctx.getMethod())) {
            return;
        }

        // Публичные эндпоинты (регистрация, логин, refresh) не требуют токена
        if (isPublic(ctx)) {
            return;
        }

        String header = ctx.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw new AuthenticationException("Missing or malformed Authorization header");
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();
        
        // Валидация токена и извлечение данных
        JwtPayload payload = jwtService.parseAccess(token);
        
        boolean secure = ctx.getSecurityContext() != null 
                && ctx.getSecurityContext().isSecure();
        
        // Устанавливаем кастомный SecurityContext для дальнейших фильтров и ресурсов
        ctx.setSecurityContext(new JwtSecurityContext(payload, secure));
    }

    private boolean isPublic(ContainerRequestContext ctx) {
        // 1. Проверяем наличие аннотации @Public на методе или классе ресурса
        if (resourceInfo != null) {
            if (resourceInfo.getResourceMethod() != null
                    && resourceInfo.getResourceMethod().isAnnotationPresent(Public.class)) {
                return true;
            }
            if (resourceInfo.getResourceClass() != null
                    && resourceInfo.getResourceClass().isAnnotationPresent(Public.class)) {
                return true;
            }
        }

        // 2. Fallback: проверяем путь напрямую. 
        // Это страхует на случай, если resourceInfo еще не готов на этапе AUTHENTICATION.
        String path = ctx.getUriInfo().getPath();
        if (path == null) return false;
        
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path.startsWith("auth/");
    }
}