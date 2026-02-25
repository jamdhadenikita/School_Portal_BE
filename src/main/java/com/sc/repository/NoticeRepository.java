package com.sc.repository;

import com.sc.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    // Find by status
    List<NoticeEntity> findByStatus(String status);

    // Find by category
    List<NoticeEntity> findByCategory(String category);

    // Find by priority
    List<NoticeEntity> findByPriority(String priority);

    // Find by createdBy
    List<NoticeEntity> findByCreatedBy(String createdBy);

    // Find active notices (published and not expired)
    @Query("SELECT n FROM NoticeEntity n WHERE n.publishDate <= :currentDate AND n.expiryDate >= :currentDate AND n.status = 'active'")
    List<NoticeEntity> findActiveNotices(@Param("currentDate") LocalDateTime currentDate);

    // Find upcoming notices (not yet published)
    @Query("SELECT n FROM NoticeEntity n WHERE n.publishDate > :currentDate AND n.status = 'active'")
    List<NoticeEntity> findUpcomingNotices(@Param("currentDate") LocalDateTime currentDate);

    // Find expired notices
    @Query("SELECT n FROM NoticeEntity n WHERE n.expiryDate < :currentDate")
    List<NoticeEntity> findExpiredNotices(@Param("currentDate") LocalDateTime currentDate);

    // Find by target type
    @Query("SELECT n FROM NoticeEntity n WHERE n.targetType = :targetType")
    List<NoticeEntity> findByTargetType(@Param("targetType") String targetType);

    // Find by class name in targetClasses
    @Query("SELECT n FROM NoticeEntity n WHERE :className MEMBER OF n.targetClasses")
    List<NoticeEntity> findByTargetClass(@Param("className") String className);

    // Find by class and section
    @Query("SELECT n FROM NoticeEntity n WHERE n.targetClass = :className AND n.targetSection = :section")
    List<NoticeEntity> findByClassAndSection(@Param("className") String className, @Param("section") String section);

    // Find pinned notices
    @Query("SELECT n FROM NoticeEntity n WHERE n.pinToTop = true ORDER BY n.pinPriority DESC")
    List<NoticeEntity> findPinnedNotices();

    // Search by title or description
    @Query("SELECT n FROM NoticeEntity n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<NoticeEntity> searchNotices(@Param("keyword") String keyword);

    // Find by date range
    @Query("SELECT n FROM NoticeEntity n WHERE n.publishDate BETWEEN :startDate AND :endDate")
    List<NoticeEntity> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Find by multiple classes
    @Query("SELECT n FROM NoticeEntity n WHERE :className MEMBER OF n.targetClasses")
    List<NoticeEntity> findByMultipleClasses(@Param("className") String className);

    // Find by multiple sections for a specific class
    @Query("SELECT n FROM NoticeEntity n WHERE n.targetClass = :className AND :section MEMBER OF n.targetSections")
    List<NoticeEntity> findByMultipleSections(@Param("className") String className, @Param("section") String section);

    // Find by audience
    List<NoticeEntity> findByAudience(String audience);

    // Find by student ID
    List<NoticeEntity> findByTargetStudentId(Long studentId);

    // Find by teacher ID
    List<NoticeEntity> findByTargetTeacherId(Long teacherId);

    // Find by recurring flag
    List<NoticeEntity> findByIsRecurring(Boolean isRecurring);

    // Find by requires acknowledgement
    List<NoticeEntity> findByRequiresAcknowledgement(Boolean requiresAcknowledgement);

    // Count by status
    @Query("SELECT COUNT(n) FROM NoticeEntity n WHERE n.status = :status")
    long countByStatus(@Param("status") String status);

    // Count by category
    @Query("SELECT COUNT(n) FROM NoticeEntity n WHERE n.category = :category")
    long countByCategory(@Param("category") String category);

    // Find notices created between dates
    @Query("SELECT n FROM NoticeEntity n WHERE n.createdAt BETWEEN :startDate AND :endDate")
    List<NoticeEntity> findByCreatedDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Find notices expiring soon (within next 7 days)
    @Query("SELECT n FROM NoticeEntity n WHERE n.expiryDate BETWEEN :currentDate AND :nextWeekDate AND n.status = 'active'")
    List<NoticeEntity> findNoticesExpiringSoon(@Param("currentDate") LocalDateTime currentDate, @Param("nextWeekDate") LocalDateTime nextWeekDate);

    // Find expired notices for cleanup
    @Query("SELECT n FROM NoticeEntity n WHERE n.expiryDate < :currentDate AND n.status = 'expired'")
    List<NoticeEntity> findExpiredNoticesForCleanup(@Param("currentDate") LocalDateTime currentDate);

    // Find by single tag
    @Query("SELECT n FROM NoticeEntity n WHERE :tag MEMBER OF n.tags")
    List<NoticeEntity> findByTag(@Param("tag") String tag);

    // Find by multiple tags - USING JOIN (CORRECTED)
    @Query("SELECT DISTINCT n FROM NoticeEntity n JOIN n.tags t WHERE t IN :tags")
    List<NoticeEntity> findByTags(@Param("tags") List<String> tags);

    // Alternative: Find by multiple tags using OR condition
    @Query("SELECT n FROM NoticeEntity n WHERE :tag1 MEMBER OF n.tags OR :tag2 MEMBER OF n.tags OR :tag3 MEMBER OF n.tags")
    List<NoticeEntity> findByTagsOr(@Param("tag1") String tag1, @Param("tag2") String tag2, @Param("tag3") String tag3);

    // Find latest notices
    @Query("SELECT n FROM NoticeEntity n ORDER BY n.createdAt DESC")
    List<NoticeEntity> findLatestNotices();

    // Find latest notices with limit
    @Query(value = "SELECT * FROM notices ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<NoticeEntity> findLatestNoticesWithLimit(@Param("limit") int limit);
}