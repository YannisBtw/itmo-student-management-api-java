package ru.itmo.weblab3.dto;

import java.time.LocalDate;

public class StudentResponseDto {

    private final Long id;
    private final String fullName;
    private final Integer isuId;
    private final String group;
    private final Integer dormitoryNumber;
    private final Integer roomNumber;
    private final LocalDate moveInDate;
    private final Boolean isForeign;
    private final String notes;

    public StudentResponseDto(
            Long id,
            String fullName,
            Integer isuId,
            String group,
            Integer dormitoryNumber,
            Integer roomNumber,
            LocalDate moveInDate,
            Boolean isForeign,
            String notes
    ) {
        this.id = id;
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
}