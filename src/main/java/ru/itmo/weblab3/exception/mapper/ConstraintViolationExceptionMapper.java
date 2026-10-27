package ru.itmo.weblab3.exception.mapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.weblab3.dto.ErrorResponse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Provider
public class ConstraintViolationExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOG =
            Logger.getLogger(
                    ConstraintViolationExceptionMapper.class.getName()
            );

    @Override
    public Response toResponse(
            ConstraintViolationException exception
    ) {

        LOG.warning(
                "Validation failed (422): "
                        + exception.getMessage()
        );

        Map<String, String> details = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation
                : exception.getConstraintViolations()) {

            String path =
                    violation.getPropertyPath().toString();

            String field = path.substring(
                    path.lastIndexOf('.') + 1
            );

            details.put(
                    field,
                    violation.getMessage()
            );
        }

        ErrorResponse error = new ErrorResponse(
                422,
                "Unprocessable Entity",
                "Validation failed",
                details
        );

        return Response
                .status(422)
                .entity(error)
                .build();
    }
}