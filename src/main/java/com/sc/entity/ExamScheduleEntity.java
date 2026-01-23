package com.sc.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//@Entity
//@Table(name = "exam_schedules")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class ExamScheduleEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "schedule_id", updatable = false, nullable = false)
    private String scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private ExamEntity exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectEntity subject;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "max_marks", nullable = false)
    private Integer maxMarks = 100;

    @Column(name = "instructions", length = 1000)
    private String instructions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invigilator_id")
    private TeacherEntity invigilator;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public String getDuration() {
        if (durationMinutes != null) {
            int hours = durationMinutes / 60;
            int minutes = durationMinutes % 60;
            return hours + "h " + minutes + "m";
        }
        return "";
    }

    public boolean isToday() {
        return examDate.equals(LocalDate.now());
    }

    public boolean isUpcoming() {
        return examDate.isAfter(LocalDate.now()) ||
                (examDate.equals(LocalDate.now()) && startTime.isAfter(LocalTime.now()));
    }
}
