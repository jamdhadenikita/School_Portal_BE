package com.sc.repository;

import com.sc.entity.TeacherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Long>, JpaSpecificationExecutor<TeacherEntity> {

    Optional<TeacherEntity> findByEmployeeId(String employeeId);
    boolean existsByEmployeeId(String employeeId);

    Optional<TeacherEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<TeacherEntity> findByAadharNumber(String aadharNumber);
    boolean existsByAadharNumber(String aadharNumber);

    Optional<TeacherEntity> findByPanNumber(String panNumber);
    boolean existsByPanNumber(String panNumber);

    // Basic queries
    List<TeacherEntity> findByDepartment(String department);
    List<TeacherEntity> findByDesignation(String designation);
    List<TeacherEntity> findByStatus(String status);
    List<TeacherEntity> findByEmploymentType(String employmentType);
    List<TeacherEntity> findByGender(String gender);

    // Search queries
    List<TeacherEntity> findByTeacherNameContainingIgnoreCase(String keyword);
    List<TeacherEntity> findByEmployeeIdContainingIgnoreCase(String keyword);
    List<TeacherEntity> findByEmailContainingIgnoreCase(String keyword);

    // Status based queries
    List<TeacherEntity> findByStatusAndApprovedTrue(String status);
    List<TeacherEntity> findByApprovedTrue();
    List<TeacherEntity> findByApprovedFalse();
    List<TeacherEntity> findByDepartmentAndApprovedTrue(String department);
    List<TeacherEntity> findByDepartmentAndApprovedFalse(String department);

    // Count queries
    long countByApprovedTrue();
    long countByApprovedFalse();
    long countByStatus(String status);
    long countByDepartment(String department);

    // Experience and salary based queries
    List<TeacherEntity> findByTotalExperienceGreaterThanEqual(Integer minExperience);
    List<TeacherEntity> findByTotalExperienceBetween(Integer minExperience, Integer maxExperience);
    List<TeacherEntity> findByTotalExperienceLessThanEqual(Integer maxExperience);

    List<TeacherEntity> findByGrossSalaryGreaterThanEqual(Double minSalary);
    List<TeacherEntity> findByGrossSalaryBetween(Double minSalary, Double maxSalary);
    List<TeacherEntity> findByGrossSalaryLessThanEqual(Double maxSalary);

    // Sorting queries
    List<TeacherEntity> findAllByOrderByTeacherNameAsc();
    List<TeacherEntity> findAllByOrderByJoiningDateDesc();
    List<TeacherEntity> findAllByOrderByGrossSalaryDesc();
    List<TeacherEntity> findAllByOrderByTotalExperienceDesc();
    List<TeacherEntity> findByDepartmentOrderByTeacherNameAsc(String department);
    List<TeacherEntity> findByDepartmentOrderByJoiningDateDesc(String department);

    // Combined queries
    List<TeacherEntity> findByDepartmentAndStatus(String department, String status);
    List<TeacherEntity> findByDepartmentAndDesignation(String department, String designation);
    List<TeacherEntity> findByDepartmentAndEmploymentType(String department, String employmentType);
    List<TeacherEntity> findByStatusAndTotalExperienceGreaterThanEqual(String status, Integer minExperience);
    List<TeacherEntity> findByDepartmentAndGrossSalaryGreaterThanEqual(String department, Double minSalary);

    // Override default delete methods to prevent accidental hard deletes
    @Override
    @Modifying
    @Query("UPDATE TeacherEntity t SET t.isDeleted = true WHERE t.id = ?1")
    void deleteById(Long id);

    @Override
    @Modifying
    @Query("UPDATE TeacherEntity t SET t.isDeleted = true WHERE t = ?1")
    void delete(TeacherEntity entity);

    // Include deleted teachers when needed (for admin purposes)
    @Query("SELECT t FROM TeacherEntity t WHERE t.id = ?1 AND t.isDeleted = true")
    Optional<TeacherEntity> findDeletedById(Long id);

    // Default method for active teachers
    default List<TeacherEntity> findAllActive() {
        return findAll(TeacherEntity.notDeleted());
    }

    // Pagination queries
    @Query("SELECT t FROM TeacherEntity t ORDER BY t.createdAt DESC")
    Page<TeacherEntity> findAllWithPagination(Pageable pageable);

    // SIMPLIFIED: Remove getApproved() and use the field directly with = true
    @Query("SELECT t FROM TeacherEntity t WHERE t.isDeleted = false AND t.approved = true ORDER BY t.createdAt DESC")
    Page<TeacherEntity> findAllActiveAndApproved(Pageable pageable);

    // SIMPLIFIED: Add the missing method
    @Query("SELECT t FROM TeacherEntity t WHERE t.department = :department AND t.isDeleted = false AND t.approved = true")
    List<TeacherEntity> findByDepartmentAndActive(@Param("department") String department);

    // Add this missing method too
    @Query("SELECT t FROM TeacherEntity t WHERE t.status = :status AND t.isDeleted = false AND t.approved = true")
    List<TeacherEntity> findByStatusAndActive(@Param("status") String status);

    // Search with pagination
    @Query("SELECT t FROM TeacherEntity t WHERE " +
            "(LOWER(t.teacherName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "t.employeeId LIKE CONCAT('%', :search, '%') OR " +
            "t.contactNumber LIKE CONCAT('%', :search, '%')) AND " +
            "t.isDeleted = false AND t.approved = true")
    Page<TeacherEntity> searchTeachers(@Param("search") String search, Pageable pageable);

    // Statistics
    @Query("SELECT COUNT(t) FROM TeacherEntity t WHERE t.isDeleted = false AND t.approved = true")
    long countActiveTeachers();

    @Query("SELECT AVG(t.totalExperience) FROM TeacherEntity t WHERE t.isDeleted = false AND t.approved = true")
    Double findAverageExperience();

    @Query("SELECT AVG(t.grossSalary) FROM TeacherEntity t WHERE t.isDeleted = false AND t.approved = true")
    Double findAverageSalary();

    @Query("SELECT t.department, COUNT(t) FROM TeacherEntity t WHERE t.isDeleted = false AND t.approved = true GROUP BY t.department")
    List<Object[]> countTeachersByDepartment();

    @Query("SELECT t.status, COUNT(t) FROM TeacherEntity t WHERE t.isDeleted = false AND t.approved = true GROUP BY t.status")
    List<Object[]> countTeachersByStatus();

    @Query("SELECT t.designation, COUNT(t) FROM TeacherEntity t WHERE t.isDeleted = false AND t.approved = true GROUP BY t.designation")
    List<Object[]> countTeachersByDesignation();

    // Find by primary subject
    List<TeacherEntity> findByPrimarySubject(String primarySubject);

    // Find teachers with highest salary
    @Query("SELECT t FROM TeacherEntity t WHERE t.isDeleted = false AND t.approved = true ORDER BY t.grossSalary DESC")
    List<TeacherEntity> findTopBySalary(Pageable pageable);

    // Find most experienced teachers
    @Query("SELECT t FROM TeacherEntity t WHERE t.isDeleted = false AND t.approved = true ORDER BY t.totalExperience DESC")
    List<TeacherEntity> findTopByExperience(Pageable pageable);

    // Check if employee ID exists (excluding deleted)
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TeacherEntity t WHERE t.employeeId = :employeeId AND t.isDeleted = false")
    boolean existsActiveByEmployeeId(@Param("employeeId") String employeeId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TeacherEntity t WHERE t.email = :email AND t.isDeleted = false")
    boolean existsActiveByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TeacherEntity t WHERE t.aadharNumber = :aadharNumber AND t.isDeleted = false")
    boolean existsActiveByAadharNumber(@Param("aadharNumber") String aadharNumber);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TeacherEntity t WHERE t.panNumber = :panNumber AND t.isDeleted = false")
    boolean existsActiveByPanNumber(@Param("panNumber") String panNumber);

    // Find teachers who joined in a specific year
    @Query("SELECT t FROM TeacherEntity t WHERE YEAR(t.joiningDate) = :year AND t.isDeleted = false AND t.approved = true")
    List<TeacherEntity> findByJoiningYear(@Param("year") Integer year);

    // Remove or comment out problematic queries temporarily:
    /*
    // REMOVE THESE FOR NOW:
    @Query("SELECT t FROM TeacherEntity t WHERE :subject MEMBER OF t.additionalSubjects AND t.isDeleted = false AND t.approved = true")
    List<TeacherEntity> findByAdditionalSubjectsContaining(@Param("subject") String subject);

    @Query("SELECT t FROM TeacherEntity t WHERE :className MEMBER OF t.classes AND t.isDeleted = false AND t.approved = true")
    List<TeacherEntity> findByClassesContaining(@Param("className") String className);

    @Query("SELECT DISTINCT t.department FROM TeacherEntity t WHERE t.isDeleted = false")
    List<String> findDistinctDepartments();

    @Query("SELECT DISTINCT t.designation FROM TeacherEntity t WHERE t.isDeleted = false")
    List<String> findDistinctDesignations();

    @Query("SELECT DISTINCT t.employmentType FROM TeacherEntity t WHERE t.isDeleted = false")
    List<String> findDistinctEmploymentTypes();

    @Query("SELECT DISTINCT t.status FROM TeacherEntity t WHERE t.isDeleted = false")
    List<String> findDistinctStatuses();

    @Query("SELECT t FROM TeacherEntity t WHERE t.joiningDate >= DATE_SUB(CURRENT_DATE, :days) AND t.isDeleted = false AND t.approved = true")
    List<TeacherEntity> findRecentJoins(@Param("days") Integer days);

    @Query("SELECT t FROM TeacherEntity t WHERE " +
           "(:department IS NULL OR t.department = :department) AND " +
           "(:designation IS NULL OR t.designation = :designation) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:employmentType IS NULL OR t.employmentType = :employmentType) AND " +
           "t.isDeleted = false AND t.approved = true")
    Page<TeacherEntity> findTeachersByFilters(
            @Param("department") String department,
            @Param("designation") String designation,
            @Param("status") String status,
            @Param("employmentType") String employmentType,
            Pageable pageable);
    */
}