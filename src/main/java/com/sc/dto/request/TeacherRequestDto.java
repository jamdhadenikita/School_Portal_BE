package com.sc.dto.request;


import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO for creating/updating Teachers.
 */
public class TeacherRequestDto {

    // Personal Details
    private String employeeId;
    private String teacherName;
    private Date dob;
    private String gender;
    private String bloodGroup;

    private List<String> addressLines = new ArrayList<>();
    private String city;
    private String state;
    private String pincode;

    private String contactNumber;
    private String email;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private String aadharNumber;
    private String panNumber;
    private String medicalInfo;

    // Professional Details
    private Date joiningDate;
    private String designation;
    private Integer totalExperience;
    private String department;
    private String employmentType;

    // Experience entries
    private List<ExperienceEntry> previousExperience = new ArrayList<>();

    // Academic Details
    private List<QualificationEntry> qualifications = new ArrayList<>();
    private String primarySubject;
    private List<String> additionalSubjects = new ArrayList<>();
    private List<String> classes = new ArrayList<>();

    // Salary Details
    private Double basicSalary;
    private Double hra;
    private Double da;
    private Double ta;
    private List<AllowanceEntry> additionalAllowances = new ArrayList<>();

    // Bank Details
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String branchName;

    // Status & Images
    private String status;
    private MultipartFile teacherPhoto;

    // Admin approval flag
    private Boolean approved;

    // ────────────────────────────────────────────────
    // Inner DTO classes for complex data
    // ────────────────────────────────────────────────

    public static class ExperienceEntry {
        private String prevSchool;
        private String prevPosition;
        private Integer prevDuration;

        public String getPrevSchool() { return prevSchool; }
        public void setPrevSchool(String prevSchool) { this.prevSchool = prevSchool; }

        public String getPrevPosition() { return prevPosition; }
        public void setPrevPosition(String prevPosition) { this.prevPosition = prevPosition; }

        public Integer getPrevDuration() { return prevDuration; }
        public void setPrevDuration(Integer prevDuration) { this.prevDuration = prevDuration; }
    }

    public static class QualificationEntry {
        private String degree;
        private String specialization;
        private String university;
        private Integer completionYear;

        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }

        public String getSpecialization() { return specialization; }
        public void setSpecialization(String specialization) { this.specialization = specialization; }

        public String getUniversity() { return university; }
        public void setUniversity(String university) { this.university = university; }

        public Integer getCompletionYear() { return completionYear; }
        public void setCompletionYear(Integer completionYear) { this.completionYear = completionYear; }
    }

    public static class AllowanceEntry {
        private String name;
        private Double amount;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
    }

    // ────────────────────────────────────────────────
    // Getters & Setters
    // ────────────────────────────────────────────────

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public List<String> getAddressLines() { return addressLines; }
    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines != null ? addressLines : new ArrayList<>();
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

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

    public List<ExperienceEntry> getPreviousExperience() { return previousExperience; }
    public void setPreviousExperience(List<ExperienceEntry> previousExperience) {
        this.previousExperience = previousExperience != null ? previousExperience : new ArrayList<>();
    }

    public List<QualificationEntry> getQualifications() { return qualifications; }
    public void setQualifications(List<QualificationEntry> qualifications) {
        this.qualifications = qualifications != null ? qualifications : new ArrayList<>();
    }

    public String getPrimarySubject() { return primarySubject; }
    public void setPrimarySubject(String primarySubject) { this.primarySubject = primarySubject; }

    public List<String> getAdditionalSubjects() { return additionalSubjects; }
    public void setAdditionalSubjects(List<String> additionalSubjects) {
        this.additionalSubjects = additionalSubjects != null ? additionalSubjects : new ArrayList<>();
    }

    public List<String> getClasses() { return classes; }
    public void setClasses(List<String> classes) {
        this.classes = classes != null ? classes : new ArrayList<>();
    }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getHra() { return hra; }
    public void setHra(Double hra) { this.hra = hra; }

    public Double getDa() { return da; }
    public void setDa(Double da) { this.da = da; }

    public Double getTa() { return ta; }
    public void setTa(Double ta) { this.ta = ta; }

    public List<AllowanceEntry> getAdditionalAllowances() { return additionalAllowances; }
    public void setAdditionalAllowances(List<AllowanceEntry> additionalAllowances) {
        this.additionalAllowances = additionalAllowances != null ? additionalAllowances : new ArrayList<>();
    }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public MultipartFile getTeacherPhoto() { return teacherPhoto; }
    public void setTeacherPhoto(MultipartFile teacherPhoto) { this.teacherPhoto = teacherPhoto; }

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }
}