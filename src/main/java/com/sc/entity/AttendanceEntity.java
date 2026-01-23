package com.sc.entity;

import com.sc.enum_util.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.security.auth.Subject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//@Entity
//@Table(name = "attendance")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attendance_id", updatable = false, nullable = false)
    private String attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "marked_by")
    private String markedBy; // Teacher ID or Admin ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject; // For subject-wise attendance (optional) - CHANGED THIS LINE

    @Column(name = "session", length = 50)
    private String session; // Morning/Afternoon

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public double getAttendanceHours() {
        if (checkInTime != null && checkOutTime != null) {
            return java.time.Duration.between(checkInTime, checkOutTime).toHours();
        }
        return 0.0;
    }

    public boolean isFullDay() {
        return status == AttendanceStatus.PRESENT && getAttendanceHours() >= 6;
    }

    public boolean isHalfDay() {
        return status == AttendanceStatus.PRESENT && getAttendanceHours() >= 3 && getAttendanceHours() < 6;
    }
}

