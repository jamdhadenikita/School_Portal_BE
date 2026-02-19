package com.sc.service;


import com.sc.dto.request.TeachersAttendanceRequestDto;
import com.sc.dto.response.TeachersAttendanceResponseDto;

import java.time.LocalDate;
import java.util.List;
public interface TeachersAttendanceService {

    // Create new attendance record
    TeachersAttendanceResponseDto markAttendance(TeachersAttendanceRequestDto requestDto);

    // Get attendance by ID
    TeachersAttendanceResponseDto getAttendanceById(Long id);

    // Update attendance record
    TeachersAttendanceResponseDto updateAttendance(Long id, TeachersAttendanceRequestDto requestDto);

    // Delete attendance record
    void deleteAttendance(Long id);

    // Get all attendance records
    List<TeachersAttendanceResponseDto> getAllAttendance();

    // Get attendance by teacher ID
    List<TeachersAttendanceResponseDto> getAttendanceByTeacherId(Long teacherId);

    // Get attendance by date
    List<TeachersAttendanceResponseDto> getAttendanceByDate(LocalDate date);

    // Get attendance by teacher and date range
    List<TeachersAttendanceResponseDto> getAttendanceByTeacherAndDateRange(Long teacherId, LocalDate startDate, LocalDate endDate);

    // Get today's attendance
    List<TeachersAttendanceResponseDto> getTodaysAttendance();

    // Get attendance statistics
    Object getAttendanceStatistics(LocalDate date);

    // Mark bulk attendance (multiple teachers)
    List<TeachersAttendanceResponseDto> markBulkAttendance(List<TeachersAttendanceRequestDto> requestDtos);

    // Get monthly attendance summary for a teacher
    Object getMonthlyAttendanceSummary(Long teacherId, int year, int month);
}

