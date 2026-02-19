package com.sc.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "teachers_attendance")
public class TeachersAttendanceEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "status", nullable = false, length = 50)
    private String status; // Present, Absent, Late, Half Day, On Leave

    @Column(name = "time_in")
    private LocalTime timeIn;

    @Column(name = "time_out")
    private LocalTime timeOut;

    @Column(name = "late_by_minutes")
    private Integer lateByMinutes;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "marked_by", length = 100)
    private String markedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_id")
    private TeachersSalaryEntity teachersSalaryEntity;

    @Column(name = "is_processed_for_salary")
    private Boolean isProcessedForSalary = false;

    // Constructors
    public TeachersAttendanceEntity() {
        this.createdAt = LocalDate.now();
    }

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

    public TeachersSalaryEntity getTeachersSalaryEntity() {
        return teachersSalaryEntity;
    }

    public void setTeachersSalaryEntity(TeachersSalaryEntity teachersSalaryEntity) {
        this.teachersSalaryEntity = teachersSalaryEntity;
    }

    public Boolean getIsProcessedForSalary() {
        return isProcessedForSalary;
    }

    public void setIsProcessedForSalary(Boolean isProcessedForSalary) {
        this.isProcessedForSalary = isProcessedForSalary;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}


