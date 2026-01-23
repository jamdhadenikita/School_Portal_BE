package com.sc.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "student_table")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "student_id", updatable = false, nullable = false)
    private String studentId;

    // Personal Information
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    // REMOVED @Enumerated - gender is String
    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "aadhar_number", unique = true, length = 12)
    private String aadharNumber;

    @Column(name = "caste_category")
    private String casteCategory;

    @Column(name = "medical_info", columnDefinition = "TEXT")
    private String medicalInfo;

    @ElementCollection
    @CollectionTable(name = "student_sports", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "sport")
    private List<String> sports = new ArrayList<>();

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    // Address Information (no longer embedded)
    @Column(name = "local_line1")
    private String localLine1;

    @Column(name = "local_line2")
    private String localLine2;

    @Column(name = "local_city")
    private String localCity;

    @Column(name = "local_state")
    private String localState;

    @Column(name = "local_pincode")
    private String localPincode;

    @Column(name = "permanent_line1")
    private String permanentLine1;

    @Column(name = "permanent_line2")
    private String permanentLine2;

    @Column(name = "permanent_city")
    private String permanentCity;

    @Column(name = "permanent_state")
    private String permanentState;

    @Column(name = "permanent_pincode")
    private String permanentPincode;

    // Academic Information
    @Column(name = "current_class", nullable = false)
    private String currentClass;

    @Column(name = "section", nullable = false)
    private String section;

    @Column(name = "roll_number", nullable = false)
    private Integer rollNumber;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Column(name = "class_teacher")
    private String classTeacher;

    @Column(name = "previous_school")
    private String previousSchool;

    @ElementCollection
    @CollectionTable(name = "student_subjects", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "subject")
    private List<String> subjects = new ArrayList<>();

    // Relationships
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private ParentEntity parent;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id", referencedColumnName = "id")
    private FeeEntity fee;

    // Attendance Summary
    @Column(name = "total_attendance_days", columnDefinition = "INT DEFAULT 0")
    private Integer totalAttendanceDays = 0;

    @Column(name = "present_days", columnDefinition = "INT DEFAULT 0")
    private Integer presentDays = 0;

    @Column(name = "attendance_percentage", columnDefinition = "DECIMAL(5,2) DEFAULT 0.0")
    private Double attendancePercentage = 0.0;

    // System Fields - REMOVED @Enumerated - status is String
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_by")
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AttendanceEntity> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExamResultEntity> examResults = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    // Helper method in Student entity for attendance calculation:
//    public Map<String, Object> getAttendanceSummary(LocalDate startDate, LocalDate endDate) {
//        Map<String, Object> summary = new HashMap<>();
//        long totalDays = 0;
//        long presentDays = 0;
//        long absentDays = 0;
//        long lateDays = 0;
//        long halfDays = 0;
//        for (AttendanceEntity attendance : attendances) {
//            if (!attendance.getAttendanceDate().isBefore(startDate) &&
//                    !attendance.getAttendanceDate().isAfter(endDate)) {
//                totalDays++;
//                switch (attendance.getStatus()) {
//                    case PRESENT:
//                        presentDays++;
//                        if (attendance.isHalfDay()) halfDays++;
//                        break;
//                    case ABSENT:
//                        absentDays++;
//                        break;
//                    case LATE:
//                        lateDays++;
//                        presentDays++; // Late is considered present
//                        break;
//                    case HALF_DAY:
//                        presentDays++;
//                        halfDays++;
//                        break;
//                }
//            }
//        }
//        summary.put("totalDays", totalDays);
//        summary.put("presentDays", presentDays);
//        summary.put("absentDays", absentDays);
//        summary.put("lateDays", lateDays);
//        summary.put("halfDays", halfDays);
//        summary.put("attendancePercentage", totalDays > 0 ? (presentDays * 100.0) / totalDays : 0.0);
//        return summary;
//    }
//
//    // Helper method for marks/grade calculation:
//    public Map<String, Object> getAcademicPerformance(String academicYear) {
//        Map<String, Object> performance = new HashMap<>();
//        Map<String, Double> subjectMarks = new HashMap<>();
//        double totalMarks = 0;
//        int subjectCount = 0;
//        for (ExamResultEntity result : examResults) {
//            if (result.getExam().getAcademicYear().equals(academicYear)) {
//                String subjectName = result.getSubject().getSubjectName();
//                double marks = result.getMarksObtained();
//                subjectMarks.put(subjectName, marks);
//                totalMarks += marks;
//                subjectCount++;
//            }
//        }
//        performance.put("subjectMarks", subjectMarks);
//        performance.put("totalMarks", totalMarks);
//        performance.put("averageMarks", subjectCount > 0 ? totalMarks / subjectCount : 0);
//        performance.put("overallPercentage", subjectCount > 0 ? (totalMarks / (subjectCount * 100)) * 100 : 0);
//        // Calculate overall grade
//        double percentage = subjectCount > 0 ? (totalMarks / (subjectCount * 100)) * 100 : 0;
//        if (percentage >= 90) performance.put("overallGrade", "A+");
//        else if (percentage >= 80) performance.put("overallGrade", "A");
//        else if (percentage >= 70) performance.put("overallGrade", "B+");
//        else if (percentage >= 60) performance.put("overallGrade", "B");
//        else if (percentage >= 50) performance.put("overallGrade", "C");
//        else if (percentage >= 40) performance.put("overallGrade", "D");
//        else performance.put("overallGrade", "F");
//        return performance;
//    }

    // Constructors
    public StudentEntity() {

    }

    public StudentEntity(String studentId, String fullName, LocalDate dateOfBirth, String gender, String bloodGroup, String aadharNumber, String casteCategory, String medicalInfo, List<String> sports, String profileImageUrl, String localLine1, String localLine2, String localCity, String localState, String localPincode, String permanentLine1, String permanentLine2, String permanentCity, String permanentState, String permanentPincode, String currentClass, String section, Integer rollNumber, LocalDate admissionDate, String academicYear, String classTeacher, String previousSchool, List<String> subjects, ParentEntity parent, FeeEntity fee, Integer totalAttendanceDays, Integer presentDays, Double attendancePercentage, String status, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt, List<AttendanceEntity> attendances, List<ExamResultEntity> examResults, ClassEntity classEntity) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.aadharNumber = aadharNumber;
        this.casteCategory = casteCategory;
        this.medicalInfo = medicalInfo;
        this.sports = sports;
        this.profileImageUrl = profileImageUrl;
        this.localLine1 = localLine1;
        this.localLine2 = localLine2;
        this.localCity = localCity;
        this.localState = localState;
        this.localPincode = localPincode;
        this.permanentLine1 = permanentLine1;
        this.permanentLine2 = permanentLine2;
        this.permanentCity = permanentCity;
        this.permanentState = permanentState;
        this.permanentPincode = permanentPincode;
        this.currentClass = currentClass;
        this.section = section;
        this.rollNumber = rollNumber;
        this.admissionDate = admissionDate;
        this.academicYear = academicYear;
        this.classTeacher = classTeacher;
        this.previousSchool = previousSchool;
        this.subjects = subjects;
        this.parent = parent;
        this.fee = fee;
        this.totalAttendanceDays = totalAttendanceDays;
        this.presentDays = presentDays;
        this.attendancePercentage = attendancePercentage;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attendances = attendances;
        this.examResults = examResults;
        this.classEntity = classEntity;
    }

    // Getters and Setters (manually written)

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getCasteCategory() {
        return casteCategory;
    }

    public void setCasteCategory(String casteCategory) {
        this.casteCategory = casteCategory;
    }

    public String getMedicalInfo() {
        return medicalInfo;
    }

    public void setMedicalInfo(String medicalInfo) {
        this.medicalInfo = medicalInfo;
    }

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getLocalLine1() {
        return localLine1;
    }

    public void setLocalLine1(String localLine1) {
        this.localLine1 = localLine1;
    }

    public String getLocalLine2() {
        return localLine2;
    }

    public void setLocalLine2(String localLine2) {
        this.localLine2 = localLine2;
    }

    public String getLocalCity() {
        return localCity;
    }

    public void setLocalCity(String localCity) {
        this.localCity = localCity;
    }

    public String getLocalState() {
        return localState;
    }

    public void setLocalState(String localState) {
        this.localState = localState;
    }

    public String getLocalPincode() {
        return localPincode;
    }

    public void setLocalPincode(String localPincode) {
        this.localPincode = localPincode;
    }

    public String getPermanentLine1() {
        return permanentLine1;
    }

    public void setPermanentLine1(String permanentLine1) {
        this.permanentLine1 = permanentLine1;
    }

    public String getPermanentLine2() {
        return permanentLine2;
    }

    public void setPermanentLine2(String permanentLine2) {
        this.permanentLine2 = permanentLine2;
    }

    public String getPermanentCity() {
        return permanentCity;
    }

    public void setPermanentCity(String permanentCity) {
        this.permanentCity = permanentCity;
    }

    public String getPermanentState() {
        return permanentState;
    }

    public void setPermanentState(String permanentState) {
        this.permanentState = permanentState;
    }

    public String getPermanentPincode() {
        return permanentPincode;
    }

    public void setPermanentPincode(String permanentPincode) {
        this.permanentPincode = permanentPincode;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(Integer rollNumber) {
        this.rollNumber = rollNumber;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public String getPreviousSchool() {
        return previousSchool;
    }

    public void setPreviousSchool(String previousSchool) {
        this.previousSchool = previousSchool;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public ParentEntity getParent() {
        return parent;
    }

    public void setParent(ParentEntity parent) {
        this.parent = parent;
    }

    public FeeEntity getFee() {
        return fee;
    }

    public void setFee(FeeEntity fee) {
        this.fee = fee;
    }

    public Integer getTotalAttendanceDays() {
        return totalAttendanceDays;
    }

    public void setTotalAttendanceDays(Integer totalAttendanceDays) {
        this.totalAttendanceDays = totalAttendanceDays;
    }

    public Integer getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(Integer presentDays) {
        this.presentDays = presentDays;
    }

    public Double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(Double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<AttendanceEntity> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<AttendanceEntity> attendances) {
        this.attendances = attendances;
    }

    public List<ExamResultEntity> getExamResults() {
        return examResults;
    }

    public void setExamResults(List<ExamResultEntity> examResults) {
        this.examResults = examResults;
    }

    public ClassEntity getClassEntity() {
        return classEntity;
    }

    public void setClassEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
    }

    // Helper methods (same functionality)

    public void calculateAttendancePercentage() {
        if (totalAttendanceDays != null && totalAttendanceDays > 0) {
            this.attendancePercentage = (presentDays.doubleValue() / totalAttendanceDays.doubleValue()) * 100;
        } else {
            this.attendancePercentage = 0.0;
        }
    }

    public void markAttendance(boolean isPresent) {
        this.totalAttendanceDays = (this.totalAttendanceDays != null ? this.totalAttendanceDays : 0) + 1;
        if (isPresent) {
            this.presentDays = (this.presentDays != null ? this.presentDays : 0) + 1;
        }
        calculateAttendancePercentage();
    }
}