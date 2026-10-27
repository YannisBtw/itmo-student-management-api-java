package ru.itmo.weblab3.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class StudentCreateDto {

    @NotBlank
    private String fullName;

    @NotNull
    @Min(100000)
    @Max(999999)
    private Integer isuId;

    @NotBlank
    @Pattern(regexp = "[A-Z][1-9][1-4][0-9]{2}")
    private String group;

    @NotNull
    @Positive
    @Max(99)
    private Integer dormitoryNumber;

    @NotNull
    @Positive
    @Max(9999)
    private Integer roomNumber;

    @NotNull
    @Past
    private LocalDate moveInDate;

    @NotNull
    private Boolean isForeign;

    private String notes;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getIsuId() {
        return isuId;
    }

    public void setIsuId(Integer isuId) {
        this.isuId = isuId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getDormitoryNumber() {
        return dormitoryNumber;
    }

    public void setDormitoryNumber(Integer dormitoryNumber) {
        this.dormitoryNumber = dormitoryNumber;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getMoveInDate() {
        return moveInDate;
    }

    public void setMoveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
    }

    public Boolean getIsForeign() {
        return isForeign;
    }

    public void setIsForeign(Boolean isForeign) {
        this.isForeign = isForeign;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}