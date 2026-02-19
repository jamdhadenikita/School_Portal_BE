package com.sc.service;

import com.sc.dto.request.StudentRequestDto;
import com.sc.dto.response.StudentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StudentService {

    // ============= 📝 CREATE =============
    StudentResponseDto createStudent(StudentRequestDto requestDto);

    // ============= 🔍 READ =============
    List<StudentResponseDto> getAllStudents();
    StudentResponseDto getStudentById(Long stdId);
    StudentResponseDto getStudentByStudentId(String studentId);
    StudentResponseDto getStudentByRollNumber(String rollNumber);
    List<StudentResponseDto> getStudentsByClass(String className);
    List<StudentResponseDto> getStudentsByClassAndSection(String className, String section);
    List<StudentResponseDto> getStudentsByStatus(String status);
    List<StudentResponseDto> getStudentsByAdmissionDate(Date admissionDate);
    List<StudentResponseDto> searchStudents(String name, String fatherName, String studentId, String rollNumber);

    // ============= 📊 STATISTICS =============
    Map<String, Long> getStudentCountByClass();
    Map<String, Map<String, Long>> getStudentCountByClassAndSection();
    Map<String, Object> getStudentStatistics();

    // ============= ✏️ UPDATE =============
    StudentResponseDto updateStudent(Long stdId, StudentRequestDto requestDto);
    StudentResponseDto updateStudentPartial(Long id, Map<String, Object> updates);
    StudentResponseDto updateStudentStatus(Long id, String status);
    StudentResponseDto updateStudentClassSection(Long id, String currentClass, String section);
    List<StudentResponseDto> bulkUpdateStudentStatus(List<Long> studentIds, String status);

    // ============= 🗑️ DELETE =============
    void deleteStudent(Long stdId);

    // ============= 🖼️ IMAGE UPLOAD =============
    StudentResponseDto uploadStudentImage(Long stdId, MultipartFile profileImage);
    StudentResponseDto uploadStudentDocuments(Long stdId, MultipartFile studentAadharImage,
                                              MultipartFile fatherAadharImage,
                                              MultipartFile motherAadharImage,
                                              MultipartFile birthCertificateImage,
                                              MultipartFile transferCertificateImage,
                                              MultipartFile markSheetImage);

    // ============= 🖼️ IMAGE RETRIEVAL =============
    byte[] getProfileImage(Long stdId);
    byte[] getStudentAadharImage(Long stdId);
    byte[] getFatherAadharImage(Long stdId);
    byte[] getMotherAadharImage(Long stdId);
    byte[] getBirthCertificateImage(Long stdId);
    byte[] getTransferCertificateImage(Long stdId);
    byte[] getMarkSheetImage(Long stdId);

    // ============= 💰 FEES =============
    StudentResponseDto updateStudentFees(Long stdId, StudentRequestDto requestDto);
}