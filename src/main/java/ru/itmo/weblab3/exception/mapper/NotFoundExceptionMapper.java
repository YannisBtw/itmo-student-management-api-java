package ru.itmo.weblab3.exception.mapper;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.weblab3.dto.ErrorResponse;
import java.util.logging.Logger;

@Provider
public class NotFoundExceptionMapper
        implements ExceptionMapper<NotFoundException> {

    private static final Logger LOG =
            Logger.getLogger(NotFoundExceptionMapper.class.getName());

    @Override
    public Response toResponse(NotFoundException exception) {
        LOG.info("Not found (404): " + exception.getMessage());

        ErrorResponse error = new ErrorResponse(
                404,
                "Not Found",
                exception.getMessage()
        );

        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}