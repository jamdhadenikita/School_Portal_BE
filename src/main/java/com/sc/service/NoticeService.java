package com.sc.service;

import com.sc.dto.request.NoticeRequestDTO;
import com.sc.dto.response.NoticeResponseDTO;

import java.util.List;
import java.util.Map;

public interface NoticeService {

    // Basic CRUD operations
    NoticeResponseDTO createNotice(NoticeRequestDTO requestDTO);
    NoticeResponseDTO updateNotice(Long id, NoticeRequestDTO requestDTO);
    void deleteNotice(Long id);
    NoticeResponseDTO getNoticeById(Long id);
    List<NoticeResponseDTO> getAllNotices();

    // Status operations
    NoticeResponseDTO publishNotice(Long id);
    NoticeResponseDTO archiveNotice(Long id);
    NoticeResponseDTO expireNotice(Long id);
    NoticeResponseDTO draftNotice(Long id);

    // Pin operations
    NoticeResponseDTO pinNotice(Long id, Integer priority);
    NoticeResponseDTO unpinNotice(Long id);
    List<NoticeResponseDTO> getPinnedNotices();

    // Query methods
    List<NoticeResponseDTO> getActiveNotices();
    List<NoticeResponseDTO> getUpcomingNotices();
    List<NoticeResponseDTO> getExpiredNotices();
    List<NoticeResponseDTO> getDraftNotices();

    // Target based queries
    List<NoticeResponseDTO> getNoticesByTargetType(String targetType);
    List<NoticeResponseDTO> getNoticesForStudent(Long studentId, String className, String section);
    List<NoticeResponseDTO> getNoticesForTeacher(Long teacherId);
    List<NoticeResponseDTO> getNoticesByClass(String className);
    List<NoticeResponseDTO> getNoticesByClassAndSection(String className, String section);
    List<NoticeResponseDTO> getNoticesByAudience(String audience);

    // Filter methods
    List<NoticeResponseDTO> getNoticesByCategory(String category);
    List<NoticeResponseDTO> getNoticesByPriority(String priority);
    List<NoticeResponseDTO> getNoticesByStatus(String status);
    List<NoticeResponseDTO> getNoticesByCreatedBy(String createdBy);

    // Search and filters
    List<NoticeResponseDTO> searchNotices(String keyword);
    List<NoticeResponseDTO> getNoticesByDateRange(String startDate, String endDate);
    List<NoticeResponseDTO> getNoticesByTag(String tag);
    List<NoticeResponseDTO> getNoticesByTags(List<String> tags);
    List<NoticeResponseDTO> getNoticesExpiringSoon();

    // Bulk operations
    void bulkDeleteNotices(List<Long> ids);
    void bulkUpdateStatus(List<Long> ids, String status);
    void bulkUpdatePriority(List<Long> ids, String priority);
    void bulkPinNotices(List<Long> ids, Integer priority);
    void bulkUnpinNotices(List<Long> ids);

    // Statistics and analytics
    Map<String, Object> getNoticeStatistics();
    Map<String, Long> getCountByStatus();
    Map<String, Long> getCountByCategory();
    Map<String, Long> getCountByPriority();
    Map<String, Object> getMonthlyNoticeStats(int year, int month);

    // Utility methods
    boolean existsById(Long id);
    long getTotalNoticeCount();
    List<NoticeResponseDTO> getLatestNotices(int limit);
    void cleanupExpiredNotices();

    // Recurring notices
    List<NoticeResponseDTO> getRecurringNotices();
    NoticeResponseDTO updateRecurringPattern(Long id, String pattern);

    // Acknowledgement
    List<NoticeResponseDTO> getNoticesRequiringAcknowledgement();
    void markAsAcknowledged(Long noticeId, Long userId);
    boolean isAcknowledged(Long noticeId, Long userId);
}