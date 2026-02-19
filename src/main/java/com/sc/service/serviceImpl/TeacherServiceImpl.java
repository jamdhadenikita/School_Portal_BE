package com.sc.service.serviceImpl;

import com.sc.dto.request.TeacherRequestDto;
import com.sc.dto.response.TeacherResponseDto;
import com.sc.entity.TeacherEntity;
import com.sc.repository.TeacherRepository;
import com.sc.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Autowired
    private TeacherRepository repo;

    @Override
    @Transactional
    public TeacherResponseDto createTeacher(TeacherRequestDto dto) {
        logger.info("Creating new teacher with Employee ID: {}", dto.getEmployeeId());

        try {
            if (repo.existsByEmployeeId(dto.getEmployeeId())) {
                logger.error("Employee ID already exists: {}", dto.getEmployeeId());
                throw new IllegalArgumentException("Employee ID already exists: " + dto.getEmployeeId());
            }

            if (repo.existsByEmail(dto.getEmail())) {
                logger.error("Email already exists: {}", dto.getEmail());
                throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
            }

            if (repo.existsByAadharNumber(dto.getAadharNumber())) {
                logger.error("Aadhar number already exists: {}", dto.getAadharNumber());
                throw new IllegalArgumentException("Aadhar number already exists: " + dto.getAadharNumber());
            }

            if (repo.existsByPanNumber(dto.getPanNumber())) {
                logger.error("PAN number already exists: {}", dto.getPanNumber());
                throw new IllegalArgumentException("PAN number already exists: " + dto.getPanNumber());
            }

            TeacherEntity entity = toEntity(dto, new TeacherEntity());

            // Calculate gross salary
            entity.calculateGrossSalary();

            entity = repo.save(entity);

            TeacherResponseDto response = toDto(entity);
            logger.info("Teacher created successfully with ID: {}, Name: {}", response.getId(), response.getTeacherName());

            return response;

        } catch (Exception e) {
            logger.error("Error creating teacher with Employee ID {}: {}", dto.getEmployeeId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TeacherResponseDto updateTeacher(Long id, TeacherRequestDto dto) {
        logger.info("Fully updating teacher with ID: {}", id);

        try {
            TeacherEntity entity = repo.findById(id).orElseThrow(() -> {
                logger.warn("Teacher not found with ID: {}", id);
                return new IllegalArgumentException("Teacher not found with ID: " + id);
            });

            if (!entity.getEmployeeId().equals(dto.getEmployeeId())) {
                if (repo.existsByEmployeeId(dto.getEmployeeId())) {
                    logger.error("Employee ID already exists: {}", dto.getEmployeeId());
                    throw new IllegalArgumentException("Employee ID already exists: " + dto.getEmployeeId());
                }
            }

            if (!entity.getEmail().equals(dto.getEmail())) {
                if (repo.existsByEmail(dto.getEmail())) {
                    logger.error("Email already exists: {}", dto.getEmail());
                    throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
                }
            }

            if (!entity.getAadharNumber().equals(dto.getAadharNumber())) {
                if (repo.existsByAadharNumber(dto.getAadharNumber())) {
                    logger.error("Aadhar number already exists: {}", dto.getAadharNumber());
                    throw new IllegalArgumentException("Aadhar number already exists: " + dto.getAadharNumber());
                }
            }

            if (!entity.getPanNumber().equals(dto.getPanNumber())) {
                if (repo.existsByPanNumber(dto.getPanNumber())) {
                    logger.error("PAN number already exists: {}", dto.getPanNumber());
                    throw new IllegalArgumentException("PAN number already exists: " + dto.getPanNumber());
                }
            }

            entity = toEntity(dto, entity);

            // Recalculate gross salary
            entity.calculateGrossSalary();

            entity = repo.save(entity);

            TeacherResponseDto response = toDto(entity);
            logger.info("Teacher fully updated successfully with ID: {}, Name: {}", response.getId(), response.getTeacherName());

            return response;

        } catch (Exception e) {
            logger.error("Error updating teacher with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TeacherResponseDto patchTeacher(Long id, TeacherRequestDto dto) {
        logger.info("Partially updating teacher with ID: {}", id);

        try {
            TeacherEntity entity = repo.findById(id).orElseThrow(() -> {
                logger.warn("Teacher not found with ID: {}", id);
                return new IllegalArgumentException("Teacher not found with ID: " + id);
            });

            if (StringUtils.hasText(dto.getEmployeeId()) && !dto.getEmployeeId().equals(entity.getEmployeeId())) {
                if (repo.existsByEmployeeId(dto.getEmployeeId())) {
                    logger.error("Employee ID already exists: {}", dto.getEmployeeId());
                    throw new IllegalArgumentException("Employee ID already exists: " + dto.getEmployeeId());
                }
            }

            if (StringUtils.hasText(dto.getEmail()) && !dto.getEmail().equals(entity.getEmail())) {
                if (repo.existsByEmail(dto.getEmail())) {
                    logger.error("Email already exists: {}", dto.getEmail());
                    throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
                }
            }

            patchEntity(dto, entity);

            // Recalculate gross salary if salary fields were updated
            if (dto.getBasicSalary() != null || dto.getHra() != null ||
                    dto.getDa() != null || dto.getTa() != null ||
                    dto.getAdditionalAllowances() != null) {
                entity.calculateGrossSalary();
            }

            entity = repo.save(entity);

            TeacherResponseDto response = toDto(entity);
            logger.info("Teacher patched successfully with ID: {}, Name: {}", response.getId(), response.getTeacherName());

            return response;

        } catch (Exception e) {
            logger.error("Error patching teacher with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TeacherResponseDto getTeacherById(Long id) {
        logger.debug("Fetching teacher by ID: {}", id);

        try {
            TeacherEntity entity = repo.findById(id).orElseThrow(() -> {
                logger.warn("Teacher not found with ID: {}", id);
                return new IllegalArgumentException("Teacher not found with ID: " + id);
            });

            TeacherResponseDto response = toDto(entity);
            logger.debug("Teacher fetched successfully with ID: {}, Name: {}", response.getId(), response.getTeacherName());

            return response;

        } catch (Exception e) {
            logger.error("Error fetching teacher with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TeacherResponseDto getTeacherByEmployeeId(String employeeId) {
        logger.info("Fetching teacher by Employee ID: {}", employeeId);

        try {
            TeacherEntity entity = repo.findByEmployeeId(employeeId).orElseThrow(() -> {
                logger.warn("Teacher not found with Employee ID: {}", employeeId);
                return new IllegalArgumentException("Teacher not found with Employee ID: " + employeeId);
            });

            TeacherResponseDto response = toDto(entity);
            logger.info("Teacher fetched successfully with Employee ID: {}, Name: {}", employeeId, response.getTeacherName());

            return response;

        } catch (Exception e) {
            logger.error("Error fetching teacher with Employee ID {}: {}", employeeId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeacherResponseDto> getAllTeachers(Pageable pageable) {
        logger.debug("Fetching all teachers with pagination");

        try {
            Page<TeacherEntity> entities = repo.findAllWithPagination(pageable);
            logger.debug("Fetched {} teachers (page {})",
                    entities.getNumberOfElements(), pageable.getPageNumber());
            return entities.map(this::toDto);

        } catch (Exception e) {
            logger.error("Error fetching all teachers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve all teachers: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponseDto> getAllTeachersList() {
        try {
            List<TeacherEntity> entities = repo.findAll();
            return entities.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all teachers: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeacherResponseDto> getAllActiveTeachers(Pageable pageable) {
        try {
            Page<TeacherEntity> entities = repo.findAllActiveAndApproved(pageable);
            return entities.map(this::toDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve active teachers: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponseDto> getAllActiveTeachers() {
        try {
            List<TeacherEntity> entities = repo.findAllActive();
            return entities.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve active teachers: " + e.getMessage(), e);
        }
    }

// In TeacherServiceImpl.java, find this method:
    @Override
    public List<TeacherResponseDto> getTeachersByDepartment(String department) {
        logger.info("Fetching teachers by department: {}", department);

        try {
            // Change this line from repo.findByDepartmentAndActive(department) to:
            List<TeacherEntity> teachers = repo.findByDepartment(department)
                    .stream()
                    .filter(teacher -> !teacher.isDeleted() && teacher.getApproved())
                    .collect(Collectors.toList());

            List<TeacherResponseDto> result = teachers.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            logger.info("Found {} teachers in department '{}'", result.size(), department);
            return result;

        } catch (Exception e) {
            logger.error("Error fetching teachers by department '{}': {}", department, e.getMessage(), e);
            throw e;
        }
    }
    @Override
    public List<TeacherResponseDto> getTeachersByStatus(String status) {
        logger.info("Fetching teachers by status: {}", status);

        try {
            List<TeacherResponseDto> teachers = repo.findByStatusAndActive(status).stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            logger.info("Found {} teachers with status '{}'", teachers.size(), status);
            return teachers;

        } catch (Exception e) {
            logger.error("Error fetching teachers by status '{}': {}", status, e.getMessage(), e);
            throw e;
        }
    }

    // In TeacherServiceImpl.java, find this method:
    @Override
    public List<TeacherResponseDto> searchTeachers(String keyword) {
        logger.info("Searching teachers with keyword: {}", keyword);

        try {
            List<TeacherEntity> nameResults = repo.findByTeacherNameContainingIgnoreCase(keyword);
            List<TeacherEntity> employeeIdResults = repo.findByEmployeeIdContainingIgnoreCase(keyword);
            List<TeacherEntity> emailResults = repo.findByEmailContainingIgnoreCase(keyword);

            // Combine results and remove duplicates
            java.util.Set<TeacherEntity> combinedResults = new java.util.HashSet<>();
            combinedResults.addAll(nameResults);
            combinedResults.addAll(employeeIdResults);
            combinedResults.addAll(emailResults);

            // Filter for active and approved teachers
            List<TeacherResponseDto> result = combinedResults.stream()
                    .filter(teacher -> !teacher.isDeleted() && teacher.getApproved())
                    .map(this::toDto)
                    .collect(Collectors.toList());

            logger.info("Found {} teachers matching keyword: {}", result.size(), keyword);
            return result;

        } catch (Exception e) {
            logger.error("Error searching teachers by keyword '{}': {}", keyword, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean existsByEmployeeId(String employeeId) {
        logger.debug("Checking if teacher exists with Employee ID: {}", employeeId);

        try {
            boolean exists = repo.existsByEmployeeId(employeeId);
            logger.debug("Teacher existence check for Employee ID {}: {}", employeeId, exists);
            return exists;

        } catch (Exception e) {
            logger.error("Error checking teacher existence for Employee ID {}: {}", employeeId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteTeacher(Long id) {
        logger.info("Deleting teacher with ID: {}", id);

        try {
            if (!repo.existsById(id)) {
                logger.warn("Teacher not found for deletion with ID: {}", id);
                throw new IllegalArgumentException("Teacher not found with ID: " + id);
            }

            repo.deleteById(id);
            logger.info("Teacher deleted successfully with ID: {}", id);

        } catch (Exception e) {
            logger.error("Error deleting teacher with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TeacherResponseDto toDto(TeacherEntity e) {
        logger.debug("Converting Entity to DTO for teacher ID: {}", e.getId());

        TeacherResponseDto d = new TeacherResponseDto();
        d.setId(e.getId());
        d.setEmployeeId(e.getEmployeeId());
        d.setTeacherName(e.getTeacherName());
        d.setEmail(e.getEmail());
        d.setContactNumber(e.getContactNumber());
        d.setDob(e.getDob());
        d.setGender(e.getGender());
        d.setBloodGroup(e.getBloodGroup());
        d.setAddressLines(e.getAddressLines());
        d.setCity(e.getCity());
        d.setState(e.getState());
        d.setPincode(e.getPincode());
        d.setEmergencyContactName(e.getEmergencyContactName());
        d.setEmergencyContactNumber(e.getEmergencyContactNumber());
        d.setAadharNumber(e.getAadharNumber());
        d.setPanNumber(e.getPanNumber());
        d.setMedicalInfo(e.getMedicalInfo());
        d.setJoiningDate(e.getJoiningDate());
        d.setDesignation(e.getDesignation());
        d.setTotalExperience(e.getTotalExperience());
        d.setDepartment(e.getDepartment());
        d.setEmploymentType(e.getEmploymentType());

        // Convert previous experience
        if (e.getPreviousExperience() != null) {
            d.setPreviousExperience(e.getPreviousExperience().stream().map(exp -> {
                TeacherResponseDto.ExperienceResponse er = new TeacherResponseDto.ExperienceResponse();
                er.setPrevSchool(exp.getPrevSchool());
                er.setPrevPosition(exp.getPrevPosition());
                er.setPrevDuration(exp.getPrevDuration());
                return er;
            }).collect(Collectors.toList()));
        }

        // Convert qualifications
        if (e.getQualifications() != null) {
            d.setQualifications(e.getQualifications().stream().map(qual -> {
                TeacherResponseDto.QualificationResponse qr = new TeacherResponseDto.QualificationResponse();
                qr.setDegree(qual.getDegree());
                qr.setSpecialization(qual.getSpecialization());
                qr.setUniversity(qual.getUniversity());
                qr.setCompletionYear(qual.getCompletionYear());
                return qr;
            }).collect(Collectors.toList()));
        }

        d.setPrimarySubject(e.getPrimarySubject());
        d.setAdditionalSubjects(e.getAdditionalSubjects());
        d.setClasses(e.getClasses());
        d.setBasicSalary(e.getBasicSalary());
        d.setHra(e.getHra());
        d.setDa(e.getDa());
        d.setTa(e.getTa());

        // Convert additional allowances
        if (e.getAdditionalAllowances() != null) {
            d.setAdditionalAllowances(e.getAdditionalAllowances().stream().map(allow -> {
                TeacherResponseDto.AllowanceResponse ar = new TeacherResponseDto.AllowanceResponse();
                ar.setName(allow.getName());
                ar.setAmount(allow.getAmount());
                return ar;
            }).collect(Collectors.toList()));
        }

        d.setGrossSalary(e.getGrossSalary());
        d.setBankName(e.getBankName());
        d.setAccountNumber(e.getAccountNumber());
        d.setIfscCode(e.getIfscCode());
        d.setBranchName(e.getBranchName());
        d.setStatus(e.getStatus());
        d.setCreatedAt(e.getCreatedAt());
        d.setLastUpdated(e.getLastUpdated());
        d.setApproved(e.isApproved());
        d.setDeleted(e.isDeleted());

        // Set photo URL
        if (e.getTeacherPhoto() != null) {
            d.setTeacherPhotoUrl("/api/teachers/" + e.getId() + "/photo");
        }

        // Calculate additional stats
        d.setAssignedClassCount(e.getClasses() != null ? e.getClasses().size() : 0);
        d.setSubjectCount((e.getPrimarySubject() != null ? 1 : 0) +
                (e.getAdditionalSubjects() != null ? e.getAdditionalSubjects().size() : 0));

        logger.debug("DTO conversion completed for teacher ID: {}", e.getId());
        return d;
    }

    @Override
    public List<String> getSubjectsByTeacher(Long teacherId) {
        TeacherEntity teacher = repo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        java.util.List<String> subjects = new java.util.ArrayList<>();

        if (teacher.getPrimarySubject() != null && !teacher.getPrimarySubject().isEmpty()) {
            subjects.add(teacher.getPrimarySubject());
        }

        if (teacher.getAdditionalSubjects() != null) {
            subjects.addAll(teacher.getAdditionalSubjects());
        }

        return subjects;
    }

    private TeacherEntity toEntity(TeacherRequestDto d, TeacherEntity e) {
        logger.debug("Converting DTO to Entity for teacher");

        e.setEmployeeId(d.getEmployeeId());
        e.setTeacherName(d.getTeacherName());
        e.setEmail(d.getEmail());
        e.setContactNumber(d.getContactNumber());
        e.setDob(d.getDob());
        e.setGender(d.getGender());
        e.setBloodGroup(d.getBloodGroup());

        // Address lines
        e.setAddressLines(d.getAddressLines());
        e.setCity(d.getCity());
        e.setState(d.getState());
        e.setPincode(d.getPincode());

        // Contact and identification
        e.setEmergencyContactName(d.getEmergencyContactName());
        e.setEmergencyContactNumber(d.getEmergencyContactNumber());
        e.setAadharNumber(d.getAadharNumber());
        e.setPanNumber(d.getPanNumber());
        e.setMedicalInfo(d.getMedicalInfo());

        // Professional details
        e.setJoiningDate(d.getJoiningDate());
        e.setDesignation(d.getDesignation());
        e.setTotalExperience(d.getTotalExperience());
        e.setDepartment(d.getDepartment());
        e.setEmploymentType(d.getEmploymentType());

        // Previous experience
        if (d.getPreviousExperience() != null) {
            e.getPreviousExperience().clear();
            for (var exp : d.getPreviousExperience()) {
                TeacherEntity.PreviousExperience pe = new TeacherEntity.PreviousExperience();
                pe.setPrevSchool(exp.getPrevSchool());
                pe.setPrevPosition(exp.getPrevPosition());
                pe.setPrevDuration(exp.getPrevDuration());
                e.getPreviousExperience().add(pe);
            }
        }

        // Qualifications
        if (d.getQualifications() != null) {
            e.getQualifications().clear();
            for (var qual : d.getQualifications()) {
                TeacherEntity.Qualification q = new TeacherEntity.Qualification();
                q.setDegree(qual.getDegree());
                q.setSpecialization(qual.getSpecialization());
                q.setUniversity(qual.getUniversity());
                q.setCompletionYear(qual.getCompletionYear());
                e.getQualifications().add(q);
            }
        }

        // Subjects and classes
        e.setPrimarySubject(d.getPrimarySubject());
        e.setAdditionalSubjects(d.getAdditionalSubjects());
        e.setClasses(d.getClasses());

        // Salary details
        e.setBasicSalary(d.getBasicSalary());
        e.setHra(d.getHra() != null ? d.getHra() : 0.0);
        e.setDa(d.getDa() != null ? d.getDa() : 0.0);
        e.setTa(d.getTa() != null ? d.getTa() : 0.0);

        // Additional allowances
        if (d.getAdditionalAllowances() != null) {
            e.getAdditionalAllowances().clear();
            for (var allowance : d.getAdditionalAllowances()) {
                TeacherEntity.AdditionalAllowance aa = new TeacherEntity.AdditionalAllowance();
                aa.setName(allowance.getName());
                aa.setAmount(allowance.getAmount());
                e.getAdditionalAllowances().add(aa);
            }
        }

        // Bank details
        e.setBankName(d.getBankName());
        e.setAccountNumber(d.getAccountNumber());
        e.setIfscCode(d.getIfscCode());
        e.setBranchName(d.getBranchName());

        // Status
        e.setStatus(d.getStatus() != null ? d.getStatus() : "Active");

        // Photo
        try {
            if (d.getTeacherPhoto() != null && !d.getTeacherPhoto().isEmpty()) {
                e.setTeacherPhoto(d.getTeacherPhoto().getBytes());
                logger.debug("Teacher photo set");
            }
        } catch (Exception ex) {
            logger.error("Error processing teacher photo: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error processing photo", ex);
        }

        // Approval
        if (d.getApproved() != null) {
            e.setApproved(d.getApproved());
        }

        return e;
    }

    private void patchEntity(TeacherRequestDto d, TeacherEntity e) {
        logger.debug("Patching teacher entity with partial data");

        if (StringUtils.hasText(d.getEmployeeId())) e.setEmployeeId(d.getEmployeeId());
        if (StringUtils.hasText(d.getTeacherName())) e.setTeacherName(d.getTeacherName());
        if (StringUtils.hasText(d.getEmail())) e.setEmail(d.getEmail());
        if (StringUtils.hasText(d.getContactNumber())) e.setContactNumber(d.getContactNumber());
        if (d.getDob() != null) e.setDob(d.getDob());
        if (StringUtils.hasText(d.getGender())) e.setGender(d.getGender());
        if (StringUtils.hasText(d.getBloodGroup())) e.setBloodGroup(d.getBloodGroup());

        // Address
        if (d.getAddressLines() != null && !d.getAddressLines().isEmpty()) e.setAddressLines(d.getAddressLines());
        if (StringUtils.hasText(d.getCity())) e.setCity(d.getCity());
        if (StringUtils.hasText(d.getState())) e.setState(d.getState());
        if (StringUtils.hasText(d.getPincode())) e.setPincode(d.getPincode());

        // Contact and identification
        if (StringUtils.hasText(d.getEmergencyContactName())) e.setEmergencyContactName(d.getEmergencyContactName());
        if (StringUtils.hasText(d.getEmergencyContactNumber())) e.setEmergencyContactNumber(d.getEmergencyContactNumber());
        if (StringUtils.hasText(d.getAadharNumber())) e.setAadharNumber(d.getAadharNumber());
        if (StringUtils.hasText(d.getPanNumber())) e.setPanNumber(d.getPanNumber());
        if (StringUtils.hasText(d.getMedicalInfo())) e.setMedicalInfo(d.getMedicalInfo());

        // Professional details
        if (d.getJoiningDate() != null) e.setJoiningDate(d.getJoiningDate());
        if (StringUtils.hasText(d.getDesignation())) e.setDesignation(d.getDesignation());
        if (d.getTotalExperience() != null) e.setTotalExperience(d.getTotalExperience());
        if (StringUtils.hasText(d.getDepartment())) e.setDepartment(d.getDepartment());
        if (StringUtils.hasText(d.getEmploymentType())) e.setEmploymentType(d.getEmploymentType());

        // Previous experience
        if (d.getPreviousExperience() != null && !d.getPreviousExperience().isEmpty()) {
            e.getPreviousExperience().clear();
            for (var exp : d.getPreviousExperience()) {
                TeacherEntity.PreviousExperience pe = new TeacherEntity.PreviousExperience();
                pe.setPrevSchool(exp.getPrevSchool());
                pe.setPrevPosition(exp.getPrevPosition());
                pe.setPrevDuration(exp.getPrevDuration());
                e.getPreviousExperience().add(pe);
            }
        }

        // Qualifications
        if (d.getQualifications() != null && !d.getQualifications().isEmpty()) {
            e.getQualifications().clear();
            for (var qual : d.getQualifications()) {
                TeacherEntity.Qualification q = new TeacherEntity.Qualification();
                q.setDegree(qual.getDegree());
                q.setSpecialization(qual.getSpecialization());
                q.setUniversity(qual.getUniversity());
                q.setCompletionYear(qual.getCompletionYear());
                e.getQualifications().add(q);
            }
        }

        // Subjects and classes
        if (StringUtils.hasText(d.getPrimarySubject())) e.setPrimarySubject(d.getPrimarySubject());
        if (d.getAdditionalSubjects() != null && !d.getAdditionalSubjects().isEmpty()) e.setAdditionalSubjects(d.getAdditionalSubjects());
        if (d.getClasses() != null && !d.getClasses().isEmpty()) e.setClasses(d.getClasses());

        // Salary details
        if (d.getBasicSalary() != null) e.setBasicSalary(d.getBasicSalary());
        if (d.getHra() != null) e.setHra(d.getHra());
        if (d.getDa() != null) e.setDa(d.getDa());
        if (d.getTa() != null) e.setTa(d.getTa());

        // Additional allowances
        if (d.getAdditionalAllowances() != null && !d.getAdditionalAllowances().isEmpty()) {
            e.getAdditionalAllowances().clear();
            for (var allowance : d.getAdditionalAllowances()) {
                TeacherEntity.AdditionalAllowance aa = new TeacherEntity.AdditionalAllowance();
                aa.setName(allowance.getName());
                aa.setAmount(allowance.getAmount());
                e.getAdditionalAllowances().add(aa);
            }
        }

        // Bank details
        if (StringUtils.hasText(d.getBankName())) e.setBankName(d.getBankName());
        if (StringUtils.hasText(d.getAccountNumber())) e.setAccountNumber(d.getAccountNumber());
        if (StringUtils.hasText(d.getIfscCode())) e.setIfscCode(d.getIfscCode());
        if (StringUtils.hasText(d.getBranchName())) e.setBranchName(d.getBranchName());

        // Status
        if (StringUtils.hasText(d.getStatus())) e.setStatus(d.getStatus());

        // Photo
        if (d.getTeacherPhoto() != null && !d.getTeacherPhoto().isEmpty()) {
            try {
                e.setTeacherPhoto(d.getTeacherPhoto().getBytes());
                logger.debug("Teacher photo updated");
            } catch (Exception ex) {
                logger.error("Error updating teacher photo: {}", ex.getMessage());
            }
        }

        // Approval
        if (d.getApproved() != null) {
            e.setApproved(d.getApproved());
        }
    }
}