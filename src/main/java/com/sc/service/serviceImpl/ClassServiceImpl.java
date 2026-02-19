package com.sc.service.serviceImpl;

import com.sc.CustomExceptions.ResourceNotFoundException;
import com.sc.dto.request.ClassCreateRequestDTO;
import com.sc.dto.response.ClassResponseDTO;
import com.sc.dto.request.TeacherSubjectAssignmentDTO;
import com.sc.entity.ClassEntity;
import com.sc.repository.ClassRepository;
import com.sc.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Override
    public ClassResponseDTO createClass(ClassCreateRequestDTO request) {
        // Validate class code uniqueness
        if (isClassCodeExists(request.getClassCode())) {
            throw new IllegalArgumentException("Class code already exists: " + request.getClassCode());
        }

        // Create new class entity
        ClassEntity classEntity = new ClassEntity();
        mapRequestToEntity(request, classEntity);

        // Save to database
        ClassEntity savedEntity = classRepository.save(classEntity);

        // Convert to response DTO
        return mapEntityToResponse(savedEntity);
    }

    @Override
    public ClassResponseDTO createClassBasic(ClassCreateRequestDTO request) {
        // Basic validation
        if (request.getClassName() == null || request.getClassName().isEmpty()) {
            throw new IllegalArgumentException("Class name is required");
        }
        if (request.getClassCode() == null || request.getClassCode().isEmpty()) {
            throw new IllegalArgumentException("Class code is required");
        }

        // Check if class code exists
        if (isClassCodeExists(request.getClassCode())) {
            throw new IllegalArgumentException("Class code already exists: " + request.getClassCode());
        }

        // Create basic class entity
        ClassEntity classEntity = new ClassEntity(
                request.getClassName(),
                request.getClassCode(),
                request.getAcademicYear() != null ? request.getAcademicYear() : "2024-2025",
                request.getSection() != null ? request.getSection() : "A"
        );

        // Set default values
        classEntity.setMaxStudents(request.getMaxStudents() != null ? request.getMaxStudents() : 30);
        classEntity.setCurrentStudents(request.getCurrentStudents() != null ? request.getCurrentStudents() : 0);
        classEntity.setStatus("ACTIVE");

        // Save to database
        ClassEntity savedEntity = classRepository.save(classEntity);

        // Convert to response DTO
        return mapEntityToResponse(savedEntity);
    }

    @Override
    public ClassResponseDTO getClassById(Long classId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

        if (Boolean.TRUE.equals(classEntity.getIsDeleted())) {
            throw new ResourceNotFoundException("Class has been deleted");
        }

        return mapEntityToResponse(classEntity);
    }

    @Override
    public ClassResponseDTO getClassByCode(String classCode) {
        ClassEntity classEntity = classRepository.findByClassCodeAndNotDeleted(classCode)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with code: " + classCode));

        return mapEntityToResponse(classEntity);
    }

    @Override
    public List<ClassResponseDTO> getAllClasses() {
        List<ClassEntity> classes = classRepository.findAllActiveClasses();
        return classes.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassResponseDTO> getClassesByAcademicYear(String academicYear) {
        List<ClassEntity> classes = classRepository.findByAcademicYear(academicYear);
        return classes.stream()
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassResponseDTO> getClassesByClassName(String className) {
        List<ClassEntity> classes = classRepository.findByClassName(className);
        return classes.stream()
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassResponseDTO> getClassesByTeacher(Long teacherId) {
        List<ClassEntity> classesAsClassTeacher = classRepository.findByClassTeacherId(teacherId);
        List<ClassEntity> classesAsAssistant = classRepository.findByAssistantTeacherId(teacherId);

        List<ClassEntity> allClasses = new java.util.ArrayList<>();
        allClasses.addAll(classesAsClassTeacher);
        allClasses.addAll(classesAsAssistant);

        return allClasses.stream()
                .distinct()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClassResponseDTO updateClass(Long classId, ClassCreateRequestDTO request) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

        if (Boolean.TRUE.equals(classEntity.getIsDeleted())) {
            throw new ResourceNotFoundException("Class has been deleted");
        }

        // Update fields
        mapRequestToEntity(request, classEntity);

        // Save updated entity
        ClassEntity updatedEntity = classRepository.save(classEntity);

        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public ClassResponseDTO updateClassTeacher(Long classId, Long teacherId, String subject) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

        if (Boolean.TRUE.equals(classEntity.getIsDeleted())) {
            throw new ResourceNotFoundException("Class has been deleted");
        }

        classEntity.setClassTeacherId(teacherId);
        classEntity.setClassTeacherSubject(subject);

        ClassEntity updatedEntity = classRepository.save(classEntity);
        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public ClassResponseDTO updateAssistantTeacher(Long classId, Long teacherId, String subject) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

        if (Boolean.TRUE.equals(classEntity.getIsDeleted())) {
            throw new ResourceNotFoundException("Class has been deleted");
        }

        classEntity.setAssistantTeacherId(teacherId);
        classEntity.setAssistantTeacherSubject(subject);

        ClassEntity updatedEntity = classRepository.save(classEntity);
        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public ClassResponseDTO addOtherTeacherSubject(Long classId, TeacherSubjectAssignmentDTO assignment) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

        if (Boolean.TRUE.equals(classEntity.getIsDeleted())) {
            throw new ResourceNotFoundException("Class has been deleted");
        }

        // Get current assignments
        List<ClassEntity.TeacherSubjectAssignment> currentAssignments = classEntity.getOtherTeacherSubject();

        // Check if teacher already exists
        boolean teacherExists = false;
        for (ClassEntity.TeacherSubjectAssignment existing : currentAssignments) {
            if (existing.getTeacherId().equals(assignment.getTeacherId())) {
                // Add new subjects to existing teacher
                for (String subject : assignment.getSubjects()) {
                    if (!existing.getSubjects().contains(subject)) {
                        existing.getSubjects().add(subject);
                    }
                }
                teacherExists = true;
                break;
            }
        }

        // If teacher doesn't exist, add new assignment
        if (!teacherExists) {
            ClassEntity.TeacherSubjectAssignment newAssignment =
                    new ClassEntity.TeacherSubjectAssignment(
                            assignment.getTeacherId(),
                            assignment.getTeacherName(),
                            assignment.getSubjects()
                    );
            currentAssignments.add(newAssignment);
        }

        // Update entity
        classEntity.setOtherTeacherSubject(currentAssignments);

        ClassEntity updatedEntity = classRepository.save(classEntity);
        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public ClassResponseDTO removeOtherTeacherSubject(Long classId, String teacherId, String subject) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

        if (Boolean.TRUE.equals(classEntity.getIsDeleted())) {
            throw new ResourceNotFoundException("Class has been deleted");
        }

        List<ClassEntity.TeacherSubjectAssignment> assignments = classEntity.getOtherTeacherSubject();

        // Remove subject from teacher
        for (ClassEntity.TeacherSubjectAssignment assignment : assignments) {
            if (assignment.getTeacherId().equals(teacherId)) {
                assignment.getSubjects().remove(subject);
                // If no subjects left, remove the entire assignment
                if (assignment.getSubjects().isEmpty()) {
                    assignments.remove(assignment);
                }
                break;
            }
        }

        classEntity.setOtherTeacherSubject(assignments);

        ClassEntity updatedEntity = classRepository.save(classEntity);
        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public void deleteClass(Long classId) {
        classRepository.deleteById(classId);
    }

    @Override
    public void softDeleteClass(Long classId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

        classEntity.setIsDeleted(true);
        classRepository.save(classEntity);
    }

    @Override
    public ClassResponseDTO updateClassStatus(Long classId, String status) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

        if (Boolean.TRUE.equals(classEntity.getIsDeleted())) {
            throw new ResourceNotFoundException("Class has been deleted");
        }

        classEntity.setStatus(status);

        ClassEntity updatedEntity = classRepository.save(classEntity);
        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public boolean isClassCodeExists(String classCode) {
        return classRepository.findByClassCode(classCode).isPresent();
    }

    @Override
    public boolean isTeacherAssignedToClass(Long teacherId, Long excludeClassId) {
        List<ClassEntity> classesAsClassTeacher = classRepository.findByClassTeacherId(teacherId);
        List<ClassEntity> classesAsAssistant = classRepository.findByAssistantTeacherId(teacherId);

        // Check if teacher is assigned to any class except the excluded one
        for (ClassEntity classEntity : classesAsClassTeacher) {
            if (!classEntity.getClassId().equals(excludeClassId) &&
                    !Boolean.TRUE.equals(classEntity.getIsDeleted())) {
                return true;
            }
        }

        for (ClassEntity classEntity : classesAsAssistant) {
            if (!classEntity.getClassId().equals(excludeClassId) &&
                    !Boolean.TRUE.equals(classEntity.getIsDeleted())) {
                return true;
            }
        }

        return false;
    }

    // Helper method to map request DTO to entity
    private void mapRequestToEntity(com.sc.dto.request.ClassCreateRequestDTO request, ClassEntity entity) {
        entity.setClassName(request.getClassName());
        entity.setClassCode(request.getClassCode());
        entity.setAcademicYear(request.getAcademicYear());
        entity.setSection(request.getSection());
        entity.setMaxStudents(request.getMaxStudents());
        entity.setCurrentStudents(request.getCurrentStudents());
        entity.setRoomNumber(request.getRoomNumber());
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
        entity.setDescription(request.getDescription());
        entity.setClassTeacherId(request.getClassTeacherId());
        entity.setClassTeacherSubject(request.getClassTeacherSubject());
        entity.setAssistantTeacherId(request.getAssistantTeacherId());
        entity.setAssistantTeacherSubject(request.getAssistantTeacherSubject());
        entity.setWorkingDays(request.getWorkingDays());
        entity.setStatus(request.getStatus());

        // Convert otherTeacherSubject DTOs to entity format
        if (request.getOtherTeacherSubject() != null) {
            List<ClassEntity.TeacherSubjectAssignment> assignments =
                    request.getOtherTeacherSubject().stream()
                            .map(dto -> new ClassEntity.TeacherSubjectAssignment(
                                    dto.getTeacherId(),
                                    dto.getTeacherName(),
                                    dto.getSubjects()
                            ))
                            .collect(Collectors.toList());
            entity.setOtherTeacherSubject(assignments);
        }
    }

    // Helper method to map entity to response DTO
    private ClassResponseDTO mapEntityToResponse(ClassEntity entity) {
        ClassResponseDTO response = new ClassResponseDTO();
        response.setClassId(entity.getClassId());
        response.setClassName(entity.getClassName());
        response.setClassCode(entity.getClassCode());
        response.setAcademicYear(entity.getAcademicYear());
        response.setSection(entity.getSection());
        response.setMaxStudents(entity.getMaxStudents());
        response.setCurrentStudents(entity.getCurrentStudents());
        response.setRoomNumber(entity.getRoomNumber());
        response.setStartTime(entity.getStartTime());
        response.setEndTime(entity.getEndTime());
        response.setDescription(entity.getDescription());
        response.setClassTeacherId(entity.getClassTeacherId());
        response.setClassTeacherSubject(entity.getClassTeacherSubject());
        response.setAssistantTeacherId(entity.getAssistantTeacherId());
        response.setAssistantTeacherSubject(entity.getAssistantTeacherSubject());
        response.setWorkingDays(entity.getWorkingDays());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        // Convert entity assignments to DTOs
        List<TeacherSubjectAssignmentDTO> assignmentDTOs =
                entity.getOtherTeacherSubject().stream()
                        .map(assignment -> new TeacherSubjectAssignmentDTO(
                                assignment.getTeacherId(),
                                assignment.getTeacherName(),
                                assignment.getSubjects()
                        ))
                        .collect(Collectors.toList());
        response.setOtherTeacherSubject(assignmentDTOs);

        return response;
    }
}