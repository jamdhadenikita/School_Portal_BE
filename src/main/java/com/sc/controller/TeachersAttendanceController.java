package com.sc.controller;

import com.sc.dto.request.TeachersAttendanceRequestDto;
import com.sc.dto.response.TeachersAttendanceResponseDto;
import com.sc.service.TeachersAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/teachers-attendance")
@CrossOrigin("*")
public class TeachersAttendanceController {

    @Autowired
    private TeachersAttendanceService attendanceService;

    // Mark attendance for a teacher
    @PostMapping("/mark")
    public ResponseEntity<TeachersAttendanceResponseDto> markAttendance(@RequestBody TeachersAttendanceRequestDto requestDto) {
        TeachersAttendanceResponseDto responseDto = attendanceService.markAttendance(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // Mark attendance for multiple teachers
    @PostMapping("/mark-bulk")
    public ResponseEntity<List<TeachersAttendanceResponseDto>> markBulkAttendance(@RequestBody List<TeachersAttendanceRequestDto> requestDtos) {
        List<TeachersAttendanceResponseDto> responseDtos = attendanceService.markBulkAttendance(requestDtos);
        return new ResponseEntity<>(responseDtos, HttpStatus.CREATED);
    }

    // Get attendance by ID
    @GetMapping("/{id}")
    public ResponseEntity<TeachersAttendanceResponseDto> getAttendanceById(@PathVariable Long id) {
        TeachersAttendanceResponseDto responseDto = attendanceService.getAttendanceById(id);
        if (responseDto != null) {
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update attendance
    @PutMapping("/{id}")
    public ResponseEntity<TeachersAttendanceResponseDto> updateAttendance(@PathVariable Long id, @RequestBody TeachersAttendanceRequestDto requestDto) {
        TeachersAttendanceResponseDto responseDto = attendanceService.updateAttendance(id, requestDto);
        if (responseDto != null) {
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete attendance
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all attendance records
    @GetMapping("/all")
    public ResponseEntity<List<TeachersAttendanceResponseDto>> getAllAttendance() {
        List<TeachersAttendanceResponseDto> responseDtos = attendanceService.getAllAttendance();
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    // Get attendance by teacher ID
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeachersAttendanceResponseDto>> getAttendanceByTeacherId(@PathVariable Long teacherId) {
        List<TeachersAttendanceResponseDto> responseDtos = attendanceService.getAttendanceByTeacherId(teacherId);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    // Get attendance by date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<TeachersAttendanceResponseDto>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TeachersAttendanceResponseDto> responseDtos = attendanceService.getAttendanceByDate(date);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    // Get today's attendance
    @GetMapping("/today")
    public ResponseEntity<List<TeachersAttendanceResponseDto>> getTodaysAttendance() {
        List<TeachersAttendanceResponseDto> responseDtos = attendanceService.getTodaysAttendance();
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    // Get attendance statistics for a date
    @GetMapping("/statistics/{date}")
    public ResponseEntity<Object> getAttendanceStatistics(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Object statistics = attendanceService.getAttendanceStatistics(date);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    // Get attendance by teacher and date range
    @GetMapping("/teacher/{teacherId}/range")
    public ResponseEntity<List<TeachersAttendanceResponseDto>> getAttendanceByTeacherAndDateRange(
            @PathVariable Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<TeachersAttendanceResponseDto> responseDtos =
                attendanceService.getAttendanceByTeacherAndDateRange(teacherId, startDate, endDate);

        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    // Get monthly attendance summary for a teacher
    @GetMapping("/teacher/{teacherId}/monthly-summary")
    public ResponseEntity<Object> getMonthlyAttendanceSummary(
            @PathVariable Long teacherId,
            @RequestParam int year,
            @RequestParam int month) {

        Object summary = attendanceService.getMonthlyAttendanceSummary(teacherId, year, month);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}

