package ru.itmo.weblab3.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private Integer isuId;

    @Column(name = "student_group", nullable = false)
    private String group;

    @Column(nullable = false)
    private Integer dormitoryNumber;

    @Column(nullable = false)
    private Integer roomNumber;

    @Column(nullable = false)
    private LocalDate moveInDate;

    @Column(nullable = false)
    private Boolean isForeign;

    private String notes;

    protected Student() {
    }

    public Student(
            String fullName,
            Integer isuId,
            String group,
            Integer dormitoryNumber,
            Integer roomNumber,
            LocalDate moveInDate,
            Boolean isForeign,
            String notes
    ) {
        this.fullName = fullName;
        this.isuId = isuId;
        this.group = group;
        this.dormitoryNumber = dormitoryNumber;
        this.roomNumber = roomNumber;
        this.moveInDate = moveInDate;
        this.isForeign = isForeign;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getIsuId() {
        return isuId;
    }

    public String getGroup() {
        return group;
    }

    public Integer getDormitoryNumber() {
        return dormitoryNumber;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getMoveInDate() {
        return moveInDate;
    }

    public Boolean getIsForeign() {
        return isForeign;
    }

    public String getNotes() {
        return notes;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setIsuId(Integer isuId) {
        this.isuId = isuId;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setDormitoryNumber(Integer dormitoryNumber) {
        this.dormitoryNumber = dormitoryNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setMoveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
    }

    public void setIsForeign(Boolean isForeign) {
        this.isForeign = isForeign;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}