package ru.itmo.weblab3.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Student Management API",
                version = "1.0.0",
                description = "REST API для управления студентами, пользователями и авторизацией"
        ),
        servers = {
                @Server(
                        url = "/web-lab-3",
                        description = "Local GlassFish server"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@ApplicationPath("/api")
public class RestApplication extends Application {
}