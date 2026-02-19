package com.sc.dto.request;

import java.util.List;

public class TeacherSubjectAssignmentDTO {
    private String teacherId;
    private String teacherName;
    private List<String> subjects;

    // Constructors
    public TeacherSubjectAssignmentDTO() {}

    public TeacherSubjectAssignmentDTO(String teacherId, String teacherName, List<String> subjects) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.subjects = subjects;
    }

    // Getters and Setters
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public List<String> getSubjects() { return subjects; }
    public void setSubjects(List<String> subjects) { this.subjects = subjects; }
}