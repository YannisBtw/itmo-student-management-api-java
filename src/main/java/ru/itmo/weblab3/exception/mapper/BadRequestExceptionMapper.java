package ru.itmo.weblab3.exception.mapper;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.weblab3.dto.ErrorResponse;
import java.util.logging.Logger;

@Provider
public class BadRequestExceptionMapper
        implements ExceptionMapper<BadRequestException> {

    private static final Logger LOG =
            Logger.getLogger(BadRequestExceptionMapper.class.getName());

    @Override
    public Response toResponse(BadRequestException exception) {
        LOG.info("Bad request (400): " + exception.getMessage());

        ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
    }
}