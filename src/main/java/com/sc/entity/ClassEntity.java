package com.sc.entity;

import com.sc.enum_util.ClassStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "classes")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class ClassEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "class_id", updatable = false, nullable = false)
    private String classId;

    @Column(name = "class_name", nullable = false, length = 50)
    private String className; // e.g., "PG", "LKG", "UKG", "1st", "2nd"

    @Column(name = "class_code", nullable = false, unique = true, length = 20)
    private String classCode; // e.g., "PG-2024-A"

    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;

    @Column(name = "section", nullable = false, length = 5)
    private String section; // A, B, C, D

    @Column(name = "max_students", nullable = false)
    private Integer maxStudents = 30;

    @Column(name = "current_students")
    private Integer currentStudents = 0;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_teacher_id")
    private TeacherEntity classTeacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_teacher_id")
    private TeacherEntity assistantTeacher;

    @Column(name = "start_time")
    private String startTime; // "08:30"

    @Column(name = "end_time")
    private String endTime; // "13:30"

    @ElementCollection
    @CollectionTable(name = "class_working_days", joinColumns = @JoinColumn(name = "class_id"))
    @Column(name = "working_day")
    private List<String> workingDays = new ArrayList<>(); // ["monday", "tuesday", ...]

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentEntity> students = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "class_subjects",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<SubjectEntity> subjects = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClassStatus status = ClassStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public int getAvailableSeats() {
        return maxStudents - currentStudents;
    }

    public boolean hasAvailableSeats() {
        return getAvailableSeats() > 0;
    }

    public double getCapacityPercentage() {
        return (currentStudents * 100.0) / maxStudents;
    }

    public String getFullClassName() {
        return className + " - Section " + section;
    }
}


