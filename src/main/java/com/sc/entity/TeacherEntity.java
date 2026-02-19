package com.sc.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "teachers")
@SQLDelete(sql = "UPDATE teachers SET is_deleted = true WHERE id = ?")
public class TeacherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")  // Map to the actual database column
    private Long id;

    @Column(unique = true, nullable = false)
    private String employeeId;

    @Column(nullable = false)
    private String teacherName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String contactNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")  // ADD THIS LINE
    private Date dob;

    private String gender;
    private String bloodGroup;

    private String address; // ADD THIS LINE

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ElementCollection
    @CollectionTable(name = "teacher_address", joinColumns = @JoinColumn(name = "teacher_id"))
    @BatchSize(size = 50)
    @Column(name = "address_line")
    private List<String> addressLines = new ArrayList<>();

    private String city;
    private String state;
    private String pincode;

    private String emergencyContactName;
    private String emergencyContactNumber;

    @Column(unique = true, nullable = false)
    private String aadharNumber;

    @Column(unique = true, nullable = false)
    private String panNumber;

    private String medicalInfo;

    @Temporal(TemporalType.DATE)
    private Date joiningDate;

    private String designation;
    private Integer totalExperience;
    private String department;
    private String employmentType;

    @ElementCollection
    @CollectionTable(name = "teacher_previous_experience", joinColumns = @JoinColumn(name = "teacher_id"))
    @BatchSize(size = 50)
    private List<PreviousExperience> previousExperience = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "teacher_qualifications", joinColumns = @JoinColumn(name = "teacher_id"))
    @BatchSize(size = 50)
    private List<Qualification> qualifications = new ArrayList<>();

    private String primarySubject;

    @ElementCollection
    @CollectionTable(name = "teacher_additional_subjects", joinColumns = @JoinColumn(name = "teacher_id"))
    @BatchSize(size = 50)
    @Column(name = "subject")
    private List<String> additionalSubjects = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "teacher_classes", joinColumns = @JoinColumn(name = "teacher_id"))
    @BatchSize(size = 50)
    @Column(name = "class_assigned")
    private List<String> classes = new ArrayList<>();

    private Double basicSalary;
    private Double hra;
    private Double da;
    private Double ta;

    @ElementCollection
    @CollectionTable(name = "teacher_additional_allowances", joinColumns = @JoinColumn(name = "teacher_id"))
    @BatchSize(size = 50)
    private List<AdditionalAllowance> additionalAllowances = new ArrayList<>();

    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String branchName;

    private Double grossSalary;

    @Lob
    @Column(name = "teacher_photo", columnDefinition = "LONGBLOB")
    private byte[] teacherPhoto;

    private String status = "Active";

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    // Add this field (somewhere before the isDeleted field)
    @Column(name = "is_approved", nullable = false, columnDefinition = "boolean default false")
    private boolean approved = false;

    // Add these getter and setter methods (add them to your existing getters/setters section)
    public boolean getApproved() {
        return approved;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        lastUpdated = new Date();
        if (status == null) {
            status = "Active";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = new Date();
    }

    // Helper method to calculate gross salary
    public void calculateGrossSalary() {
        double total = (basicSalary != null ? basicSalary : 0) +
                (hra != null ? hra : 0) +
                (da != null ? da : 0) +
                (ta != null ? ta : 0);

        if (additionalAllowances != null) {
            total += additionalAllowances.stream()
                    .mapToDouble(AdditionalAllowance::getAmount)
                    .sum();
        }

        this.grossSalary = total;
    }

    // Embedded classes for complex fields
    @Embeddable
    public static class PreviousExperience {
        private String prevSchool;
        private String prevPosition;
        private Integer prevDuration;

        // Getters and Setters
        public String getPrevSchool() { return prevSchool; }
        public void setPrevSchool(String prevSchool) { this.prevSchool = prevSchool; }

        public String getPrevPosition() { return prevPosition; }
        public void setPrevPosition(String prevPosition) { this.prevPosition = prevPosition; }

        public Integer getPrevDuration() { return prevDuration; }
        public void setPrevDuration(Integer prevDuration) { this.prevDuration = prevDuration; }
    }

    @Embeddable
    public static class Qualification {
        private String degree;
        private String specialization;
        private String university;
        private Integer completionYear;

        // Getters and Setters
        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }

        public String getSpecialization() { return specialization; }
        public void setSpecialization(String specialization) { this.specialization = specialization; }

        public String getUniversity() { return university; }
        public void setUniversity(String university) { this.university = university; }

        public Integer getCompletionYear() { return completionYear; }
        public void setCompletionYear(Integer completionYear) { this.completionYear = completionYear; }
    }

    @Embeddable
    public static class AdditionalAllowance {
        private String name;
        private Double amount;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
    }

    // Specification for not deleted teachers
    public static Specification<TeacherEntity> notDeleted() {
        return (root, query, cb) -> cb.equal(root.get("isDeleted"), false);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public List<String> getAddressLines() { return addressLines; }
    public void setAddressLines(List<String> addressLines) { this.addressLines = addressLines; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }

    public String getEmergencyContactNumber() { return emergencyContactNumber; }
    public void setEmergencyContactNumber(String emergencyContactNumber) { this.emergencyContactNumber = emergencyContactNumber; }

    public String getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }

    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }

    public String getMedicalInfo() { return medicalInfo; }
    public void setMedicalInfo(String medicalInfo) { this.medicalInfo = medicalInfo; }

    public Date getJoiningDate() { return joiningDate; }
    public void setJoiningDate(Date joiningDate) { this.joiningDate = joiningDate; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public Integer getTotalExperience() { return totalExperience; }
    public void setTotalExperience(Integer totalExperience) { this.totalExperience = totalExperience; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public List<PreviousExperience> getPreviousExperience() { return previousExperience; }
    public void setPreviousExperience(List<PreviousExperience> previousExperience) { this.previousExperience = previousExperience; }

    public List<Qualification> getQualifications() { return qualifications; }
    public void setQualifications(List<Qualification> qualifications) { this.qualifications = qualifications; }

    public String getPrimarySubject() { return primarySubject; }
    public void setPrimarySubject(String primarySubject) { this.primarySubject = primarySubject; }

    public List<String> getAdditionalSubjects() { return additionalSubjects; }
    public void setAdditionalSubjects(List<String> additionalSubjects) { this.additionalSubjects = additionalSubjects; }

    public List<String> getClasses() { return classes; }
    public void setClasses(List<String> classes) { this.classes = classes; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getHra() { return hra; }
    public void setHra(Double hra) { this.hra = hra; }

    public Double getDa() { return da; }
    public void setDa(Double da) { this.da = da; }

    public Double getTa() { return ta; }
    public void setTa(Double ta) { this.ta = ta; }

    public List<AdditionalAllowance> getAdditionalAllowances() { return additionalAllowances; }
    public void setAdditionalAllowances(List<AdditionalAllowance> additionalAllowances) { this.additionalAllowances = additionalAllowances; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public Double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(Double grossSalary) { this.grossSalary = grossSalary; }

    public byte[] getTeacherPhoto() { return teacherPhoto; }
    public void setTeacherPhoto(byte[] teacherPhoto) { this.teacherPhoto = teacherPhoto; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}