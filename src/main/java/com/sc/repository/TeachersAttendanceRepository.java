package com.sc.repository;


import com.sc.entity.TeachersAttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeachersAttendanceRepository extends JpaRepository<TeachersAttendanceEntity, Long> {

    // Find attendance by teacher and date
    Optional<TeachersAttendanceEntity> findByTeacherIdAndAttendanceDate(Long teacherId, LocalDate attendanceDate);

    // Find all attendance records for a teacher
    List<TeachersAttendanceEntity> findByTeacherId(Long teacherId);

    // Find attendance records for a teacher in a date range
    List<TeachersAttendanceEntity> findByTeacherIdAndAttendanceDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate);

    // Find attendance records for a specific date
    List<TeachersAttendanceEntity> findByAttendanceDate(LocalDate attendanceDate);

    // Find attendance records by date range
    List<TeachersAttendanceEntity> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

    // Find attendance by status
    List<TeachersAttendanceEntity> findByStatus(String status);

    // Find attendance by status and date
    List<TeachersAttendanceEntity> findByStatusAndAttendanceDate(String status, LocalDate attendanceDate);

    // Count attendance by status for a teacher in a month
    @Query("SELECT COUNT(a) FROM TeachersAttendanceEntity a WHERE a.teacherId = :teacherId AND a.status = :status AND YEAR(a.attendanceDate) = :year AND MONTH(a.attendanceDate) = :month")
    Long countByTeacherIdAndStatusAndMonth(@Param("teacherId") Long teacherId,
                                           @Param("status") String status,
                                           @Param("year") int year,
                                           @Param("month") int month);

    // Find attendance records not processed for salary
    List<TeachersAttendanceEntity> findByIsProcessedForSalaryFalse();

    // Find attendance records not processed for salary for a specific teacher
    List<TeachersAttendanceEntity> findByTeacherIdAndIsProcessedForSalaryFalse(Long teacherId);

    // Get attendance summary for a month
    @Query("SELECT a.status, COUNT(a) FROM TeachersAttendanceEntity a WHERE YEAR(a.attendanceDate) = :year AND MONTH(a.attendanceDate) = :month GROUP BY a.status")
    List<Object[]> getAttendanceSummaryByMonth(@Param("year") int year, @Param("month") int month);
}