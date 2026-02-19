package com.sc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.dto.request.TeacherRequestDto;
import com.sc.dto.response.TeacherResponseDto;
import com.sc.entity.TeacherEntity;
import com.sc.repository.TeacherRepository;
import com.sc.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping(value = "/create-teacher", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TeacherResponseDto> createTeacher(
            @RequestPart("teacherData") String teacherDataJson,
            @RequestPart(value = "teacherPhoto", required = false) MultipartFile teacherPhoto) {

        logger.info("Received request to create teacher");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TeacherRequestDto dto = objectMapper.readValue(teacherDataJson, TeacherRequestDto.class);

            if (teacherPhoto != null && !teacherPhoto.isEmpty()) {
                dto.setTeacherPhoto(teacherPhoto);
            }

            validateTeacherRequest(dto);

            TeacherResponseDto response = teacherService.createTeacher(dto);
            logger.info("Teacher created successfully with ID: {}, Name: {}", response.getId(), response.getTeacherName());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for teacher creation: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Error creating teacher: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> getTeacher(@PathVariable Long id) {
        logger.info("Fetching teacher with ID: {}", id);

        try {
            TeacherResponseDto response = teacherService.getTeacherById(id);
            logger.info("Teacher retrieved successfully: {}", response.getTeacherName());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Teacher not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error retrieving teacher with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<TeacherResponseDto> getTeacherByEmployeeId(@PathVariable String employeeId) {
        logger.info("Fetching teacher with Employee ID: {}", employeeId);

        try {
            String decodedEmployeeId = java.net.URLDecoder.decode(employeeId, java.nio.charset.StandardCharsets.UTF_8);

            TeacherResponseDto response = teacherService.getTeacherByEmployeeId(decodedEmployeeId);
            logger.info("Teacher found with Employee ID: {}", decodedEmployeeId);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Teacher not found with Employee ID: {}", employeeId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching teacher with Employee ID '{}': {}", employeeId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<TeacherResponseDto>> getTeachersByDepartment(@PathVariable String department) {
        logger.info("Fetching teachers by department: {}", department);

        try {
            String decodedDepartment = java.net.URLDecoder.decode(department, java.nio.charset.StandardCharsets.UTF_8);

            if (decodedDepartment == null || decodedDepartment.trim().isEmpty()) {
                logger.warn("Teacher department is null or empty after decoding");
                return ResponseEntity.badRequest().build();
            }

            List<TeacherResponseDto> teachers = teacherService.getTeachersByDepartment(decodedDepartment);
            logger.info("Found {} teachers in department '{}'", teachers.size(), decodedDepartment);
            return ResponseEntity.ok(teachers);

        } catch (Exception e) {
            logger.error("Error processing department parameter: {}", department, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TeacherResponseDto>> getTeachersByStatus(@PathVariable String status) {
        logger.info("Fetching teachers by status: {}", status);

        try {
            String decodedStatus = java.net.URLDecoder.decode(status, java.nio.charset.StandardCharsets.UTF_8);

            List<TeacherResponseDto> teachers = teacherService.getTeachersByStatus(decodedStatus);
            logger.info("Found {} teachers with status '{}'", teachers.size(), decodedStatus);
            return ResponseEntity.ok(teachers);

        } catch (Exception e) {
            logger.error("Error fetching teachers by status '{}': {}", status, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<TeacherResponseDto>> getAllTeachers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        logger.info("Fetching all teachers with pagination - page: {}, size: {}", page, size);

        try {
            Pageable pageable = PageRequest.of(page, size, getSort(sort));
            Page<TeacherResponseDto> teachers = teacherService.getAllTeachers(pageable);

            logger.info("Retrieved {} teachers (total: {})",
                    teachers.getNumberOfElements(), teachers.getTotalElements());
            return ResponseEntity.ok(teachers);

        } catch (Exception e) {
            logger.error("Error retrieving teachers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-all-active-teachers")
    public ResponseEntity<Page<TeacherResponseDto>> getAllActiveTeachers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort,
            @RequestParam(defaultValue = "100") long delayMillis) {

        try {
            // Add delay to simulate controlled response rate
            Thread.sleep(delayMillis);

            Pageable pageable = PageRequest.of(page, size, getSort(sort));
            Page<TeacherResponseDto> teacherPage = teacherService.getAllActiveTeachers(pageable);

            return ResponseEntity.ok(teacherPage); // Fixed this line
        } catch (Exception e) {
            logger.error("Error getting active teachers: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Fixed this line
        }
    }

    private Sort getSort(String[] sort) {
        if (sort.length >= 2) {
            return Sort.by(new Sort.Order(
                    sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                    sort[0]
            ));
        }
        return Sort.unsorted();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TeacherResponseDto> updateTeacher(
            @PathVariable Long id,
            @RequestPart("teacherData") String teacherDataJson,
            @RequestPart(value = "teacherPhoto", required = false) MultipartFile teacherPhoto) {

        logger.info("Fully updating teacher with ID: {}", id);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TeacherRequestDto dto = objectMapper.readValue(teacherDataJson, TeacherRequestDto.class);

            if (teacherPhoto != null && !teacherPhoto.isEmpty()) {
                dto.setTeacherPhoto(teacherPhoto);
            }

            validateTeacherRequest(dto);

            TeacherResponseDto updatedTeacher = teacherService.updateTeacher(id, dto);
            logger.info("Teacher fully updated successfully: {}", updatedTeacher.getTeacherName());

            return ResponseEntity.ok(updatedTeacher);

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input for teacher update: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error updating teacher with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping(value = "/update-teacher/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TeacherResponseDto> patchTeacher(
            @PathVariable Long id,
            @RequestPart(value = "teacherData", required = false) String teacherDataJson,
            @RequestPart(value = "teacherPhoto", required = false) MultipartFile teacherPhoto) {

        logger.info("Partially updating teacher with ID: {}", id);

        try {
            TeacherRequestDto dto = new TeacherRequestDto();

            if (teacherDataJson != null && !teacherDataJson.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                dto = objectMapper.readValue(teacherDataJson, TeacherRequestDto.class);
            }

            if (teacherPhoto != null && !teacherPhoto.isEmpty()) {
                dto.setTeacherPhoto(teacherPhoto);
            }

            TeacherResponseDto updatedTeacher = teacherService.patchTeacher(id, dto);
            logger.info("Teacher patched successfully: {}", updatedTeacher.getTeacherName());

            return ResponseEntity.ok(updatedTeacher);

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input for teacher patch: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error patching teacher with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        logger.info("Deleting teacher with ID: {}", id);

        try {
            teacherService.deleteTeacher(id);
            logger.info("Teacher deleted successfully with ID: {}", id);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            logger.warn("Teacher not found for deletion with ID: {}", id);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error deleting teacher with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getTeacherPhoto(@PathVariable Long id) {
        logger.info("Fetching photo for teacher ID: {}", id);

        try {
            Optional<TeacherEntity> teacher = teacherRepository.findById(id);
            if (teacher.isPresent() && teacher.get().getTeacherPhoto() != null) {
                logger.debug("Teacher photo found for ID: {}", id);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(teacher.get().getTeacherPhoto());
            }
            logger.warn("Teacher photo not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching teacher photo for ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> checkTeacherExists(@PathVariable Long id) {
        logger.info("Checking if teacher exists with ID: {}", id);

        try {
            boolean exists = teacherRepository.existsById(id);
            logger.debug("Teacher existence check for ID {}: {}", id, exists);
            return ResponseEntity.ok(exists);

        } catch (Exception e) {
            logger.error("Error checking teacher existence for ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/exists/employee-id/{employeeId}")
    public ResponseEntity<Boolean> checkTeacherExistsByEmployeeId(@PathVariable String employeeId) {
        logger.info("Checking if teacher exists with Employee ID: {}", employeeId);

        try {
            String decodedEmployeeId = java.net.URLDecoder.decode(employeeId, java.nio.charset.StandardCharsets.UTF_8);

            boolean exists = teacherService.existsByEmployeeId(decodedEmployeeId);
            logger.debug("Teacher existence check for Employee ID {}: {}", decodedEmployeeId, exists);
            return ResponseEntity.ok(exists);

        } catch (Exception e) {
            logger.error("Error checking teacher existence for Employee ID {}: {}", employeeId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTeacherCount() {
        logger.info("Getting total teacher count");

        try {
            long count = teacherRepository.count();
            logger.info("Total teachers count: {}", count);
            return ResponseEntity.ok(count);

        } catch (Exception e) {
            logger.error("Error getting teacher count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<TeacherResponseDto>> searchTeachers(@PathVariable String keyword) {
        logger.info("Searching teachers with keyword: {}", keyword);

        try {
            String decodedKeyword = java.net.URLDecoder.decode(keyword, java.nio.charset.StandardCharsets.UTF_8);

            List<TeacherResponseDto> result = teacherService.searchTeachers(decodedKeyword);
            logger.info("Found {} teachers matching keyword: {}", result.size(), decodedKeyword);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error searching teachers by keyword '{}': {}", keyword, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/designation/{designation}")
    public ResponseEntity<List<TeacherResponseDto>> getTeachersByDesignation(@PathVariable String designation) {
        logger.info("Fetching teachers by designation: {}", designation);

        try {
            String decodedDesignation = java.net.URLDecoder.decode(designation, java.nio.charset.StandardCharsets.UTF_8);

            List<TeacherEntity> teachers = teacherRepository.findByDesignation(decodedDesignation);
            List<TeacherResponseDto> response = teachers.stream()
                    .map(teacherService::toDto)
                    .collect(java.util.stream.Collectors.toList());

            logger.info("Found {} teachers with designation '{}'", response.size(), decodedDesignation);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching teachers by designation '{}': {}", designation, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<TeacherResponseDto>> getTeachersActive() {
        logger.info("Fetching active teachers");

        try {
            List<TeacherResponseDto> response = teacherService.getAllActiveTeachers();
            logger.info("Found {} active teachers", response.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching active teachers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/experience-range")
    public ResponseEntity<List<TeacherResponseDto>> getTeachersByExperienceRange(
            @RequestParam Integer minExperience,
            @RequestParam Integer maxExperience) {

        logger.info("Fetching teachers in experience range {} - {} years", minExperience, maxExperience);

        try {
            if (minExperience == null || maxExperience == null || minExperience < 0 || maxExperience < 0 || minExperience > maxExperience) {
                logger.warn("Invalid experience range parameters: min={}, max={}", minExperience, maxExperience);
                return ResponseEntity.badRequest().build();
            }

            List<TeacherEntity> teachers = teacherRepository.findByTotalExperienceBetween(minExperience, maxExperience);
            List<TeacherResponseDto> response = teachers.stream()
                    .map(teacherService::toDto)
                    .collect(java.util.stream.Collectors.toList());

            logger.info("Found {} teachers in experience range {} - {} years", response.size(), minExperience, maxExperience);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching teachers by experience range: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/salary-range")
    public ResponseEntity<List<TeacherResponseDto>> getTeachersBySalaryRange(
            @RequestParam Double minSalary,
            @RequestParam Double maxSalary) {

        logger.info("Fetching teachers in salary range {} - {}", minSalary, maxSalary);

        try {
            if (minSalary == null || maxSalary == null || minSalary < 0 || maxSalary < 0 || minSalary > maxSalary) {
                logger.warn("Invalid salary range parameters: min={}, max={}", minSalary, maxSalary);
                return ResponseEntity.badRequest().build();
            }

            List<TeacherEntity> teachers = teacherRepository.findByGrossSalaryBetween(minSalary, maxSalary);
            List<TeacherResponseDto> response = teachers.stream()
                    .map(teacherService::toDto)
                    .collect(java.util.stream.Collectors.toList());

            logger.info("Found {} teachers in salary range {} - {}", response.size(), minSalary, maxSalary);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching teachers by salary range: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/high-experience/{minExperience}")
    public ResponseEntity<List<TeacherResponseDto>> getTeachersByMinExperience(@PathVariable Integer minExperience) {
        logger.info("Fetching teachers with experience >= {} years", minExperience);

        try {
            if (minExperience == null || minExperience < 0) {
                logger.warn("Invalid experience parameter: {}", minExperience);
                return ResponseEntity.badRequest().build();
            }

            List<TeacherEntity> teachers = teacherRepository.findByTotalExperienceGreaterThanEqual(minExperience);
            List<TeacherResponseDto> response = teachers.stream()
                    .map(teacherService::toDto)
                    .collect(java.util.stream.Collectors.toList());

            logger.info("Found {} teachers with experience >= {} years", response.size(), minExperience);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching teachers by experience: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/high-salary/{minSalary}")
    public ResponseEntity<List<TeacherResponseDto>> getTeachersByMinSalary(@PathVariable Double minSalary) {
        logger.info("Fetching teachers with salary >= {}", minSalary);

        try {
            if (minSalary == null || minSalary < 0) {
                logger.warn("Invalid salary parameter: {}", minSalary);
                return ResponseEntity.badRequest().build();
            }

            List<TeacherEntity> teachers = teacherRepository.findByGrossSalaryGreaterThanEqual(minSalary);
            List<TeacherResponseDto> response = teachers.stream()
                    .map(teacherService::toDto)
                    .collect(java.util.stream.Collectors.toList());

            logger.info("Found {} teachers with salary >= {}", response.size(), minSalary);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching teachers by salary: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void validateTeacherRequest(TeacherRequestDto dto) {
        if (dto.getEmployeeId() == null || dto.getEmployeeId().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (dto.getTeacherName() == null || dto.getTeacherName().trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher name is required");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (dto.getContactNumber() == null || dto.getContactNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Contact number is required");
        }
        if (dto.getAadharNumber() == null || dto.getAadharNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Aadhar number is required");
        }
        if (dto.getPanNumber() == null || dto.getPanNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("PAN number is required");
        }
        if (dto.getJoiningDate() == null) {
            throw new IllegalArgumentException("Joining date is required");
        }
        if (dto.getDesignation() == null || dto.getDesignation().trim().isEmpty()) {
            throw new IllegalArgumentException("Designation is required");
        }
        if (dto.getDepartment() == null || dto.getDepartment().trim().isEmpty()) {
            throw new IllegalArgumentException("Department is required");
        }
        if (dto.getPrimarySubject() == null || dto.getPrimarySubject().trim().isEmpty()) {
            throw new IllegalArgumentException("Primary subject is required");
        }
        if (dto.getBasicSalary() == null || dto.getBasicSalary() < 0) {
            throw new IllegalArgumentException("Valid basic salary is required");
        }
    }

    // ───────────────────────────────────────────────
    // NEW ENDPOINT: Get subjects taught by teacher
    // ───────────────────────────────────────────────
    @GetMapping("/get-subjects-by-id/{id}")
    public ResponseEntity<List<String>> getSubjectsByTeacher(@PathVariable Long id) {
        try {
            List<String> subjects = teacherService.getSubjectsByTeacher(id);
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            logger.error("Error getting subjects for teacher ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}