package com.sc.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public class TeachersAttendanceRequestDto {

    private Long teacherId;
    private LocalDate attendanceDate;
    private String status; // Present, Absent, Late, Half Day, On Leave
    private LocalTime timeIn;
    private LocalTime timeOut;
    private Integer lateByMinutes;
    private String remarks;
    private String markedBy;

    // Constructors
    public TeachersAttendanceRequestDto() {}

    // Getters and Setters
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
}
