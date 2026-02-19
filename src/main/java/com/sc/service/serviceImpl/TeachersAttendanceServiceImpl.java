package com.sc.service.serviceImpl;

import com.sc.dto.request.TeachersAttendanceRequestDto;
import com.sc.dto.response.TeachersAttendanceResponseDto;
import com.sc.entity.TeachersAttendanceEntity;
import com.sc.repository.TeachersAttendanceRepository;
import com.sc.service.TeachersAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TeachersAttendanceServiceImpl implements TeachersAttendanceService {

    @Autowired
    private TeachersAttendanceRepository attendanceRepository;

    @Override
    public TeachersAttendanceResponseDto markAttendance(TeachersAttendanceRequestDto requestDto) {
        // Check if attendance already exists for this teacher and date
        Optional<TeachersAttendanceEntity> existingAttendance =
                attendanceRepository.findByTeacherIdAndAttendanceDate(
                        requestDto.getTeacherId(),
                        requestDto.getAttendanceDate()
                );

        TeachersAttendanceEntity attendanceEntity;

        if (existingAttendance.isPresent()) {
            // Update existing record
            attendanceEntity = existingAttendance.get();
        } else {
            // Create new record
            attendanceEntity = new TeachersAttendanceEntity();
            attendanceEntity.setTeacherId(requestDto.getTeacherId());
            attendanceEntity.setAttendanceDate(requestDto.getAttendanceDate());
        }

        // Update attendance details
        attendanceEntity.setStatus(requestDto.getStatus());
        attendanceEntity.setTimeIn(requestDto.getTimeIn());
        attendanceEntity.setTimeOut(requestDto.getTimeOut());
        attendanceEntity.setLateByMinutes(requestDto.getLateByMinutes());
        attendanceEntity.setRemarks(requestDto.getRemarks());
        attendanceEntity.setMarkedBy(requestDto.getMarkedBy());
        attendanceEntity.setIsProcessedForSalary(false);

        // Save to database
        TeachersAttendanceEntity savedEntity = attendanceRepository.save(attendanceEntity);

        // Convert to response DTO
        return convertToResponseDto(savedEntity);
    }

    @Override
    public TeachersAttendanceResponseDto getAttendanceById(Long id) {
        Optional<TeachersAttendanceEntity> attendanceEntity = attendanceRepository.findById(id);

        if (attendanceEntity.isPresent()) {
            return convertToResponseDto(attendanceEntity.get());
        }

        return null;
    }

    @Override
    public TeachersAttendanceResponseDto updateAttendance(Long id, TeachersAttendanceRequestDto requestDto) {
        Optional<TeachersAttendanceEntity> attendanceEntityOptional = attendanceRepository.findById(id);

        if (attendanceEntityOptional.isPresent()) {
            TeachersAttendanceEntity attendanceEntity = attendanceEntityOptional.get();

            // Update fields
            attendanceEntity.setStatus(requestDto.getStatus());
            attendanceEntity.setTimeIn(requestDto.getTimeIn());
            attendanceEntity.setTimeOut(requestDto.getTimeOut());
            attendanceEntity.setLateByMinutes(requestDto.getLateByMinutes());
            attendanceEntity.setRemarks(requestDto.getRemarks());
            attendanceEntity.setMarkedBy(requestDto.getMarkedBy());

            // Save updated entity
            TeachersAttendanceEntity updatedEntity = attendanceRepository.save(attendanceEntity);

            return convertToResponseDto(updatedEntity);
        }

        return null;
    }

    @Override
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    @Override
    public List<TeachersAttendanceResponseDto> getAllAttendance() {
        List<TeachersAttendanceEntity> attendanceEntities = attendanceRepository.findAll();
        List<TeachersAttendanceResponseDto> responseDtos = new ArrayList<>();

        for (TeachersAttendanceEntity entity : attendanceEntities) {
            responseDtos.add(convertToResponseDto(entity));
        }

        return responseDtos;
    }

    @Override
    public List<TeachersAttendanceResponseDto> getAttendanceByTeacherId(Long teacherId) {
        List<TeachersAttendanceEntity> attendanceEntities = attendanceRepository.findByTeacherId(teacherId);
        List<TeachersAttendanceResponseDto> responseDtos = new ArrayList<>();

        for (TeachersAttendanceEntity entity : attendanceEntities) {
            responseDtos.add(convertToResponseDto(entity));
        }

        return responseDtos;
    }

    @Override
    public List<TeachersAttendanceResponseDto> getAttendanceByDate(LocalDate date) {
        List<TeachersAttendanceEntity> attendanceEntities = attendanceRepository.findByAttendanceDate(date);
        List<TeachersAttendanceResponseDto> responseDtos = new ArrayList<>();

        for (TeachersAttendanceEntity entity : attendanceEntities) {
            responseDtos.add(convertToResponseDto(entity));
        }

        return responseDtos;
    }

    @Override
    public List<TeachersAttendanceResponseDto> getAttendanceByTeacherAndDateRange(Long teacherId, LocalDate startDate, LocalDate endDate) {
        List<TeachersAttendanceEntity> attendanceEntities =
                attendanceRepository.findByTeacherIdAndAttendanceDateBetween(teacherId, startDate, endDate);

        List<TeachersAttendanceResponseDto> responseDtos = new ArrayList<>();

        for (TeachersAttendanceEntity entity : attendanceEntities) {
            responseDtos.add(convertToResponseDto(entity));
        }

        return responseDtos;
    }

    @Override
    public List<TeachersAttendanceResponseDto> getTodaysAttendance() {
        LocalDate today = LocalDate.now();
        return getAttendanceByDate(today);
    }

    @Override
    public Object getAttendanceStatistics(LocalDate date) {
        List<TeachersAttendanceEntity> todaysAttendance = attendanceRepository.findByAttendanceDate(date);

        Map<String, Object> statistics = new HashMap<>();

        // Count by status
        Map<String, Long> statusCount = new HashMap<>();
        for (TeachersAttendanceEntity attendance : todaysAttendance) {
            String status = attendance.getStatus();
            statusCount.put(status, statusCount.getOrDefault(status, 0L) + 1);
        }

        statistics.put("totalTeachers", todaysAttendance.size());
        statistics.put("attendanceDate", date);
        statistics.put("statusBreakdown", statusCount);

        // Calculate attendance percentage
        long presentCount = statusCount.getOrDefault("Present", 0L);
        long halfDayCount = statusCount.getOrDefault("Half Day", 0L);
        long lateCount = statusCount.getOrDefault("Late", 0L);
        long leaveCount = statusCount.getOrDefault("On Leave", 0L);

        long effectivePresent = presentCount + (halfDayCount / 2) + (lateCount / 2) + leaveCount;
        double attendancePercentage = todaysAttendance.size() > 0 ?
                (effectivePresent * 100.0) / todaysAttendance.size() : 0.0;

        statistics.put("attendancePercentage", Math.round(attendancePercentage * 100.0) / 100.0);
        statistics.put("presentCount", presentCount);
        statistics.put("absentCount", statusCount.getOrDefault("Absent", 0L));
        statistics.put("lateCount", lateCount);
        statistics.put("halfDayCount", halfDayCount);
        statistics.put("leaveCount", leaveCount);

        return statistics;
    }

    @Override
    public List<TeachersAttendanceResponseDto> markBulkAttendance(List<TeachersAttendanceRequestDto> requestDtos) {
        List<TeachersAttendanceResponseDto> responseDtos = new ArrayList<>();

        for (TeachersAttendanceRequestDto requestDto : requestDtos) {
            TeachersAttendanceResponseDto responseDto = markAttendance(requestDto);
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @Override
    public Object getMonthlyAttendanceSummary(Long teacherId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<TeachersAttendanceEntity> monthlyAttendance =
                attendanceRepository.findByTeacherIdAndAttendanceDateBetween(teacherId, startDate, endDate);

        Map<String, Object> summary = new HashMap<>();

        // Count days by status
        Map<String, Long> statusCount = new HashMap<>();
        for (TeachersAttendanceEntity attendance : monthlyAttendance) {
            String status = attendance.getStatus();
            statusCount.put(status, statusCount.getOrDefault(status, 0L) + 1);
        }

        summary.put("teacherId", teacherId);
        summary.put("month", year + "-" + String.format("%02d", month));
        summary.put("totalDays", monthlyAttendance.size());
        summary.put("statusBreakdown", statusCount);

        // Calculate working days and attendance percentage
        long presentDays = statusCount.getOrDefault("Present", 0L);
        long halfDays = statusCount.getOrDefault("Half Day", 0L);
        long lateDays = statusCount.getOrDefault("Late", 0L);
        long leaveDays = statusCount.getOrDefault("On Leave", 0L);

        long workingDays = presentDays + (halfDays / 2) + (lateDays / 2) + leaveDays;
        double attendancePercentage = monthlyAttendance.size() > 0 ?
                (workingDays * 100.0) / monthlyAttendance.size() : 0.0;

        summary.put("workingDays", workingDays);
        summary.put("attendancePercentage", Math.round(attendancePercentage * 100.0) / 100.0);
        summary.put("presentDays", presentDays);
        summary.put("absentDays", statusCount.getOrDefault("Absent", 0L));
        summary.put("lateDays", lateDays);
        summary.put("halfDays", halfDays);
        summary.put("leaveDays", leaveDays);

        return summary;
    }

    // Helper method to convert entity to response DTO
    private TeachersAttendanceResponseDto convertToResponseDto(TeachersAttendanceEntity entity) {
        TeachersAttendanceResponseDto responseDto = new TeachersAttendanceResponseDto();

        responseDto.setId(entity.getId());
        responseDto.setTeacherId(entity.getTeacherId());
        responseDto.setAttendanceDate(entity.getAttendanceDate());
        responseDto.setStatus(entity.getStatus());
        responseDto.setTimeIn(entity.getTimeIn());
        responseDto.setTimeOut(entity.getTimeOut());
        responseDto.setLateByMinutes(entity.getLateByMinutes());
        responseDto.setRemarks(entity.getRemarks());
        responseDto.setMarkedBy(entity.getMarkedBy());
        responseDto.setCreatedAt(entity.getCreatedAt());
        responseDto.setUpdatedAt(entity.getUpdatedAt());
        responseDto.setIsProcessedForSalary(entity.getIsProcessedForSalary());

        // Note: teacherName and teacherCode would need to be fetched from TeacherService
        // For now, set as null or fetch if you have TeacherRepository
        responseDto.setTeacherName(null); // Would be populated if needed
        responseDto.setTeacherCode(null); // Would be populated if needed

        return responseDto;
    }
}


