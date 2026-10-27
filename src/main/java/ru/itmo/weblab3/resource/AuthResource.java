package ru.itmo.weblab3.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.itmo.weblab3.dto.auth.LoginRequestDto;
import ru.itmo.weblab3.dto.auth.RefreshRequestDto;
import ru.itmo.weblab3.dto.auth.RegisterRequestDto;
import ru.itmo.weblab3.dto.auth.UserResponseDto;
import ru.itmo.weblab3.security.Public;
import ru.itmo.weblab3.service.AuthService;
import ru.itmo.weblab3.service.UserService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Public
public class AuthResource {

    @Inject
    UserService userService;

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequestDto req) {
        UserResponseDto dto = userService.register(req);
        return Response.status(Response.Status.CREATED).entity(dto).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequestDto req) {
        return Response.ok(authService.login(req)).build();
    }

    @POST
    @Path("/refresh")
    public Response refresh(@Valid RefreshRequestDto req) {
        return Response.ok(authService.refresh(req.getRefreshToken())).build();
    }
}