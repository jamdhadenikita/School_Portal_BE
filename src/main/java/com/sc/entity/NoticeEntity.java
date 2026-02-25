package com.sc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notices")
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String category;
    private String priority;
    private String status;

    private LocalDateTime publishDate;
    private LocalDateTime expiryDate;

    private String targetType;
    private String targetClass;
    private String targetSection;
    private Long targetStudentId;
    private Long targetTeacherId;
    private String audience;

    private Boolean sendNotification;
    private String notificationTitle;
    private String notificationMessage;

    private Boolean pinToTop;
    private Integer pinPriority;
    private String colorCode;
    private String icon;
    private Boolean showInCarousel;

    private String createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean isRecurring;
    private String recurrencePattern;
    private Boolean requiresAcknowledgement;

    @ElementCollection
    @CollectionTable(name = "notice_target_classes",
            joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "target_class")
    private List<String> targetClasses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "notice_target_sections",
            joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "target_section")
    private List<String> targetSections = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "notice_tags",
            joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "notice_attachments",
            joinColumns = @JoinColumn(name = "notice_id"))
    private List<Attachment> attachments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public NoticeEntity() {}

    public NoticeEntity(String title, String description, String category, String priority) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = "active";
        this.sendNotification = false;
        this.pinToTop = false;
        this.pinPriority = 0;
        this.showInCarousel = true;
        this.isRecurring = false;
        this.requiresAcknowledgement = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetSection() {
        return targetSection;
    }

    public void setTargetSection(String targetSection) {
        this.targetSection = targetSection;
    }

    public Long getTargetStudentId() {
        return targetStudentId;
    }

    public void setTargetStudentId(Long targetStudentId) {
        this.targetStudentId = targetStudentId;
    }

    public Long getTargetTeacherId() {
        return targetTeacherId;
    }

    public void setTargetTeacherId(Long targetTeacherId) {
        this.targetTeacherId = targetTeacherId;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public Boolean getSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public Boolean getPinToTop() {
        return pinToTop;
    }

    public void setPinToTop(Boolean pinToTop) {
        this.pinToTop = pinToTop;
    }

    public Integer getPinPriority() {
        return pinPriority;
    }

    public void setPinPriority(Integer pinPriority) {
        this.pinPriority = pinPriority;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getShowInCarousel() {
        return showInCarousel;
    }

    public void setShowInCarousel(Boolean showInCarousel) {
        this.showInCarousel = showInCarousel;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public Boolean getRequiresAcknowledgement() {
        return requiresAcknowledgement;
    }

    public void setRequiresAcknowledgement(Boolean requiresAcknowledgement) {
        this.requiresAcknowledgement = requiresAcknowledgement;
    }

    public List<String> getTargetClasses() {
        return targetClasses;
    }

    public void setTargetClasses(List<String> targetClasses) {
        this.targetClasses = targetClasses;
    }

    public List<String> getTargetSections() {
        return targetSections;
    }

    public void setTargetSections(List<String> targetSections) {
        this.targetSections = targetSections;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    // Embedded Attachment class
    @Embeddable
    public static class Attachment {

        private String fileName;
        private Long fileSize;
        private String fileType;
        private String fileUrl;

        public Attachment() {}

        public Attachment(String fileName, Long fileSize, String fileType, String fileUrl) {
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.fileType = fileType;
            this.fileUrl = fileUrl;
        }

        // Getters and Setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Long getFileSize() {
            return fileSize;
        }

        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }
}