package com.sc.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fees")
public class FeesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ============= 🎯 PROPER MANY-TO-ONE MAPPING WITH STUDENT =============
    // ❌ REMOVED: private Long studentId;
    // ✅ ADDED: Proper JPA relationship

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_std_id", nullable = false)
    private StudentEntity student;

    @Column(name = "admission_fees")
    private Integer admissionFees;

    @Column(name = "uniform_fees")
    private Integer uniformFees;

    @Column(name = "book_fees")
    private Integer bookFees;

    @Column(name = "tuition_fees")
    private Integer tuitionFees;

    @ElementCollection
    @CollectionTable(
            name = "additional_fees",
            joinColumns = @JoinColumn(name = "fees_id")
    )
    private List<AdditionalFee> additionalFeesList = new ArrayList<>();

    @Column(name = "total_fees")
    private Integer totalFees;

    @Column(name = "initial_amount")
    private Integer initialAmount;

    @Column(name = "payment_mode")
    private String paymentMode;

    @ElementCollection
    @CollectionTable(
            name = "installments",
            joinColumns = @JoinColumn(name = "fees_id")
    )
    private List<Installment> installmentsList = new ArrayList<>();

    @Column(name = "remaining_fees")
    private Integer remainingFees;

    @Column(name = "cashier_name")
    private String cashierName;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

    // ============= 🔄 CONSTRUCTORS =============

    public FeesEntity() {
        this.additionalFeesList = new ArrayList<>();
        this.installmentsList = new ArrayList<>();
    }

    public FeesEntity(StudentEntity student) {
        this.student = student;
        this.additionalFeesList = new ArrayList<>();
        this.installmentsList = new ArrayList<>();
    }

    // ============= ⏰ LIFE CYCLE CALLBACKS =============

    @PrePersist
    protected void onCreate() {
        createdAt = new java.util.Date();
        updatedAt = new java.util.Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new java.util.Date();
    }

    // ============= 🎯 BUSINESS LOGIC METHODS =============

    /**
     * Calculate total fees (Admission + Uniform + Book + Tuition + Additional)
     */
    public void calculateTotalFees() {
        int baseFees = (admissionFees != null ? admissionFees : 0) +
                (uniformFees != null ? uniformFees : 0) +
                (bookFees != null ? bookFees : 0) +
                (tuitionFees != null ? tuitionFees : 0);

        int additionalSum = additionalFeesList.stream()
                .mapToInt(fee -> fee.getAmount() != null ? fee.getAmount() : 0)
                .sum();

        this.totalFees = baseFees + additionalSum;
    }

    /**
     * Calculate remaining fees (Total - Initial - Paid Installments)
     */
    public void calculateRemainingFees() {
        if (totalFees == null) {
            calculateTotalFees();
        }

        int paidAmount = installmentsList.stream()
                .filter(inst -> "PAID".equalsIgnoreCase(inst.getStatus()) ||
                        "paid".equalsIgnoreCase(inst.getStatus()))
                .mapToInt(inst -> {
                    int amount = inst.getAmount() != null ? inst.getAmount() : 0;
                    int addon = inst.getAddonAmount() != null ? inst.getAddonAmount() : 0;
                    return amount + addon;
                })
                .sum();

        int initial = initialAmount != null ? initialAmount : 0;
        int total = totalFees != null ? totalFees : 0;

        this.remainingFees = total - initial - paidAmount;

        // Ensure remaining fees is not negative
        if (this.remainingFees < 0) {
            this.remainingFees = 0;
        }
    }

    /**
     * Add an additional fee
     */
    public void addAdditionalFee(String name, Integer amount) {
        AdditionalFee fee = new AdditionalFee(name, amount);
        this.additionalFeesList.add(fee);
        calculateTotalFees();
        calculateRemainingFees();
    }

    /**
     * Add an installment
     */
    public void addInstallment(Installment installment) {
        this.installmentsList.add(installment);
        calculateRemainingFees();
    }

    /**
     * Check if fees is fully paid
     */
    public boolean isFullyPaid() {
        if (remainingFees == null) {
            calculateRemainingFees();
        }
        return remainingFees != null && remainingFees == 0;
    }

    /**
     * Get payment status
     */
    public String getPaymentStatus() {
        if (remainingFees == null) {
            calculateRemainingFees();
        }

        if (remainingFees == 0) {
            return "FULLY PAID";
        } else if (initialAmount != null && initialAmount > 0) {
            return "PARTIALLY PAID";
        } else {
            return "PENDING";
        }
    }

    // ============= 🔄 GETTERS AND SETTERS =============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ✅ NEW: Student Getter/Setter
    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    // ✅ Helper method to get Student ID (for backward compatibility)
    public Long getStudentId() {
        return student != null ? student.getStdId() : null;
    }

    // ❌ REMOVE the old setStudentId method - DO NOT INCLUDE
    // public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Integer getAdmissionFees() {
        return admissionFees;
    }

    public void setAdmissionFees(Integer admissionFees) {
        this.admissionFees = admissionFees;
        calculateTotalFees();
        calculateRemainingFees();
    }

    public Integer getUniformFees() {
        return uniformFees;
    }

    public void setUniformFees(Integer uniformFees) {
        this.uniformFees = uniformFees;
        calculateTotalFees();
        calculateRemainingFees();
    }

    public Integer getBookFees() {
        return bookFees;
    }

    public void setBookFees(Integer bookFees) {
        this.bookFees = bookFees;
        calculateTotalFees();
        calculateRemainingFees();
    }

    public Integer getTuitionFees() {
        return tuitionFees;
    }

    public void setTuitionFees(Integer tuitionFees) {
        this.tuitionFees = tuitionFees;
        calculateTotalFees();
        calculateRemainingFees();
    }

    public List<AdditionalFee> getAdditionalFeesList() {
        return additionalFeesList;
    }

    public void setAdditionalFeesList(List<AdditionalFee> additionalFeesList) {
        this.additionalFeesList = additionalFeesList;
        calculateTotalFees();
        calculateRemainingFees();
    }

    public Integer getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(Integer totalFees) {
        this.totalFees = totalFees;
        calculateRemainingFees();
    }

    public Integer getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(Integer initialAmount) {
        this.initialAmount = initialAmount;
        calculateRemainingFees();
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public List<Installment> getInstallmentsList() {
        return installmentsList;
    }

    public void setInstallmentsList(List<Installment> installmentsList) {
        this.installmentsList = installmentsList;
        calculateRemainingFees();
    }

    public Integer getRemainingFees() {
        return remainingFees;
    }

    public void setRemainingFees(Integer remainingFees) {
        this.remainingFees = remainingFees;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ============= 📝 EQUALS, HASHCODE, TOSTRING =============

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeesEntity that = (FeesEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "FeesEntity{" +
                "id=" + id +
                ", studentId=" + (student != null ? student.getStdId() : null) +
                ", totalFees=" + totalFees +
                ", remainingFees=" + remainingFees +
                ", paymentStatus=" + getPaymentStatus() +
                ", academicYear='" + academicYear + '\'' +
                '}';
    }
}