package com.sc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_types")
public class LeaveTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leave_code", unique = true, nullable = false)
    private String leaveCode; // SL, CL, EL, etc.

    @Column(name = "leave_name", nullable = false)
    private String leaveName; // Sick Leave, Casual Leave, Emergency Leave

    @Column(name = "affects_attendance")
    private Boolean affectsAttendance = false; // false = present % mein count hoga

    @Column(name = "max_days_per_month")
    private Integer maxDaysPerMonth;

    @Column(name = "max_days_per_year")
    private Integer maxDaysPerYear;

    @Column(name = "requires_approval")
    private Boolean requiresApproval = true;

    @Column(name = "description")
    private String description;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLeaveCode() { return leaveCode; }
    public void setLeaveCode(String leaveCode) { this.leaveCode = leaveCode; }

    public String getLeaveName() { return leaveName; }
    public void setLeaveName(String leaveName) { this.leaveName = leaveName; }

    public Boolean getAffectsAttendance() { return affectsAttendance; }
    public void setAffectsAttendance(Boolean affectsAttendance) { this.affectsAttendance = affectsAttendance; }

    public Integer getMaxDaysPerMonth() { return maxDaysPerMonth; }
    public void setMaxDaysPerMonth(Integer maxDaysPerMonth) { this.maxDaysPerMonth = maxDaysPerMonth; }

    public Integer getMaxDaysPerYear() { return maxDaysPerYear; }
    public void setMaxDaysPerYear(Integer maxDaysPerYear) { this.maxDaysPerYear = maxDaysPerYear; }

    public Boolean getRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(Boolean requiresApproval) { this.requiresApproval = requiresApproval; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
