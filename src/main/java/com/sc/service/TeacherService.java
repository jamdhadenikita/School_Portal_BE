package com.sc.service;

import com.sc.dto.request.TeacherRequestDto;
import com.sc.dto.response.TeacherResponseDto;
import com.sc.entity.TeacherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeacherService {
    TeacherResponseDto createTeacher(TeacherRequestDto dto);
    TeacherResponseDto updateTeacher(Long id, TeacherRequestDto dto);
    TeacherResponseDto patchTeacher(Long id, TeacherRequestDto dto);
    TeacherResponseDto getTeacherById(Long id);
    TeacherResponseDto getTeacherByEmployeeId(String employeeId);

    @Transactional(readOnly = true)
    Page<TeacherResponseDto> getAllTeachers(Pageable pageable);

    @Transactional(readOnly = true)
    List<TeacherResponseDto> getAllTeachersList();

    List<TeacherResponseDto> getTeachersByDepartment(String department);
    List<TeacherResponseDto> getTeachersByStatus(String status);
    List<TeacherResponseDto> searchTeachers(String keyword);
    boolean existsByEmployeeId(String employeeId);
    void deleteTeacher(Long id);
    TeacherResponseDto toDto(TeacherEntity entity);

    /**
     * Get all active teachers (non-deleted and approved only)
     * With pagination support
     *
     * @param pageable pagination information
     * @return Page of active teachers with pagination metadata
     */
    Page<TeacherResponseDto> getAllActiveTeachers(Pageable pageable);

    /**
     * Get all active teachers (non-deleted and approved only)
     * Without pagination - returns all records
     *
     * @return List of all active teachers
     */
    List<TeacherResponseDto> getAllActiveTeachers();

    //get list of subjects taught by teacher
    List<String> getSubjectsByTeacher(Long teacherId);
}