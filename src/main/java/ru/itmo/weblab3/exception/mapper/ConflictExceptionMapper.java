package ru.itmo.weblab3.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.weblab3.dto.ErrorResponse;
import ru.itmo.weblab3.exception.ConflictException;
import java.util.logging.Logger;

@Provider
public class ConflictExceptionMapper
        implements ExceptionMapper<ConflictException> {

    private static final Logger LOG =
            Logger.getLogger(ConflictExceptionMapper.class.getName());

    @Override
    public Response toResponse(ConflictException exception) {
        LOG.info("Conflict (409): " + exception.getMessage());

        ErrorResponse error = new ErrorResponse(
                409,
                "Conflict",
                exception.getMessage()
        );

        return Response
                .status(Response.Status.CONFLICT)
                .entity(error)
                .build();
    }
}