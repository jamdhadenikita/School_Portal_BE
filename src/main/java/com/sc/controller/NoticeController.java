package com.sc.controller;

import com.sc.dto.request.NoticeRequestDTO;
import com.sc.dto.response.NoticeResponseDTO;
import com.sc.service.NoticeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // ==================== Basic CRUD Operations ====================

    @PostMapping("/create")
    public ResponseEntity<NoticeResponseDTO> createNotice(@RequestBody NoticeRequestDTO requestDTO) {
        NoticeResponseDTO responseDTO = noticeService.createNotice(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<NoticeResponseDTO> updateNotice(
            @PathVariable Long id,
            @RequestBody NoticeRequestDTO requestDTO) {
        NoticeResponseDTO responseDTO = noticeService.updateNotice(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<NoticeResponseDTO> getNoticeById(@PathVariable Long id) {
        NoticeResponseDTO responseDTO = noticeService.getNoticeById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<NoticeResponseDTO>> getAllNotices() {
        List<NoticeResponseDTO> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    // ==================== Status Operations ====================

    @PatchMapping("/publish/{id}")
    public ResponseEntity<NoticeResponseDTO> publishNotice(@PathVariable Long id) {
        NoticeResponseDTO responseDTO = noticeService.publishNotice(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/archive/{id}")
    public ResponseEntity<NoticeResponseDTO> archiveNotice(@PathVariable Long id) {
        NoticeResponseDTO responseDTO = noticeService.archiveNotice(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/expire/{id}")
    public ResponseEntity<NoticeResponseDTO> expireNotice(@PathVariable Long id) {
        NoticeResponseDTO responseDTO = noticeService.expireNotice(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/draft/{id}")
    public ResponseEntity<NoticeResponseDTO> draftNotice(@PathVariable Long id) {
        NoticeResponseDTO responseDTO = noticeService.draftNotice(id);
        return ResponseEntity.ok(responseDTO);
    }

    // ==================== Pin Operations ====================

    @PatchMapping("/pin/{id}")
    public ResponseEntity<NoticeResponseDTO> pinNotice(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer priority) {
        NoticeResponseDTO responseDTO = noticeService.pinNotice(id, priority);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/unpin/{id}")
    public ResponseEntity<NoticeResponseDTO> unpinNotice(@PathVariable Long id) {
        NoticeResponseDTO responseDTO = noticeService.unpinNotice(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/get-pinned")
    public ResponseEntity<List<NoticeResponseDTO>> getPinnedNotices() {
        List<NoticeResponseDTO> notices = noticeService.getPinnedNotices();
        return ResponseEntity.ok(notices);
    }

    // ==================== Status Based Queries ====================

    @GetMapping("/get-active")
    public ResponseEntity<List<NoticeResponseDTO>> getActiveNotices() {
        List<NoticeResponseDTO> notices = noticeService.getActiveNotices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-upcoming")
    public ResponseEntity<List<NoticeResponseDTO>> getUpcomingNotices() {
        List<NoticeResponseDTO> notices = noticeService.getUpcomingNotices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-expired")
    public ResponseEntity<List<NoticeResponseDTO>> getExpiredNotices() {
        List<NoticeResponseDTO> notices = noticeService.getExpiredNotices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-draft")
    public ResponseEntity<List<NoticeResponseDTO>> getDraftNotices() {
        List<NoticeResponseDTO> notices = noticeService.getDraftNotices();
        return ResponseEntity.ok(notices);
    }

    // ==================== Target Based Queries ====================

    @GetMapping("/get-by-target-type/{targetType}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByTargetType(@PathVariable String targetType) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByTargetType(targetType);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-for-student")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesForStudent(
            @RequestParam Long studentId,
            @RequestParam String className,
            @RequestParam String section) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesForStudent(studentId, className, section);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-for-teacher")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesForTeacher(@RequestParam Long teacherId) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesForTeacher(teacherId);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-by-class/{className}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByClass(@PathVariable String className) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByClass(className);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-by-class-section")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByClassAndSection(
            @RequestParam String className,
            @RequestParam String section) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByClassAndSection(className, section);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-by-audience/{audience}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByAudience(@PathVariable String audience) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByAudience(audience);
        return ResponseEntity.ok(notices);
    }

    // ==================== Filter Methods ====================

    @GetMapping("/get-by-category/{category}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByCategory(@PathVariable String category) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByCategory(category);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-by-priority/{priority}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByPriority(@PathVariable String priority) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByPriority(priority);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByStatus(@PathVariable String status) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByStatus(status);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-by-creator/{createdBy}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByCreatedBy(@PathVariable String createdBy) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByCreatedBy(createdBy);
        return ResponseEntity.ok(notices);
    }

    // ==================== Search and Filters ====================

    @GetMapping("/search")
    public ResponseEntity<List<NoticeResponseDTO>> searchNotices(@RequestParam String keyword) {
        List<NoticeResponseDTO> notices = noticeService.searchNotices(keyword);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-by-date-range")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByDateRange(startDate, endDate);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-by-tag/{tag}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByTag(@PathVariable String tag) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByTag(tag);
        return ResponseEntity.ok(notices);
    }

    @PostMapping("/get-by-tags")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByTags(@RequestBody List<String> tags) {
        List<NoticeResponseDTO> notices = noticeService.getNoticesByTags(tags);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/get-expiring-soon")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesExpiringSoon() {
        List<NoticeResponseDTO> notices = noticeService.getNoticesExpiringSoon();
        return ResponseEntity.ok(notices);
    }

    // ==================== Bulk Operations ====================

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<Void> bulkDeleteNotices(@RequestParam List<Long> ids) {
        noticeService.bulkDeleteNotices(ids);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/bulk-update-status")
    public ResponseEntity<Void> bulkUpdateStatus(
            @RequestParam List<Long> ids,
            @RequestParam String status) {
        noticeService.bulkUpdateStatus(ids, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/bulk-update-priority")
    public ResponseEntity<Void> bulkUpdatePriority(
            @RequestParam List<Long> ids,
            @RequestParam String priority) {
        noticeService.bulkUpdatePriority(ids, priority);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/bulk-pin")
    public ResponseEntity<Void> bulkPinNotices(
            @RequestParam List<Long> ids,
            @RequestParam(defaultValue = "1") Integer priority) {
        noticeService.bulkPinNotices(ids, priority);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/bulk-unpin")
    public ResponseEntity<Void> bulkUnpinNotices(@RequestParam List<Long> ids) {
        noticeService.bulkUnpinNotices(ids);
        return ResponseEntity.ok().build();
    }

    // ==================== Statistics and Analytics ====================

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getNoticeStatistics() {
        Map<String, Object> statistics = noticeService.getNoticeStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/statistics/by-status")
    public ResponseEntity<Map<String, Long>> getCountByStatus() {
        Map<String, Long> counts = noticeService.getCountByStatus();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/statistics/by-category")
    public ResponseEntity<Map<String, Long>> getCountByCategory() {
        Map<String, Long> counts = noticeService.getCountByCategory();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/statistics/by-priority")
    public ResponseEntity<Map<String, Long>> getCountByPriority() {
        Map<String, Long> counts = noticeService.getCountByPriority();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/statistics/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyNoticeStats(
            @RequestParam int year,
            @RequestParam int month) {
        Map<String, Object> stats = noticeService.getMonthlyNoticeStats(year, month);
        return ResponseEntity.ok(stats);
    }

    // ==================== Utility Methods ====================

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        boolean exists = noticeService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalNoticeCount() {
        long count = noticeService.getTotalNoticeCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<NoticeResponseDTO>> getLatestNotices(
            @RequestParam(defaultValue = "10") int limit) {
        List<NoticeResponseDTO> notices = noticeService.getLatestNotices(limit);
        return ResponseEntity.ok(notices);
    }

    @PostMapping("/cleanup-expired")
    public ResponseEntity<Void> cleanupExpiredNotices() {
        noticeService.cleanupExpiredNotices();
        return ResponseEntity.ok().build();
    }

    // ==================== Recurring Notices ====================

    @GetMapping("/recurring")
    public ResponseEntity<List<NoticeResponseDTO>> getRecurringNotices() {
        List<NoticeResponseDTO> notices = noticeService.getRecurringNotices();
        return ResponseEntity.ok(notices);
    }

    @PatchMapping("/{id}/recurring-pattern")
    public ResponseEntity<NoticeResponseDTO> updateRecurringPattern(
            @PathVariable Long id,
            @RequestParam String pattern) {
        NoticeResponseDTO responseDTO = noticeService.updateRecurringPattern(id, pattern);
        return ResponseEntity.ok(responseDTO);
    }

    // ==================== Acknowledgement ====================

    @GetMapping("/requires-acknowledgement")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesRequiringAcknowledgement() {
        List<NoticeResponseDTO> notices = noticeService.getNoticesRequiringAcknowledgement();
        return ResponseEntity.ok(notices);
    }

    @PostMapping("/{noticeId}/acknowledge/{userId}")
    public ResponseEntity<Void> markAsAcknowledged(
            @PathVariable Long noticeId,
            @PathVariable Long userId) {
        noticeService.markAsAcknowledged(noticeId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{noticeId}/acknowledged/{userId}")
    public ResponseEntity<Boolean> isAcknowledged(
            @PathVariable Long noticeId,
            @PathVariable Long userId) {
        boolean acknowledged = noticeService.isAcknowledged(noticeId, userId);
        return ResponseEntity.ok(acknowledged);
    }

    // ==================== Additional Utility Endpoints ====================

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Notice Service is running");
    }

    @GetMapping("/version")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok("1.0.0");
    }
}