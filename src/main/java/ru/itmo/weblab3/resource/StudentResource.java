package ru.itmo.weblab3.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.itmo.weblab3.dto.StudentCreateDto;
import ru.itmo.weblab3.dto.StudentResponseDto;
import ru.itmo.weblab3.dto.StudentUpdateDto;
import ru.itmo.weblab3.dto.StudentPatchDto;
import ru.itmo.weblab3.service.StudentService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "bearerAuth")
public class StudentResource {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Inject
    private StudentService studentService;

    @GET
    public Response getStudents(
            @QueryParam("fullName") String fullName,
            @QueryParam("isuId") Integer isuId,
            @QueryParam("group") String group,
            @QueryParam("dormitoryNumber") Integer dormitoryNumber,
            @QueryParam("roomNumber") Integer roomNumber,
            @QueryParam("moveInDate") String moveInDate,
            @QueryParam("isForeign") String isForeign,
            @QueryParam("notes") String notes,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("sortDirection") String sortDirection,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size
    ) {
        LocalDate parsedMoveInDate = parseMoveInDate(moveInDate);
        Boolean parsedIsForeign = parseIsForeign(isForeign);

        List<StudentResponseDto> students =
                studentService.findFiltered(
                        fullName,
                        isuId,
                        group,
                        dormitoryNumber,
                        roomNumber,
                        parsedMoveInDate,
                        parsedIsForeign,
                        notes,
                        sortBy,
                        sortDirection,
                        page,
                        size
                );

        return Response.ok(students).build();
    }

    @GET
    @Path("/{id}")
    public Response getStudentById(
            @PathParam("id") Long id
    ) {
        StudentResponseDto student =
                studentService.findById(id);

        return Response.ok(student).build();
    }

    @POST
    public Response createStudent(@Valid StudentCreateDto dto) {
        StudentResponseDto createdStudent = studentService.create(dto);

        return Response
                .status(Response.Status.CREATED)
                .entity(createdStudent)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateStudent(
            @PathParam("id") Long id,
            @Valid StudentUpdateDto dto
    ) {
        StudentResponseDto updatedStudent = studentService.update(id, dto);

        return Response.ok(updatedStudent).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteStudent(@PathParam("id") Long id) {
        studentService.delete(id);

        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    public Response patchStudent(
            @PathParam("id") Long id,
            @Valid StudentPatchDto dto
    ) {
        StudentResponseDto updatedStudent = studentService.patch(id, dto);

        return Response.ok(updatedStudent).build();
    }

    private LocalDate parseMoveInDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new BadRequestException(
                    "moveInDate должно быть формата DD-MM-YYYY"
            );
        }
    }

    private Boolean parseIsForeign(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        if (value.equalsIgnoreCase("true")) {
            return true;
        }

        if (value.equalsIgnoreCase("false")) {
            return false;
        }

        throw new BadRequestException(
                "isForeign должно быть true или false"
        );
    }
}