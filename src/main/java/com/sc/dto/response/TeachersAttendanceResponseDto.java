package com.sc.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class TeachersAttendanceResponseDto {

    private Long id;
    private Long teacherId;
    private LocalDate attendanceDate;
    private String status;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private Integer lateByMinutes;
    private String remarks;
    private String markedBy;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Boolean isProcessedForSalary;
    private String teacherName;
    private String teacherCode;

    // Constructors
    public TeachersAttendanceResponseDto() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getLateByMinutes() {
        return lateByMinutes;
    }

    public void setLateByMinutes(Integer lateByMinutes) {
        this.lateByMinutes = lateByMinutes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(String markedBy) {
        this.markedBy = markedBy;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsProcessedForSalary() {
        return isProcessedForSalary;
    }

    public void setIsProcessedForSalary(Boolean isProcessedForSalary) {
        this.isProcessedForSalary = isProcessedForSalary;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }
}
