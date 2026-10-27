package ru.itmo.weblab3.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.weblab3.dto.ErrorResponse;
import ru.itmo.weblab3.exception.AuthenticationException;

import java.util.logging.Logger;

@Provider
public class AuthenticationExceptionMapper
        implements ExceptionMapper<AuthenticationException> {

    private static final Logger LOG =
            Logger.getLogger(
                    AuthenticationExceptionMapper.class.getName()
            );

    @Override
    public Response toResponse(AuthenticationException exception) {

        LOG.warning(
                "Unauthorized (401): "
                        + exception.getMessage()
        );

        ErrorResponse error = new ErrorResponse(
                401,
                "Unauthorized",
                exception.getMessage()
        );

        return Response
                .status(Response.Status.UNAUTHORIZED)
                .header("WWW-Authenticate", "Bearer")
                .entity(error)
                .build();
    }
}
