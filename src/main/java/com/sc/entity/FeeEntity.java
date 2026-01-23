package com.sc.entity;

import com.sc.enum_util.PaymentMethod;
import com.sc.enum_util.PaymentMode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "fees_table")
public class FeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "fee")
    private StudentEntity student;

    // Fee Structure
    @Column(name = "admission_fees", precision = 10, scale = 2)
    private BigDecimal admissionFees;

    @Column(name = "uniform_fees", precision = 10, scale = 2)
    private BigDecimal uniformFees;

    @Column(name = "book_fees", precision = 10, scale = 2)
    private BigDecimal bookFees;

    @Column(name = "tuition_fees", precision = 10, scale = 2, nullable = false)
    private BigDecimal tuitionFees;

    @Column(name = "additional_fees", columnDefinition = "JSON")
    private String additionalFeesJson;

    @Column(name = "total_fees", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalFees;

    // Payment Details
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false)
    private PaymentMode paymentMode;

    @Column(name = "installment_count")
    private Integer installmentCount;

    @Column(name = "first_installment_date")
    private LocalDate firstInstallmentDate;

    @Column(name = "initial_payment", precision = 10, scale = 2, nullable = false)
    private BigDecimal initialPayment;

    @Column(name = "paid_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal paidAmount;

    @Column(name = "pending_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal pendingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "reference_number")
    private String referenceNumber;

    @OneToMany(mappedBy = "fee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FeeInstallment> installments = new ArrayList<>();

    @OneToMany(mappedBy = "fee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentTransaction> paymentHistory = new ArrayList<>();

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public FeeEntity() {

    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentEntity getStudent() { return student; }
    public void setStudent(StudentEntity student) { this.student = student; }

    public BigDecimal getAdmissionFees() { return admissionFees; }
    public void setAdmissionFees(BigDecimal admissionFees) { this.admissionFees = admissionFees; }

    public BigDecimal getUniformFees() { return uniformFees; }
    public void setUniformFees(BigDecimal uniformFees) { this.uniformFees = uniformFees; }

    public BigDecimal getBookFees() { return bookFees; }
    public void setBookFees(BigDecimal bookFees) { this.bookFees = bookFees; }

    public BigDecimal getTuitionFees() { return tuitionFees; }
    public void setTuitionFees(BigDecimal tuitionFees) { this.tuitionFees = tuitionFees; }

    public String getAdditionalFeesJson() { return additionalFeesJson; }
    public void setAdditionalFeesJson(String additionalFeesJson) { this.additionalFeesJson = additionalFeesJson; }

    public BigDecimal getTotalFees() { return totalFees; }
    public void setTotalFees(BigDecimal totalFees) { this.totalFees = totalFees; }

    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; }

    public Integer getInstallmentCount() { return installmentCount; }
    public void setInstallmentCount(Integer installmentCount) { this.installmentCount = installmentCount; }

    public LocalDate getFirstInstallmentDate() { return firstInstallmentDate; }
    public void setFirstInstallmentDate(LocalDate firstInstallmentDate) { this.firstInstallmentDate = firstInstallmentDate; }

    public BigDecimal getInitialPayment() { return initialPayment; }
    public void setInitialPayment(BigDecimal initialPayment) { this.initialPayment = initialPayment; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public BigDecimal getPendingAmount() { return pendingAmount; }
    public void setPendingAmount(BigDecimal pendingAmount) { this.pendingAmount = pendingAmount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public List<FeeInstallment> getInstallments() { return installments; }
    public void setInstallments(List<FeeInstallment> installments) { this.installments = installments != null ? installments : new ArrayList<>(); }

    public List<PaymentTransaction> getPaymentHistory() { return paymentHistory; }
    public void setPaymentHistory(List<PaymentTransaction> paymentHistory) { this.paymentHistory = paymentHistory != null ? paymentHistory : new ArrayList<>(); }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }


    // Helper methods (same logic)
    public void calculateTotalFees() {

        BigDecimal total = BigDecimal.ZERO;
        total = total.add(admissionFees != null ? admissionFees : BigDecimal.ZERO);
        total = total.add(uniformFees != null ? uniformFees : BigDecimal.ZERO);
        total = total.add(bookFees != null ? bookFees : BigDecimal.ZERO);
        total = total.add(tuitionFees != null ? tuitionFees : BigDecimal.ZERO);
        this.totalFees = total;
        this.pendingAmount = total.subtract(paidAmount != null ? paidAmount : BigDecimal.ZERO);
    }

    public void addPayment(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        this.paidAmount = (this.paidAmount != null ? this.paidAmount : BigDecimal.ZERO).add(amount);
        this.pendingAmount = this.totalFees.subtract(this.paidAmount);

        if (pendingAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.status = "PAID";  // Use String literal
        } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.status = "PARTIAL";  // Use String literal
        }
    }

}

