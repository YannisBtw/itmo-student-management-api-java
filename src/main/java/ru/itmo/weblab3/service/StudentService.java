package ru.itmo.weblab3.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import ru.itmo.weblab3.exception.ConflictException;
import ru.itmo.weblab3.dto.StudentCreateDto;
import ru.itmo.weblab3.dto.StudentResponseDto;
import ru.itmo.weblab3.dto.StudentUpdateDto;
import ru.itmo.weblab3.dto.StudentPatchDto;
import ru.itmo.weblab3.entity.Student;
import ru.itmo.weblab3.repository.StudentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class StudentService {

    @Inject
    private StudentRepository studentRepository;

    public List<StudentResponseDto> findFiltered(
            String fullName,
            Integer isuId,
            String group,
            Integer dormitoryNumber,
            Integer roomNumber,
            LocalDate moveInDate,
            Boolean isForeign,
            String notes,
            String sortBy,
            String sortDirection,
            Integer page,
            Integer size
    ) {

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        }

        if (sortDirection == null || sortDirection.isBlank()) {
            sortDirection = "asc";
        }

        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 20;
        }

        if (isuId != null && (isuId < 100000 || isuId > 999999)) {
            throw new BadRequestException(
                    "ISU ID должен представлять собой шестизначное число"
            );
        }

        if (dormitoryNumber != null
                && (dormitoryNumber < 1 || dormitoryNumber > 99)) {
            throw new BadRequestException(
                    "dormitoryNumber должен быть в диапазоне от 1 до 99"
            );
        }

        if (roomNumber != null
                && (roomNumber < 1 || roomNumber > 9999)) {
            throw new BadRequestException(
                    "roomNumber должен быть в диапазоне от 1 до 9999"
            );
        }

        if (!sortBy.equals("id")
                && !sortBy.equals("fullName")
                && !sortBy.equals("isuId")
                && !sortBy.equals("group")
                && !sortBy.equals("dormitoryNumber")
                && !sortBy.equals("roomNumber")
                && !sortBy.equals("moveInDate")
                && !sortBy.equals("isForeign")
                && !sortBy.equals("notes")) {

            throw new BadRequestException(
                    "Неизвестное поле для сортировки"
            );
        }

        if (!sortDirection.equalsIgnoreCase("asc")
                && !sortDirection.equalsIgnoreCase("desc")) {
            throw new BadRequestException(
                    "sortDirection должно принимать значение asc или desc"
            );
        }

        if (page < 0) {
            throw new BadRequestException(
                    "page должна быть больше или равна 0"
            );
        }

        if (size < 1 || size > 100) {
            throw new BadRequestException(
                    "size должен быть в диапазоне от 1 до 100"
            );
        }

        List<Student> students = studentRepository.findFiltered(
                fullName,
                isuId,
                group,
                dormitoryNumber,
                roomNumber,
                moveInDate,
                isForeign,
                notes,
                sortBy,
                sortDirection,
                page,
                size
        );

        List<StudentResponseDto> result = new ArrayList<>();

        for (Student student : students) {
            result.add(toResponseDto(student));
        }

        return result;
    }

    private void checkIsuId(
            Integer isuId,
            Long currentStudentId
    ) {
        Student existingStudent = studentRepository
                .findByIsuId(isuId)
                .orElse(null);

        if (existingStudent != null
                && !existingStudent.getId().equals(currentStudentId)) {
            throw new ConflictException(
                    "Студент с таким ISU ID уже существует"
            );
        }
    }

    @Transactional
    public StudentResponseDto create(StudentCreateDto dto) {
        checkIsuId(dto.getIsuId(), null);

        Student student = new Student(
                dto.getFullName(),
                dto.getIsuId(),
                dto.getGroup(),
                dto.getDormitoryNumber(),
                dto.getRoomNumber(),
                dto.getMoveInDate(),
                dto.getIsForeign(),
                dto.getNotes() == null ? "" : dto.getNotes()
        );

        Student savedStudent = studentRepository.save(student);

        return toResponseDto(savedStudent);
    }

    private StudentResponseDto toResponseDto(Student student) {
        return new StudentResponseDto(
                student.getId(),
                student.getFullName(),
                student.getIsuId(),
                student.getGroup(),
                student.getDormitoryNumber(),
                student.getRoomNumber(),
                student.getMoveInDate(),
                student.getIsForeign(),
                student.getNotes()
        );
    }

    public StudentResponseDto findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Student not found")
                );

        return toResponseDto(student);
    }

    @Transactional
    public StudentResponseDto update(Long id, StudentUpdateDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Student not found")
                );

        checkIsuId(dto.getIsuId(), id);

        student.setFullName(dto.getFullName());
        student.setIsuId(dto.getIsuId());
        student.setGroup(dto.getGroup());
        student.setDormitoryNumber(dto.getDormitoryNumber());
        student.setRoomNumber(dto.getRoomNumber());
        student.setMoveInDate(dto.getMoveInDate());
        student.setIsForeign(dto.getIsForeign());
        student.setNotes(
                dto.getNotes() == null ? "" : dto.getNotes()
        );

        return toResponseDto(student);
    }

    @Transactional
    public StudentResponseDto patch(Long id, StudentPatchDto dto) {
        if (dto.getIsuId() != null) {
            checkIsuId(dto.getIsuId(), id);
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Student not found")
                );

        if (dto.getFullName() != null) {
            student.setFullName(dto.getFullName());
        }

        if (dto.getIsuId() != null) {
            student.setIsuId(dto.getIsuId());
        }

        if (dto.getGroup() != null) {
            student.setGroup(dto.getGroup());
        }

        if (dto.getDormitoryNumber() != null) {
            student.setDormitoryNumber(dto.getDormitoryNumber());
        }

        if (dto.getRoomNumber() != null) {
            student.setRoomNumber(dto.getRoomNumber());
        }

        if (dto.getMoveInDate() != null) {
            student.setMoveInDate(dto.getMoveInDate());
        }

        if (dto.getIsForeign() != null) {
            student.setIsForeign(dto.getIsForeign());
        }

        if (dto.getNotes() != null) {
            student.setNotes(dto.getNotes());
        }

        return toResponseDto(student);
    }

    @Transactional
    public void delete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Student not found")
                );

        studentRepository.delete(student);
    }

}