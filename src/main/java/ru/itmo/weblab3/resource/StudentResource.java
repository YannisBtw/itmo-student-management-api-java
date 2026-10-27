package ru.itmo.weblab3.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import ru.itmo.weblab3.dto.StudentCreateDto;
import ru.itmo.weblab3.dto.StudentResponseDto;
import ru.itmo.weblab3.dto.StudentUpdateDto;
import ru.itmo.weblab3.dto.StudentPatchDto;
import ru.itmo.weblab3.service.StudentService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;


@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentResource {

    private static final Logger LOG =
            Logger.getLogger(StudentResource.class.getName());

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Inject
    private StudentService studentService;

    @Context
    SecurityContext securityContext;

    private String actor() {
        Principal principal = securityContext == null
                ? null
                : securityContext.getUserPrincipal();

        return principal == null ? "anonymous" : principal.getName();
    }

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

        LOG.info("Student created: id=" + createdStudent.getId()
                + ", isuId=" + createdStudent.getIsuId()
                + ", by=" + actor());

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

        LOG.info("Student updated (PUT): id=" + id + ", by=" + actor());

        return Response.ok(updatedStudent).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteStudent(@PathParam("id") Long id) {
        studentService.delete(id);

        LOG.info("Student deleted: id=" + id + ", by=" + actor());

        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    public Response patchStudent(
            @PathParam("id") Long id,
            @Valid StudentPatchDto dto
    ) {
        StudentResponseDto updatedStudent = studentService.patch(id, dto);

        LOG.info("Student updated (PATCH): id=" + id + ", by=" + actor());

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