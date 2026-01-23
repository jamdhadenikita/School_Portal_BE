package com.sc.entity;

import com.sc.enum_util.ResultStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

//@Entity
//@Table(name = "exam_results")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class ExamResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "result_id", updatable = false, nullable = false)
    private String resultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectEntity subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private ExamEntity exam;

    @Column(name = "marks_obtained", nullable = false)
    private Double marksObtained;

    @Column(name = "max_marks", nullable = false)
    private Double maxMarks;

    @Column(name = "percentage", precision = 5, scale = 2)
    private Double percentage;

    @Column(name = "grade", length = 5)
    private String grade;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_status", nullable = false)
    private ResultStatus resultStatus;

    @Column(name = "entered_by")
    private String enteredBy; // Teacher ID

    @Column(name = "verified_by")
    private String verifiedBy; // HOD or Principal ID

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public void calculatePercentage() {
        if (marksObtained != null && maxMarks != null && maxMarks > 0) {
            this.percentage = (marksObtained / maxMarks) * 100;
        }
    }

    public void calculateGrade() {
        if (percentage != null) {
            if (percentage >= 90) this.grade = "A+";
            else if (percentage >= 80) this.grade = "A";
            else if (percentage >= 70) this.grade = "B+";
            else if (percentage >= 60) this.grade = "B";
            else if (percentage >= 50) this.grade = "C";
            else if (percentage >= 40) this.grade = "D";
            else this.grade = "F";
        }
    }

    public boolean isPassed() {
        return resultStatus == ResultStatus.PASSED;
    }
}


