package com.sc.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;

//    @OneToMany(mappedBy = "classEntity")
//    private List<SubjectEntity> subject;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_code", unique = true, nullable = false)
    private String classCode;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Column(name = "section", nullable = false)
    private String section;

    @Column(name = "max_students")
    private Integer maxStudents = 30;

    @Column(name = "current_students")
    private Integer currentStudents = 0;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "class_teacher_id")
    private Long classTeacherId;

    @Column(name = "class_teacher_subject")
    private String classTeacherSubject;

    @Column(name = "assistant_teacher_id")
    private Long assistantTeacherId;

    @Column(name = "assistant_teacher_subject")
    private String assistantTeacherSubject;

    @Column(name = "other_teacher_subject_json", columnDefinition = "TEXT")
    private String otherTeacherSubjectJson;

    @Column(name = "working_days_json", columnDefinition = "TEXT")
    private String workingDaysJson;

    @Column(name = "status")
    private String status = "ACTIVE";

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // Transient fields for easy access
    @Transient
    private List<TeacherSubjectAssignment> otherTeacherSubject = new ArrayList<>();

    @Transient
    private List<String> workingDays = new ArrayList<>();

    // Constructors
    public ClassEntity() {
        this.otherTeacherSubject = new ArrayList<>();
        this.workingDays = new ArrayList<>();
    }

    public ClassEntity(String className, String classCode, String academicYear, String section) {
        this.className = className;
        this.classCode = classCode;
        this.academicYear = academicYear;
        this.section = section;
        this.otherTeacherSubject = new ArrayList<>();
        this.workingDays = new ArrayList<>();
    }

    // Lifecycle methods
    @PostLoad
    private void onLoad() {
        loadOtherTeacherSubjectFromJson();
        loadWorkingDaysFromJson();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
        saveOtherTeacherSubjectToJson();
        saveWorkingDaysToJson();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
        saveOtherTeacherSubjectToJson();
        saveWorkingDaysToJson();
    }

    // JSON Helper Methods for otherTeacherSubject
    private void loadOtherTeacherSubjectFromJson() {
        if (otherTeacherSubjectJson != null && !otherTeacherSubjectJson.isEmpty()) {
            try {
                otherTeacherSubject = parseOtherTeacherSubjectJson(otherTeacherSubjectJson);
            } catch (Exception e) {
                otherTeacherSubject = new ArrayList<>();
            }
        } else {
            otherTeacherSubject = new ArrayList<>();
        }
    }

    private void saveOtherTeacherSubjectToJson() {
        if (otherTeacherSubject != null && !otherTeacherSubject.isEmpty()) {
            otherTeacherSubjectJson = convertOtherTeacherSubjectToJson(otherTeacherSubject);
        } else {
            otherTeacherSubjectJson = "[]";
        }
    }

    private List<TeacherSubjectAssignment> parseOtherTeacherSubjectJson(String json) {
        List<TeacherSubjectAssignment> list = new ArrayList<>();

        if (json == null || json.isEmpty() || json.equals("[]")) {
            return list;
        }

        try {
            json = json.trim();
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1);
                String[] objects = json.split("\\},\\{");

                for (String obj : objects) {
                    obj = obj.replace("{", "").replace("}", "");
                    String[] fields = obj.split(",");

                    String teacherId = "";
                    String teacherName = "";
                    List<String> subjects = new ArrayList<>();

                    for (String field : fields) {
                        String[] keyValue = field.split(":");
                        if (keyValue.length == 2) {
                            String key = keyValue[0].trim().replace("\"", "");
                            String value = keyValue[1].trim().replace("\"", "");

                            if (key.equals("teacherId")) {
                                teacherId = value;
                            } else if (key.equals("teacherName")) {
                                teacherName = value;
                            } else if (key.equals("subjects")) {
                                value = value.replace("[", "").replace("]", "");
                                if (!value.isEmpty()) {
                                    String[] subjectArray = value.split(",");
                                    for (String subject : subjectArray) {
                                        subjects.add(subject.trim().replace("\"", ""));
                                    }
                                }
                            }
                        }
                    }

                    if (!teacherId.isEmpty()) {
                        list.add(new TeacherSubjectAssignment(teacherId, teacherName, subjects));
                    }
                }
            }
        } catch (Exception e) {
            list = new ArrayList<>();
        }

        return list;
    }

    private String convertOtherTeacherSubjectToJson(List<TeacherSubjectAssignment> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }

        StringBuilder json = new StringBuilder("[");
        boolean firstObject = true;

        for (TeacherSubjectAssignment assignment : list) {
            if (!firstObject) {
                json.append(",");
            }
            firstObject = false;

            json.append("{");
            json.append("\"teacherId\":\"").append(assignment.getTeacherId()).append("\",");
            json.append("\"teacherName\":\"").append(assignment.getTeacherName()).append("\",");
            json.append("\"subjects\":[");

            boolean firstSubject = true;
            for (String subject : assignment.getSubjects()) {
                if (!firstSubject) {
                    json.append(",");
                }
                firstSubject = false;
                json.append("\"").append(subject).append("\"");
            }

            json.append("]}");
        }

        json.append("]");
        return json.toString();
    }

    // JSON Helper Methods for workingDays
    private void loadWorkingDaysFromJson() {
        if (workingDaysJson != null && !workingDaysJson.isEmpty()) {
            try {
                workingDays = parseWorkingDaysJson(workingDaysJson);
            } catch (Exception e) {
                workingDays = new ArrayList<>();
            }
        } else {
            workingDays = new ArrayList<>();
        }
    }

    private void saveWorkingDaysToJson() {
        if (workingDays != null && !workingDays.isEmpty()) {
            workingDaysJson = convertWorkingDaysToJson(workingDays);
        } else {
            workingDaysJson = "[]";
        }
    }

    private List<String> parseWorkingDaysJson(String json) {
        List<String> list = new ArrayList<>();

        if (json == null || json.isEmpty() || json.equals("[]")) {
            return list;
        }

        try {
            json = json.trim();
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1);
                if (!json.isEmpty()) {
                    String[] days = json.split(",");
                    for (String day : days) {
                        list.add(day.trim().replace("\"", ""));
                    }
                }
            }
        } catch (Exception e) {
            list = new ArrayList<>();
        }

        return list;
    }

    private String convertWorkingDaysToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }

        StringBuilder json = new StringBuilder("[");
        boolean firstDay = true;

        for (String day : list) {
            if (!firstDay) {
                json.append(",");
            }
            firstDay = false;
            json.append("\"").append(day).append("\"");
        }

        json.append("]");
        return json.toString();
    }

    // Inner class for teacher-subject assignments
    public static class TeacherSubjectAssignment {
        private String teacherId;
        private String teacherName;
        private List<String> subjects;

        public TeacherSubjectAssignment() {
            this.subjects = new ArrayList<>();
        }

        public TeacherSubjectAssignment(String teacherId, String teacherName, List<String> subjects) {
            this.teacherId = teacherId;
            this.teacherName = teacherName;
            this.subjects = subjects != null ? subjects : new ArrayList<>();
        }

        public String getTeacherId() { return teacherId; }
        public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

        public String getTeacherName() { return teacherName; }
        public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

        public List<String> getSubjects() { return subjects; }
        public void setSubjects(List<String> subjects) { this.subjects = subjects; }
    }

    // Business Methods
    public void addWorkingDay(String day) {
        if (!workingDays.contains(day)) {
            workingDays.add(day);
        }
    }

    public void removeWorkingDay(String day) {
        workingDays.remove(day);
    }

    public void addOtherTeacherAssignment(String teacherId, String teacherName, String subject) {
        // Find if teacher already exists
        for (TeacherSubjectAssignment assignment : otherTeacherSubject) {
            if (assignment.getTeacherId().equals(teacherId)) {
                if (!assignment.getSubjects().contains(subject)) {
                    assignment.getSubjects().add(subject);
                }
                return;
            }
        }

        // Add new teacher assignment
        List<String> subjects = new ArrayList<>();
        subjects.add(subject);
        otherTeacherSubject.add(new TeacherSubjectAssignment(teacherId, teacherName, subjects));
    }

    public void removeOtherTeacherAssignment(String teacherId, String subject) {
        otherTeacherSubject.removeIf(assignment ->
                assignment.getTeacherId().equals(teacherId) &&
                        assignment.getSubjects().contains(subject)
        );
    }

    // Getters and Setters
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents != null ? maxStudents : 30; }

    public Integer getCurrentStudents() { return currentStudents; }
    public void setCurrentStudents(Integer currentStudents) { this.currentStudents = currentStudents != null ? currentStudents : 0; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getClassTeacherId() { return classTeacherId; }
    public void setClassTeacherId(Long classTeacherId) { this.classTeacherId = classTeacherId; }

    public String getClassTeacherSubject() { return classTeacherSubject; }
    public void setClassTeacherSubject(String classTeacherSubject) { this.classTeacherSubject = classTeacherSubject; }

    public Long getAssistantTeacherId() { return assistantTeacherId; }
    public void setAssistantTeacherId(Long assistantTeacherId) { this.assistantTeacherId = assistantTeacherId; }

    public String getAssistantTeacherSubject() { return assistantTeacherSubject; }
    public void setAssistantTeacherSubject(String assistantTeacherSubject) { this.assistantTeacherSubject = assistantTeacherSubject; }

    public String getOtherTeacherSubjectJson() { return otherTeacherSubjectJson; }
    public void setOtherTeacherSubjectJson(String otherTeacherSubjectJson) {
        this.otherTeacherSubjectJson = otherTeacherSubjectJson;
        loadOtherTeacherSubjectFromJson();
    }

    public String getWorkingDaysJson() { return workingDaysJson; }
    public void setWorkingDaysJson(String workingDaysJson) {
        this.workingDaysJson = workingDaysJson;
        loadWorkingDaysFromJson();
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status != null ? status : "ACTIVE"; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public List<TeacherSubjectAssignment> getOtherTeacherSubject() { return otherTeacherSubject; }
    public void setOtherTeacherSubject(List<TeacherSubjectAssignment> otherTeacherSubject) {
        this.otherTeacherSubject = otherTeacherSubject != null ? otherTeacherSubject : new ArrayList<>();
    }

    public List<String> getWorkingDays() { return workingDays; }
    public void setWorkingDays(List<String> workingDays) {
        this.workingDays = workingDays != null ? workingDays : new ArrayList<>();
    }
}