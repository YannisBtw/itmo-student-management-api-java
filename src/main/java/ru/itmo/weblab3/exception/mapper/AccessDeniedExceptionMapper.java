package ru.itmo.weblab3.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.weblab3.dto.ErrorResponse;
import ru.itmo.weblab3.exception.AccessDeniedException;
import java.util.logging.Logger;

@Provider
public class AccessDeniedExceptionMapper
        implements ExceptionMapper<AccessDeniedException> {

    private static final Logger LOG =
            Logger.getLogger(AccessDeniedExceptionMapper.class.getName());

    @Override
    public Response toResponse(AccessDeniedException exception) {
        LOG.warning("Access denied (403): " + exception.getMessage());

        ErrorResponse error = new ErrorResponse(
                403,
                "Forbidden",
                exception.getMessage()
        );

        return Response
                .status(Response.Status.FORBIDDEN)
                .entity(error)
                .build();
    }
}
