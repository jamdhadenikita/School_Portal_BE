package com.sc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class Installment {

    @Column(name = "installment_id")
    private Long installmentId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "addon_amount")
    private Integer addonAmount = 0;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "status", nullable = false)
    private String status = "PENDING"; // PENDING, PAID, OVERDUE, CANCELLED

    @Column(name = "due_amount")
    private Integer dueAmount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "payment_mode")
    private String paymentMode; // CASH, ONLINE, CHEQUE, CARD

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "remarks")
    private String remarks;

    // ============= 🔄 CONSTRUCTORS =============

    public Installment() {
        this.addonAmount = 0;
        this.status = "PENDING";
    }

    public Installment(Long installmentId, Integer amount, LocalDate dueDate) {
        this.installmentId = installmentId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.dueAmount = amount;
        this.addonAmount = 0;
        this.status = "PENDING";
    }

    public Installment(Long installmentId, Integer amount, Integer addonAmount, LocalDate paidDate,
                       String status, Integer dueAmount, LocalDate dueDate) {
        this.installmentId = installmentId;
        this.amount = amount;
        this.addonAmount = addonAmount != null ? addonAmount : 0;
        this.paidDate = paidDate;
        this.status = status != null ? status : "PENDING";
        this.dueAmount = dueAmount;
        this.dueDate = dueDate;
    }

    // ============= 🎯 BUSINESS LOGIC METHODS =============

    /**
     * Mark installment as paid
     */
    public void markAsPaid() {
        this.status = "PAID";
        this.paidDate = LocalDate.now();
        this.dueAmount = 0;
    }

    /**
     * Mark installment as paid with payment details
     */
    public void markAsPaid(String paymentMode, String transactionReference) {
        this.status = "PAID";
        this.paidDate = LocalDate.now();
        this.dueAmount = 0;
        this.paymentMode = paymentMode;
        this.transactionReference = transactionReference;
    }

    /**
     * Mark installment as overdue
     */
    public void markAsOverdue() {
        if (!"PAID".equals(this.status) && !"CANCELLED".equals(this.status)) {
            if (LocalDate.now().isAfter(this.dueDate)) {
                this.status = "OVERDUE";
            }
        }
    }

    /**
     * Mark installment as cancelled
     */
    public void markAsCancelled(String reason) {
        this.status = "CANCELLED";
        this.remarks = reason;
        this.dueAmount = 0;
    }

    /**
     * Add late fee / penalty
     */
    public void addLateFee(Integer lateFee) {
        if (lateFee != null && lateFee > 0) {
            this.addonAmount = (this.addonAmount != null ? this.addonAmount : 0) + lateFee;
            this.dueAmount = (this.dueAmount != null ? this.dueAmount : this.amount) + lateFee;
        }
    }

    /**
     * Check if installment is paid
     */
    public boolean isPaid() {
        return "PAID".equalsIgnoreCase(this.status);
    }

    /**
     * Check if installment is overdue
     */
    public boolean isOverdue() {
        if ("PAID".equals(this.status) || "CANCELLED".equals(this.status)) {
            return false;
        }
        return LocalDate.now().isAfter(this.dueDate);
    }

    /**
     * Check if installment is pending
     */
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(this.status);
    }

    /**
     * Get total amount (amount + addon)
     */
    public Integer getTotalAmount() {
        return (amount != null ? amount : 0) + (addonAmount != null ? addonAmount : 0);
    }

    /**
     * Get remaining days until due date
     */
    public Long getRemainingDays() {
        if (dueDate == null) return null;
        return (long) LocalDate.now().until(dueDate).getDays();
    }

    /**
     * Check if installment is due today
     */
    public boolean isDueToday() {
        return dueDate != null && dueDate.equals(LocalDate.now());
    }

    /**
     * Apply discount to installment
     */
    public void applyDiscount(Integer discountAmount) {
        if (discountAmount != null && discountAmount > 0) {
            this.dueAmount = Math.max(0, (this.dueAmount != null ? this.dueAmount : this.amount) - discountAmount);
        }
    }

    // ============= 🔄 GETTERS AND SETTERS =============

    public Long getInstallmentId() {
        return installmentId;
    }

    public void setInstallmentId(Long installmentId) {
        this.installmentId = installmentId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
        if (this.dueAmount == null) {
            this.dueAmount = amount;
        }
    }

    public Integer getAddonAmount() {
        return addonAmount != null ? addonAmount : 0;
    }

    public void setAddonAmount(Integer addonAmount) {
        this.addonAmount = addonAmount != null ? addonAmount : 0;
        if (this.dueAmount != null && this.amount != null) {
            this.dueAmount = this.amount + this.addonAmount;
        }
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
        if (paidDate != null && "PENDING".equals(this.status)) {
            this.status = "PAID";
            this.dueAmount = 0;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if ("PAID".equalsIgnoreCase(status) && this.paidDate == null) {
            this.paidDate = LocalDate.now();
            this.dueAmount = 0;
        }
    }

    public Integer getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Integer dueAmount) {
        this.dueAmount = dueAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // ============= 📝 EQUALS, HASHCODE, TOSTRING =============

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Installment that = (Installment) o;
        return installmentId != null && installmentId.equals(that.installmentId);
    }

    @Override
    public int hashCode() {
        return installmentId != null ? installmentId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Installment{" +
                "installmentId=" + installmentId +
                ", amount=" + amount +
                ", addonAmount=" + addonAmount +
                ", totalAmount=" + getTotalAmount() +
                ", status='" + status + '\'' +
                ", dueDate=" + dueDate +
                ", dueAmount=" + dueAmount +
                ", paidDate=" + paidDate +
                '}';
    }
}