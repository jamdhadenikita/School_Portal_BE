package com.sc.service.serviceImpl;

import com.sc.dto.request.NoticeRequestDTO;
import com.sc.dto.response.NoticeResponseDTO;
import com.sc.entity.NoticeEntity;
import com.sc.repository.NoticeRepository;
import com.sc.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // ==================== Basic CRUD Operations ====================

    @Override
    public NoticeResponseDTO createNotice(NoticeRequestDTO requestDTO) {
        validateNoticeRequest(requestDTO);
        NoticeEntity notice = convertToEntity(requestDTO);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());

        // Set default values if not provided
        if (notice.getStatus() == null) notice.setStatus("draft");
        if (notice.getPinToTop() == null) notice.setPinToTop(false);
        if (notice.getPinPriority() == null) notice.setPinPriority(0);
        if (notice.getShowInCarousel() == null) notice.setShowInCarousel(true);
        if (notice.getIsRecurring() == null) notice.setIsRecurring(false);
        if (notice.getRequiresAcknowledgement() == null) notice.setRequiresAcknowledgement(false);

        NoticeEntity savedNotice = noticeRepository.save(notice);
        return convertToResponseDTO(savedNotice);
    }

    @Override
    public NoticeResponseDTO updateNotice(Long id, NoticeRequestDTO requestDTO) {
        NoticeEntity existingNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));

        validateNoticeRequest(requestDTO);
        updateEntity(existingNotice, requestDTO);
        existingNotice.setUpdatedAt(LocalDateTime.now());

        NoticeEntity updatedNotice = noticeRepository.save(existingNotice);
        return convertToResponseDTO(updatedNotice);
    }

    @Override
    public void deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new RuntimeException("Notice not found with id: " + id);
        }
        noticeRepository.deleteById(id);
    }

    @Override
    public NoticeResponseDTO getNoticeById(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        return convertToResponseDTO(notice);
    }

    @Override
    public List<NoticeResponseDTO> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==================== Status Operations ====================

    @Override
    public NoticeResponseDTO publishNotice(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        notice.setStatus("active");
        if (notice.getPublishDate() == null) {
            notice.setPublishDate(LocalDateTime.now());
        }
        notice.setUpdatedAt(LocalDateTime.now());
        NoticeEntity updatedNotice = noticeRepository.save(notice);
        return convertToResponseDTO(updatedNotice);
    }

    @Override
    public NoticeResponseDTO archiveNotice(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        notice.setStatus("archived");
        notice.setUpdatedAt(LocalDateTime.now());
        NoticeEntity updatedNotice = noticeRepository.save(notice);
        return convertToResponseDTO(updatedNotice);
    }

    @Override
    public NoticeResponseDTO expireNotice(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        notice.setStatus("expired");
        notice.setUpdatedAt(LocalDateTime.now());
        NoticeEntity updatedNotice = noticeRepository.save(notice);
        return convertToResponseDTO(updatedNotice);
    }

    @Override
    public NoticeResponseDTO draftNotice(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        notice.setStatus("draft");
        notice.setUpdatedAt(LocalDateTime.now());
        NoticeEntity updatedNotice = noticeRepository.save(notice);
        return convertToResponseDTO(updatedNotice);
    }

    // ==================== Pin Operations ====================

    @Override
    public NoticeResponseDTO pinNotice(Long id, Integer priority) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        notice.setPinToTop(true);
        notice.setPinPriority(priority != null ? priority : 1);
        notice.setUpdatedAt(LocalDateTime.now());
        NoticeEntity updatedNotice = noticeRepository.save(notice);
        return convertToResponseDTO(updatedNotice);
    }

    @Override
    public NoticeResponseDTO unpinNotice(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        notice.setPinToTop(false);
        notice.setPinPriority(0);
        notice.setUpdatedAt(LocalDateTime.now());
        NoticeEntity updatedNotice = noticeRepository.save(notice);
        return convertToResponseDTO(updatedNotice);
    }

    @Override
    public List<NoticeResponseDTO> getPinnedNotices() {
        return noticeRepository.findPinnedNotices().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==================== Query Methods ====================

    @Override
    public List<NoticeResponseDTO> getActiveNotices() {
        return noticeRepository.findActiveNotices(LocalDateTime.now()).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getUpcomingNotices() {
        return noticeRepository.findUpcomingNotices(LocalDateTime.now()).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getExpiredNotices() {
        return noticeRepository.findExpiredNotices(LocalDateTime.now()).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getDraftNotices() {
        return noticeRepository.findByStatus("draft").stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==================== Target Based Queries ====================

    @Override
    public List<NoticeResponseDTO> getNoticesByTargetType(String targetType) {
        return noticeRepository.findByTargetType(targetType).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesForStudent(Long studentId, String className, String section) {
        return noticeRepository.findAll().stream()
                .filter(notice -> isNoticeForStudent(notice, studentId, className, section))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesForTeacher(Long teacherId) {
        return noticeRepository.findAll().stream()
                .filter(notice -> isNoticeForTeacher(notice, teacherId))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesByClass(String className) {
        List<NoticeEntity> notices = new ArrayList<>();
        notices.addAll(noticeRepository.findByTargetClass(className));
        notices.addAll(noticeRepository.findByClassAndSection(className, null));
        return notices.stream()
                .distinct()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesByClassAndSection(String className, String section) {
        return noticeRepository.findByClassAndSection(className, section).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesByAudience(String audience) {
        return noticeRepository.findByAudience(audience).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==================== Filter Methods ====================

    @Override
    public List<NoticeResponseDTO> getNoticesByCategory(String category) {
        return noticeRepository.findByCategory(category).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesByPriority(String priority) {
        return noticeRepository.findByPriority(priority).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesByStatus(String status) {
        return noticeRepository.findByStatus(status).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesByCreatedBy(String createdBy) {
        return noticeRepository.findByCreatedBy(createdBy).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==================== Search and Filters ====================

    @Override
    public List<NoticeResponseDTO> searchNotices(String keyword) {
        return noticeRepository.searchNotices(keyword).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesByDateRange(String startDate, String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate, DATE_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endDate, DATE_FORMATTER);

        return noticeRepository.findByDateRange(start, end).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesByTag(String tag) {
        return noticeRepository.findByTag(tag).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        return noticeRepository.findByTags(tags).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDTO> getNoticesExpiringSoon() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusDays(7);
        return noticeRepository.findNoticesExpiringSoon(now, nextWeek).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==================== Bulk Operations ====================

    @Override
    public void bulkDeleteNotices(List<Long> ids) {
        List<NoticeEntity> notices = noticeRepository.findAllById(ids);
        noticeRepository.deleteAll(notices);
    }

    @Override
    public void bulkUpdateStatus(List<Long> ids, String status) {
        List<NoticeEntity> notices = noticeRepository.findAllById(ids);
        for (NoticeEntity notice : notices) {
            notice.setStatus(status);
            notice.setUpdatedAt(LocalDateTime.now());
        }
        noticeRepository.saveAll(notices);
    }

    @Override
    public void bulkUpdatePriority(List<Long> ids, String priority) {
        List<NoticeEntity> notices = noticeRepository.findAllById(ids);
        for (NoticeEntity notice : notices) {
            notice.setPriority(priority);
            notice.setUpdatedAt(LocalDateTime.now());
        }
        noticeRepository.saveAll(notices);
    }

    @Override
    public void bulkPinNotices(List<Long> ids, Integer priority) {
        List<NoticeEntity> notices = noticeRepository.findAllById(ids);
        for (NoticeEntity notice : notices) {
            notice.setPinToTop(true);
            notice.setPinPriority(priority);
            notice.setUpdatedAt(LocalDateTime.now());
        }
        noticeRepository.saveAll(notices);
    }

    @Override
    public void bulkUnpinNotices(List<Long> ids) {
        List<NoticeEntity> notices = noticeRepository.findAllById(ids);
        for (NoticeEntity notice : notices) {
            notice.setPinToTop(false);
            notice.setPinPriority(0);
            notice.setUpdatedAt(LocalDateTime.now());
        }
        noticeRepository.saveAll(notices);
    }

    // ==================== Statistics and Analytics ====================

    @Override
    public Map<String, Object> getNoticeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", noticeRepository.count());
        stats.put("active", noticeRepository.countByStatus("active"));
        stats.put("draft", noticeRepository.countByStatus("draft"));
        stats.put("archived", noticeRepository.countByStatus("archived"));
        stats.put("expired", noticeRepository.countByStatus("expired"));
        stats.put("pinned", noticeRepository.findPinnedNotices().size());
        stats.put("recurring", noticeRepository.findByIsRecurring(true).size());
        stats.put("requiresAcknowledgement", noticeRepository.findByRequiresAcknowledgement(true).size());
        return stats;
    }

    @Override
    public Map<String, Long> getCountByStatus() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("active", noticeRepository.countByStatus("active"));
        counts.put("draft", noticeRepository.countByStatus("draft"));
        counts.put("archived", noticeRepository.countByStatus("archived"));
        counts.put("expired", noticeRepository.countByStatus("expired"));
        return counts;
    }

    @Override
    public Map<String, Long> getCountByCategory() {
        List<NoticeEntity> allNotices = noticeRepository.findAll();
        return allNotices.stream()
                .filter(n -> n.getCategory() != null)
                .collect(Collectors.groupingBy(NoticeEntity::getCategory, Collectors.counting()));
    }

    @Override
    public Map<String, Long> getCountByPriority() {
        List<NoticeEntity> allNotices = noticeRepository.findAll();
        return allNotices.stream()
                .filter(n -> n.getPriority() != null)
                .collect(Collectors.groupingBy(NoticeEntity::getPriority, Collectors.counting()));
    }

    @Override
    public Map<String, Object> getMonthlyNoticeStats(int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        List<NoticeEntity> monthNotices = noticeRepository.findByCreatedDateBetween(startOfMonth, endOfMonth);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", monthNotices.size());
        stats.put("byCategory", monthNotices.stream()
                .filter(n -> n.getCategory() != null)
                .collect(Collectors.groupingBy(NoticeEntity::getCategory, Collectors.counting())));
        stats.put("byPriority", monthNotices.stream()
                .filter(n -> n.getPriority() != null)
                .collect(Collectors.groupingBy(NoticeEntity::getPriority, Collectors.counting())));
        stats.put("byStatus", monthNotices.stream()
                .filter(n -> n.getStatus() != null)
                .collect(Collectors.groupingBy(NoticeEntity::getStatus, Collectors.counting())));

        return stats;
    }

    // ==================== Utility Methods ====================

    @Override
    public boolean existsById(Long id) {
        return noticeRepository.existsById(id);
    }

    @Override
    public long getTotalNoticeCount() {
        return noticeRepository.count();
    }

    @Override
    public List<NoticeResponseDTO> getLatestNotices(int limit) {
        return noticeRepository.findLatestNotices().stream()
                .limit(limit)
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void cleanupExpiredNotices() {
        LocalDateTime now = LocalDateTime.now();
        List<NoticeEntity> expiredNotices = noticeRepository.findExpiredNoticesForCleanup(now);
        if (!expiredNotices.isEmpty()) {
            noticeRepository.deleteAll(expiredNotices);
        }
    }

    // ==================== Recurring Notices ====================

    @Override
    public List<NoticeResponseDTO> getRecurringNotices() {
        return noticeRepository.findByIsRecurring(true).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NoticeResponseDTO updateRecurringPattern(Long id, String pattern) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        notice.setRecurrencePattern(pattern);
        notice.setUpdatedAt(LocalDateTime.now());
        NoticeEntity updatedNotice = noticeRepository.save(notice);
        return convertToResponseDTO(updatedNotice);
    }

    // ==================== Acknowledgement ====================

    @Override
    public List<NoticeResponseDTO> getNoticesRequiringAcknowledgement() {
        return noticeRepository.findByRequiresAcknowledgement(true).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsAcknowledged(Long noticeId, Long userId) {
        // This would typically update an acknowledgement table
        // For now, we'll just log it
        System.out.println("Notice " + noticeId + " acknowledged by user " + userId);
    }

    @Override
    public boolean isAcknowledged(Long noticeId, Long userId) {
        // This would check an acknowledgement table
        // For now, return false
        return false;
    }

    // ==================== Private Helper Methods ====================

    private void validateNoticeRequest(NoticeRequestDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (dto.getPublishDate() != null && dto.getExpiryDate() != null) {
            if (dto.getPublishDate().isAfter(dto.getExpiryDate())) {
                throw new IllegalArgumentException("Publish date must be before expiry date");
            }
        }
    }

    private boolean isNoticeForStudent(NoticeEntity notice, Long studentId, String className, String section) {
        String targetType = notice.getTargetType();

        if (targetType == null) return false;

        switch (targetType) {
            case "ALL":
            case "ALL_STUDENTS":
                return true;
            case "CLASS":
                return className != null && className.equals(notice.getTargetClass());
            case "SECTION":
                return className != null && section != null &&
                        className.equals(notice.getTargetClass()) &&
                        section.equals(notice.getTargetSection());
            case "STUDENT":
                return studentId != null && studentId.equals(notice.getTargetStudentId());
            case "MULTIPLE_CLASSES":
                return notice.getTargetClasses() != null &&
                        className != null &&
                        notice.getTargetClasses().contains(className);
            case "MULTIPLE_SECTIONS":
                return className != null && section != null &&
                        className.equals(notice.getTargetClass()) &&
                        notice.getTargetSections() != null &&
                        notice.getTargetSections().contains(section);
            default:
                return false;
        }
    }

    private boolean isNoticeForTeacher(NoticeEntity notice, Long teacherId) {
        String targetType = notice.getTargetType();

        if (targetType == null) return false;

        switch (targetType) {
            case "ALL":
            case "ALL_TEACHERS":
                return true;
            case "TEACHER":
                return teacherId != null && teacherId.equals(notice.getTargetTeacherId());
            default:
                return false;
        }
    }

    private NoticeEntity convertToEntity(NoticeRequestDTO dto) {
        NoticeEntity notice = new NoticeEntity();
        notice.setTitle(dto.getTitle());
        notice.setDescription(dto.getDescription());
        notice.setCategory(dto.getCategory());
        notice.setPriority(dto.getPriority());
        notice.setStatus(dto.getStatus());
        notice.setPublishDate(dto.getPublishDate());
        notice.setExpiryDate(dto.getExpiryDate());
        notice.setTargetType(dto.getTargetType());
        notice.setTargetClass(dto.getTargetClass());
        notice.setTargetSection(dto.getTargetSection());
        notice.setTargetStudentId(dto.getTargetStudentId());
        notice.setTargetTeacherId(dto.getTargetTeacherId());
        notice.setTargetClasses(dto.getTargetClasses());
        notice.setTargetSections(dto.getTargetSections());
        notice.setAudience(dto.getAudience());
        notice.setSendNotification(dto.getSendNotification());
        notice.setNotificationTitle(dto.getNotificationTitle());
        notice.setNotificationMessage(dto.getNotificationMessage());
        notice.setPinToTop(dto.getPinToTop());
        notice.setPinPriority(dto.getPinPriority());
        notice.setColorCode(dto.getColorCode());
        notice.setIcon(dto.getIcon());
        notice.setShowInCarousel(dto.getShowInCarousel());
        notice.setCreatedBy(dto.getCreatedBy());
        notice.setCreatedByName(dto.getCreatedByName());
        notice.setTags(dto.getTags());
        notice.setIsRecurring(dto.getIsRecurring());
        notice.setRecurrencePattern(dto.getRecurrencePattern());
        notice.setRequiresAcknowledgement(dto.getRequiresAcknowledgement());

        if (dto.getAttachments() != null) {
            List<NoticeEntity.Attachment> attachments = dto.getAttachments().stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());
            notice.setAttachments(attachments);
        }

        return notice;
    }

    private NoticeEntity.Attachment convertToEntity(NoticeRequestDTO.AttachmentRequestDTO dto) {
        return new NoticeEntity.Attachment(
                dto.getFileName(),
                dto.getFileSize(),
                dto.getFileType(),
                dto.getFileUrl()
        );
    }

    private void updateEntity(NoticeEntity notice, NoticeRequestDTO dto) {
        if (dto.getTitle() != null) notice.setTitle(dto.getTitle());
        if (dto.getDescription() != null) notice.setDescription(dto.getDescription());
        if (dto.getCategory() != null) notice.setCategory(dto.getCategory());
        if (dto.getPriority() != null) notice.setPriority(dto.getPriority());
        if (dto.getStatus() != null) notice.setStatus(dto.getStatus());
        if (dto.getPublishDate() != null) notice.setPublishDate(dto.getPublishDate());
        if (dto.getExpiryDate() != null) notice.setExpiryDate(dto.getExpiryDate());
        if (dto.getTargetType() != null) notice.setTargetType(dto.getTargetType());
        if (dto.getTargetClass() != null) notice.setTargetClass(dto.getTargetClass());
        if (dto.getTargetSection() != null) notice.setTargetSection(dto.getTargetSection());
        if (dto.getTargetStudentId() != null) notice.setTargetStudentId(dto.getTargetStudentId());
        if (dto.getTargetTeacherId() != null) notice.setTargetTeacherId(dto.getTargetTeacherId());
        if (dto.getTargetClasses() != null) notice.setTargetClasses(dto.getTargetClasses());
        if (dto.getTargetSections() != null) notice.setTargetSections(dto.getTargetSections());
        if (dto.getAudience() != null) notice.setAudience(dto.getAudience());
        if (dto.getSendNotification() != null) notice.setSendNotification(dto.getSendNotification());
        if (dto.getNotificationTitle() != null) notice.setNotificationTitle(dto.getNotificationTitle());
        if (dto.getNotificationMessage() != null) notice.setNotificationMessage(dto.getNotificationMessage());
        if (dto.getPinToTop() != null) notice.setPinToTop(dto.getPinToTop());
        if (dto.getPinPriority() != null) notice.setPinPriority(dto.getPinPriority());
        if (dto.getColorCode() != null) notice.setColorCode(dto.getColorCode());
        if (dto.getIcon() != null) notice.setIcon(dto.getIcon());
        if (dto.getShowInCarousel() != null) notice.setShowInCarousel(dto.getShowInCarousel());
        if (dto.getTags() != null) notice.setTags(dto.getTags());
        if (dto.getIsRecurring() != null) notice.setIsRecurring(dto.getIsRecurring());
        if (dto.getRecurrencePattern() != null) notice.setRecurrencePattern(dto.getRecurrencePattern());
        if (dto.getRequiresAcknowledgement() != null) notice.setRequiresAcknowledgement(dto.getRequiresAcknowledgement());
        if (dto.getAttachments() != null) {
            List<NoticeEntity.Attachment> attachments = dto.getAttachments().stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());
            notice.setAttachments(attachments);
        }
    }

    private NoticeResponseDTO convertToResponseDTO(NoticeEntity notice) {
        NoticeResponseDTO dto = new NoticeResponseDTO();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setDescription(notice.getDescription());
        dto.setCategory(notice.getCategory());
        dto.setPriority(notice.getPriority());
        dto.setStatus(notice.getStatus());
        dto.setPublishDate(notice.getPublishDate());
        dto.setExpiryDate(notice.getExpiryDate());
        dto.setTargetType(notice.getTargetType());
        dto.setTargetClass(notice.getTargetClass());
        dto.setTargetSection(notice.getTargetSection());
        dto.setTargetStudentId(notice.getTargetStudentId());
        dto.setTargetTeacherId(notice.getTargetTeacherId());
        dto.setTargetClasses(notice.getTargetClasses());
        dto.setTargetSections(notice.getTargetSections());
        dto.setAudience(notice.getAudience());
        dto.setSendNotification(notice.getSendNotification());
        dto.setNotificationTitle(notice.getNotificationTitle());
        dto.setNotificationMessage(notice.getNotificationMessage());
        dto.setPinToTop(notice.getPinToTop());
        dto.setPinPriority(notice.getPinPriority());
        dto.setColorCode(notice.getColorCode());
        dto.setIcon(notice.getIcon());
        dto.setShowInCarousel(notice.getShowInCarousel());
        dto.setCreatedBy(notice.getCreatedBy());
        dto.setCreatedByName(notice.getCreatedByName());
        dto.setCreatedAt(notice.getCreatedAt());
        dto.setUpdatedAt(notice.getUpdatedAt());
        dto.setTags(notice.getTags());
        dto.setIsRecurring(notice.getIsRecurring());
        dto.setRecurrencePattern(notice.getRecurrencePattern());
        dto.setRequiresAcknowledgement(notice.getRequiresAcknowledgement());

        if (notice.getAttachments() != null) {
            List<NoticeResponseDTO.AttachmentResponseDTO> attachmentDTOs = notice.getAttachments().stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
            dto.setAttachments(attachmentDTOs);
        }

        return dto;
    }

    private NoticeResponseDTO.AttachmentResponseDTO convertToResponseDTO(NoticeEntity.Attachment attachment) {
        return new NoticeResponseDTO.AttachmentResponseDTO(
                attachment.getFileName(),
                attachment.getFileSize(),
                attachment.getFileType(),
                attachment.getFileUrl()
        );
    }
}