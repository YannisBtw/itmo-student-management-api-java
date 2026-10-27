package ru.itmo.weblab3.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ru.itmo.weblab3.entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StudentRepository {

    @PersistenceContext(unitName = "studentPU")
    private EntityManager entityManager;

    public List<Student> findAll() {
        return entityManager
                .createQuery("SELECT s FROM Student s", Student.class)
                .getResultList();
    }

    public List<Student> findFiltered(
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
        StringBuilder jpql = new StringBuilder(
                "SELECT s FROM Student s WHERE 1 = 1"
        );

        if (fullName != null && !fullName.isBlank()) {
            jpql.append(
                    " AND LOWER(s.fullName) LIKE LOWER(:fullName)"
            );
        }

        if (isuId != null) {
            jpql.append(
                    " AND s.isuId = :isuId"
            );
        }

        if (group != null && !group.isBlank()) {
            jpql.append(
                    " AND LOWER(s.group) LIKE LOWER(:group)"
            );
        }

        if (dormitoryNumber != null) {
            jpql.append(
                    " AND s.dormitoryNumber = :dormitoryNumber"
            );
        }

        if (roomNumber != null) {
            jpql.append(
                    " AND s.roomNumber = :roomNumber"
            );
        }

        if (moveInDate != null) {
            jpql.append(
                    " AND s.moveInDate = :moveInDate"
            );
        }

        if (isForeign != null) {
            jpql.append(
                    " AND s.isForeign = :isForeign"
            );
        }

        if (notes != null && !notes.isBlank()) {
            jpql.append(
                    " AND LOWER(s.notes) LIKE LOWER(:notes)"
            );
        }

        String sortField = switch (sortBy) {
            case "fullName" -> "s.fullName";
            case "isuId" -> "s.isuId";
            case "group" -> "s.group";
            case "dormitoryNumber" -> "s.dormitoryNumber";
            case "roomNumber" -> "s.roomNumber";
            case "moveInDate" -> "s.moveInDate";
            case "isForeign" -> "s.isForeign";
            case "notes" -> "s.notes";
            default -> "s.id";
        };

        jpql.append(
                " ORDER BY "
                        + sortField
                        + " "
                        + sortDirection.toUpperCase()
        );

        TypedQuery<Student> query = entityManager.createQuery(
                jpql.toString(),
                Student.class
        );

        if (fullName != null && !fullName.isBlank()) {
            query.setParameter(
                    "fullName",
                    "%" + fullName.trim() + "%"
            );
        }

        if (isuId != null) {
            query.setParameter("isuId", isuId);
        }

        if (group != null && !group.isBlank()) {
            query.setParameter(
                    "group",
                    "%" + group.trim() + "%"
            );
        }

        if (dormitoryNumber != null) {
            query.setParameter(
                    "dormitoryNumber",
                    dormitoryNumber
            );
        }

        if (roomNumber != null) {
            query.setParameter(
                    "roomNumber",
                    roomNumber
            );
        }

        if (moveInDate != null) {
            query.setParameter(
                    "moveInDate",
                    moveInDate
            );
        }

        if (isForeign != null) {
            query.setParameter(
                    "isForeign",
                    isForeign
            );
        }

        if (notes != null && !notes.isBlank()) {
            query.setParameter(
                    "notes",
                    "%" + notes.trim() + "%"
            );
        }

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(
                entityManager.find(Student.class, id)
        );
    }

    public Optional<Student> findByIsuId(Integer isuId) {
        return entityManager
                .createQuery(
                        "SELECT s FROM Student s WHERE s.isuId = :isuId",
                        Student.class
                )
                .setParameter("isuId", isuId)
                .getResultStream()
                .findFirst();
    }

    public Student save(Student student) {
        entityManager.persist(student);
        entityManager.flush();

        return student;
    }

    public void delete(Student student) {
        entityManager.remove(student);
    }
}