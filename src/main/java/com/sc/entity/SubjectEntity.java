package com.sc.entity;

import com.sc.enum_util.SubjectStatus;
import com.sc.enum_util.SubjectType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "subjects")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class SubjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "subject_id", updatable = false, nullable = false)
    private String subjectId;

    @Column(name = "subject_code", nullable = false, unique = true, length = 20)
    private String subjectCode;

    @Column(name = "subject_name", nullable = false, length = 100)
    private String subjectName;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type", nullable = false)
    private SubjectType subjectType;

    @Column(name = "max_marks", nullable = false)
    private Integer maxMarks = 100;

    @Column(name = "passing_marks", nullable = false)
    private Integer passingMarks = 35;

    @Column(name = "credit_hours")
    private Integer creditHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;

    @ManyToMany(mappedBy = "subjects")
    private List<Class> classes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubjectStatus status = SubjectStatus.ACTIVE;

    // Helper methods
    public boolean isPassing(int marks) {
        return marks >= passingMarks;
    }

    public String getGrade(int marks) {
        double percentage = (marks * 100.0) / maxMarks;
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B+";
        if (percentage >= 60) return "B";
        if (percentage >= 50) return "C";
        if (percentage >= 40) return "D";
        return "F";
    }
}




