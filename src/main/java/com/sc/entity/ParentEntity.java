package com.sc.entity;

import jakarta.persistence.*;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

//@Entity
//@Table(name = "parent_table")
public class ParentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "parent")
    private StudentEntity student;


    // Father Details
    @Column(name = "father_name", nullable = false)
    private String fatherName;


    @Column(name = "father_aadhar", length = 12)
    private String fatherAadhar;


    @Column(name = "father_contact", nullable = false, length = 15)
    private String fatherContact;


    @Column(name = "father_occupation")
    private String fatherOccupation;


    // Mother Details
    @Column(name = "mother_name", nullable = false)
    private String motherName;


    @Column(name = "mother_aadhar", length = 12)
    private String motherAadhar;


    @Column(name = "mother_contact", length = 15)
    private String motherContact;


    @Column(name = "mother_occupation")
    private String motherOccupation;


    // Primary Guardian

    @Column(name = "primary_email", nullable = false)
    private String primaryEmail;


    @Enumerated(EnumType.STRING)
    @Column(name = "relationship", nullable = false)
    private Relationship relationship;

    // Emergency Contact
    @Column(name = "emergency_contact_name", nullable = false)
    private String emergencyContactName;

    @Column(name = "emergency_contact_number", nullable = false, length = 15)
    private String emergencyContactNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public ParentEntity() {

    }


    public ParentEntity(Long id, StudentEntity student, String fatherName, String fatherAadhar, String fatherContact,

                  String fatherOccupation, String motherName, String motherAadhar, String motherContact,

                  String motherOccupation, String primaryEmail, Relationship relationship,

                  String emergencyContactName, String emergencyContactNumber,

                  LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;

        this.student = student;

        this.fatherName = fatherName;

        this.fatherAadhar = fatherAadhar;

        this.fatherContact = fatherContact;

        this.fatherOccupation = fatherOccupation;

        this.motherName = motherName;

        this.motherAadhar = motherAadhar;

        this.motherContact = motherContact;

        this.motherOccupation = motherOccupation;

        this.primaryEmail = primaryEmail;

        this.relationship = relationship;

        this.emergencyContactName = emergencyContactName;

        this.emergencyContactNumber = emergencyContactNumber;

        this.createdAt = createdAt;

        this.updatedAt = updatedAt;

    }


    // Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public StudentEntity getStudent() { return student; }

    public void setStudent(StudentEntity student) { this.student = student; }

    public String getFatherName() { return fatherName; }

    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getFatherAadhar() { return fatherAadhar; }

    public void setFatherAadhar(String fatherAadhar) { this.fatherAadhar = fatherAadhar; }

    public String getFatherContact() { return fatherContact; }

    public void setFatherContact(String fatherContact) { this.fatherContact = fatherContact; }

    public String getFatherOccupation() { return fatherOccupation; }

    public void setFatherOccupation(String fatherOccupation) { this.fatherOccupation = fatherOccupation; }

    public String getMotherName() { return motherName; }

    public void setMotherName(String motherName) { this.motherName = motherName; }

    public String getMotherAadhar() { return motherAadhar; }

    public void setMotherAadhar(String motherAadhar) { this.motherAadhar = motherAadhar; }

    public String getMotherContact() { return motherContact; }

    public void setMotherContact(String motherContact) { this.motherContact = motherContact; }

    public String getMotherOccupation() { return motherOccupation; }

    public void setMotherOccupation(String motherOccupation) { this.motherOccupation = motherOccupation; }

    public String getPrimaryEmail() { return primaryEmail; }

    public void setPrimaryEmail(String primaryEmail) { this.primaryEmail = primaryEmail; }

    public Relationship getRelationship() { return relationship; }

    public void setRelationship(Relationship relationship) { this.relationship = relationship; }

    public String getEmergencyContactName() { return emergencyContactName; }

    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }

    public String getEmergencyContactNumber() { return emergencyContactNumber; }

    public void setEmergencyContactNumber(String emergencyContactNumber) { this.emergencyContactNumber = emergencyContactNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

}
