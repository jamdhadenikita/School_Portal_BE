package com.sc.service.serviceImpl;

import com.sc.dto.request.FeesRequestDto;
import com.sc.dto.response.FeesResponseDto;
import com.sc.entity.AdditionalFee;
import com.sc.entity.FeesEntity;
import com.sc.entity.Installment;
import com.sc.entity.StudentEntity;
import com.sc.repository.FeesRepository;
import com.sc.repository.StudentRepository;
import com.sc.service.FeesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeesServiceImpl implements FeesService {

    private static final Logger logger = LoggerFactory.getLogger(FeesServiceImpl.class);

    @Autowired
    private FeesRepository feesRepository;

    @Autowired
    private StudentRepository studentRepository;

    // ============= 🎯 CREATE FEES =============

    @Override
    @Transactional
    public FeesResponseDto createFees(FeesRequestDto requestDto) {
        logger.info("Creating fees for student ID: {}", requestDto.getStudentId());

        try {
            // 1. Validate and get Student
            StudentEntity student = studentRepository.findById(requestDto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + requestDto.getStudentId()));

            // 2. Check if fees already exists for this student in current academic year
            Optional<FeesEntity> existingFees = feesRepository.findByStudentAndAcademicYear(
                    student, requestDto.getAcademicYear());

            if (existingFees.isPresent()) {
                throw new RuntimeException("Fees already exists for student ID: " +
                        requestDto.getStudentId() + " for academic year: " + requestDto.getAcademicYear());
            }

            // 3. Convert DTO to Entity
            FeesEntity entity = toEntity(requestDto, student);

            // 4. Calculate totals
            entity.calculateTotalFees();
            entity.calculateRemainingFees();

            // 5. Save fees
            FeesEntity saved = feesRepository.save(entity);

            // 6. Add to student's fees list (bidirectional)
            student.getFeesList().add(saved);
            studentRepository.save(student);

            logger.info("Fees created successfully with ID: {} for student ID: {}",
                    saved.getId(), student.getStudentId());

            return toResponseDto(saved);

        } catch (Exception e) {
            logger.error("Error creating fees: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create fees: " + e.getMessage());
        }
    }

    // ============= 🔍 GET FEES BY ID =============

    @Override
    @Transactional(readOnly = true)
    public FeesResponseDto getFeesById(Long id) {
        try {
            Optional<FeesEntity> optional = feesRepository.findById(id);
            if (optional.isPresent()) {
                return toResponseDto(optional.get());
            }
            logger.warn("Fees not found with ID: {}", id);
            return null;
        } catch (Exception e) {
            logger.error("Error fetching fees by ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to fetch fees: " + e.getMessage());
        }
    }

    // ============= 🔍 GET FEES BY STUDENT ID =============

    @Override
    @Transactional(readOnly = true)
    public FeesResponseDto getFeesByStudentId(Long studentId) {
        try {
            // Method 1: Using custom query
            Optional<FeesEntity> entity = feesRepository.findByStudentId(studentId);

            // Method 2: Using Student entity (alternative)
            // StudentEntity student = studentRepository.findById(studentId).orElse(null);
            // Optional<FeesEntity> entity = student != null ? feesRepository.findByStudent(student) : Optional.empty();

            if (entity.isPresent()) {
                return toResponseDto(entity.get());
            }

            logger.warn("Fees not found for student ID: {}", studentId);
            return null;

        } catch (Exception e) {
            logger.error("Error fetching fees for student ID {}: {}", studentId, e.getMessage());
            throw new RuntimeException("Failed to fetch fees: " + e.getMessage());
        }
    }

    // ============= 📋 GET ALL FEES =============

    @Override
    @Transactional(readOnly = true)
    public List<FeesResponseDto> getAllFees() {
        try {
            List<FeesEntity> entities = feesRepository.findAll();
            logger.info("Retrieved {} fees records", entities.size());
            return entities.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all fees: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch fees list: " + e.getMessage());
        }
    }

    // ============= ✏️ UPDATE FEES =============

    @Override
    @Transactional
    public FeesResponseDto updateFees(Long id, FeesRequestDto requestDto) {
        logger.info("Updating fees with ID: {}", id);

        try {
            Optional<FeesEntity> optional = feesRepository.findById(id);
            if (optional.isPresent()) {
                FeesEntity entity = optional.get();

                // Update entity with DTO data
                updateEntity(entity, requestDto);

                // Recalculate totals
                entity.calculateTotalFees();
                entity.calculateRemainingFees();

                FeesEntity updated = feesRepository.save(entity);
                logger.info("Fees updated successfully with ID: {}", updated.getId());

                return toResponseDto(updated);
            }

            logger.warn("Fees not found with ID: {}", id);
            return null;

        } catch (Exception e) {
            logger.error("Error updating fees with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update fees: " + e.getMessage());
        }
    }

    // ============= 🗑️ DELETE FEES =============

    @Override
    @Transactional
    public void deleteFees(Long id) {
        logger.info("Deleting fees with ID: {}", id);

        try {
            if (!feesRepository.existsById(id)) {
                throw new RuntimeException("Fees not found with ID: " + id);
            }

            // Optional: Remove from student's fees list
            Optional<FeesEntity> feesOpt = feesRepository.findById(id);
            if (feesOpt.isPresent()) {
                FeesEntity fees = feesOpt.get();
                StudentEntity student = fees.getStudent();
                if (student != null) {
                    student.getFeesList().remove(fees);
                    studentRepository.save(student);
                }
            }

            feesRepository.deleteById(id);
            logger.info("Fees deleted successfully with ID: {}", id);

        } catch (Exception e) {
            logger.error("Error deleting fees with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete fees: " + e.getMessage());
        }
    }

    // ============= 🆕 ADDITIONAL BUSINESS METHODS =============

    /**
     * Get all pending fees
     */
    @Transactional(readOnly = true)
    public List<FeesResponseDto> getAllPendingFees() {
        try {
            List<FeesEntity> entities = feesRepository.findAllPendingFees();
            return entities.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching pending fees: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch pending fees: " + e.getMessage());
        }
    }

    /**
     * Get all paid fees
     */
    @Transactional(readOnly = true)
    public List<FeesResponseDto> getAllPaidFees() {
        try {
            List<FeesEntity> entities = feesRepository.findAllPaidFees();
            return entities.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching paid fees: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch paid fees: " + e.getMessage());
        }
    }

    /**
     * Get fees by academic year
     */
    @Transactional(readOnly = true)
    public List<FeesResponseDto> getFeesByAcademicYear(String academicYear) {
        try {
            List<FeesEntity> entities = feesRepository.findByAcademicYear(academicYear);
            return entities.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching fees for academic year {}: {}", academicYear, e.getMessage());
            throw new RuntimeException("Failed to fetch fees: " + e.getMessage());
        }
    }

    /**
     * Process installment payment
     */
    @Transactional
    public FeesResponseDto processInstallmentPayment(Long feesId, Long installmentId,
                                                     String paymentMode, String transactionRef) {
        logger.info("Processing installment payment for Fees ID: {}, Installment ID: {}", feesId, installmentId);

        try {
            FeesEntity fees = feesRepository.findById(feesId)
                    .orElseThrow(() -> new RuntimeException("Fees not found with ID: " + feesId));

            Optional<Installment> installmentOpt = fees.getInstallmentsList().stream()
                    .filter(inst -> installmentId.equals(inst.getInstallmentId()))
                    .findFirst();

            if (installmentOpt.isPresent()) {
                Installment installment = installmentOpt.get();
                installment.markAsPaid(paymentMode, transactionRef);
                fees.calculateRemainingFees();

                FeesEntity updated = feesRepository.save(fees);
                logger.info("Installment payment processed successfully");
                return toResponseDto(updated);
            } else {
                throw new RuntimeException("Installment not found with ID: " + installmentId);
            }
        } catch (Exception e) {
            logger.error("Error processing installment payment: {}", e.getMessage());
            throw new RuntimeException("Failed to process payment: " + e.getMessage());
        }
    }

    // ============= 🔄 CONVERTERS =============

    /**
     * Convert Request DTO to Entity
     */
    private FeesEntity toEntity(FeesRequestDto dto, StudentEntity student) {
        FeesEntity entity = new FeesEntity();

        // Set Student relationship
        entity.setStudent(student);

        // Set basic fees
        entity.setAdmissionFees(dto.getAdmissionFees());
        entity.setUniformFees(dto.getUniformFees());
        entity.setBookFees(dto.getBookFees());
        entity.setTuitionFees(dto.getTuitionFees());

        // Set additional fees
        List<AdditionalFee> additionalFees = new ArrayList<>();
        if (dto.getAdditionalFeesList() != null && !dto.getAdditionalFeesList().isEmpty()) {
            for (Map.Entry<String, Integer> entry : dto.getAdditionalFeesList().entrySet()) {
                AdditionalFee fee = new AdditionalFee();
                fee.setName(entry.getKey());
                fee.setAmount(entry.getValue());
                fee.setCategory("OTHER");
                fee.setIsMandatory(true);
                additionalFees.add(fee);
            }
        }
        entity.setAdditionalFeesList(additionalFees);

        // Set payment details
        entity.setInitialAmount(dto.getInitialAmount());
        entity.setPaymentMode(dto.getPaymentMode());
        entity.setCashierName(dto.getCashierName());
        entity.setTransactionId(dto.getTransactionId());

        // Set academic year
        entity.setAcademicYear(dto.getAcademicYear() != null ?
                dto.getAcademicYear() :
                java.time.Year.now().toString() + "-" + java.time.Year.now().plusYears(1).toString());

        // Set installments
        List<Installment> installments = new ArrayList<>();
        if (dto.getInstallmentsList() != null && !dto.getInstallmentsList().isEmpty()) {
            for (FeesRequestDto.InstallmentDto instDto : dto.getInstallmentsList()) {
                Installment inst = new Installment();
                inst.setInstallmentId(instDto.getInstallmentId());
                inst.setAmount(instDto.getAmount());
                inst.setAddonAmount(instDto.getAddonAmount() != null ? instDto.getAddonAmount() : 0);
                inst.setPaidDate(instDto.getPaidDate());
                inst.setStatus(instDto.getStatus() != null ? instDto.getStatus() : "PENDING");
                inst.setDueAmount(instDto.getDueAmount() != null ?
                        instDto.getDueAmount() : instDto.getAmount());
                inst.setDueDate(instDto.getDueDate());
                inst.setPaymentMode(instDto.getPaymentMode());
                inst.setTransactionReference(instDto.getTransactionReference());
                inst.setRemarks(instDto.getRemarks());
                installments.add(inst);
            }
        }
        entity.setInstallmentsList(installments);

        return entity;
    }

    /**
     * Convert Entity to Response DTO
     */
    private FeesResponseDto toResponseDto(FeesEntity entity) {
        FeesResponseDto dto = new FeesResponseDto();

        // Basic info
        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudent() != null ? entity.getStudent().getStdId() : null);

        // Student details (optional - for enriched response)
        if (entity.getStudent() != null) {
            dto.setStudentName(entity.getStudent().getFirstName() + " " +
                    (entity.getStudent().getLastName() != null ? entity.getStudent().getLastName() : ""));
            dto.setStudentRollNumber(entity.getStudent().getStudentRollNumber());
            dto.setStudentClass(entity.getStudent().getCurrentClass());
            dto.setStudentSection(entity.getStudent().getSection());
        }

        // Fee details
        dto.setAdmissionFees(entity.getAdmissionFees());
        dto.setUniformFees(entity.getUniformFees());
        dto.setBookFees(entity.getBookFees());
        dto.setTuitionFees(entity.getTuitionFees());

        // Additional fees
        Map<String, Integer> additionalFeesMap = new LinkedHashMap<>();
        if (entity.getAdditionalFeesList() != null) {
            for (AdditionalFee fee : entity.getAdditionalFeesList()) {
                additionalFeesMap.put(fee.getName(), fee.getAmount());
            }
        }
        dto.setAdditionalFeesList(additionalFeesMap);

        // Totals
        dto.setTotalFees(entity.getTotalFees());
        dto.setInitialAmount(entity.getInitialAmount());
        dto.setRemainingFees(entity.getRemainingFees());

        // Payment info
        dto.setPaymentMode(entity.getPaymentMode());
        dto.setCashierName(entity.getCashierName());
        dto.setTransactionId(entity.getTransactionId());
        dto.setAcademicYear(entity.getAcademicYear());

        // Payment status
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setFullyPaid(entity.isFullyPaid());

        // Installments
        List<FeesResponseDto.InstallmentDto> installmentDtos = new ArrayList<>();
        if (entity.getInstallmentsList() != null) {
            for (Installment inst : entity.getInstallmentsList()) {
                FeesResponseDto.InstallmentDto instDto = new FeesResponseDto.InstallmentDto();
                instDto.setInstallmentId(inst.getInstallmentId());
                instDto.setAmount(inst.getAmount());
                instDto.setAddonAmount(inst.getAddonAmount());
                instDto.setTotalAmount(inst.getTotalAmount());
                instDto.setPaidDate(inst.getPaidDate());
                instDto.setStatus(inst.getStatus());
                instDto.setDueAmount(inst.getDueAmount());
                instDto.setDueDate(inst.getDueDate());
                instDto.setPaymentMode(inst.getPaymentMode());
                instDto.setTransactionReference(inst.getTransactionReference());
                instDto.setRemarks(inst.getRemarks());
                instDto.setRemainingDays(inst.getRemainingDays());
                instDto.setOverdue(inst.isOverdue());
                instDto.setPaid(inst.isPaid());
                installmentDtos.add(instDto);
            }
        }
        dto.setInstallmentsList(installmentDtos);

        // Timestamps
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    /**
     * Update existing entity with DTO data
     */
    private void updateEntity(FeesEntity entity, FeesRequestDto dto) {

        // Update student only if provided (careful with this!)
        if (dto.getStudentId() != null && !dto.getStudentId().equals(entity.getStudent().getStdId())) {
            StudentEntity newStudent = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + dto.getStudentId()));
            entity.setStudent(newStudent);
        }

        // Update fee amounts
        if (dto.getAdmissionFees() != null) entity.setAdmissionFees(dto.getAdmissionFees());
        if (dto.getUniformFees() != null) entity.setUniformFees(dto.getUniformFees());
        if (dto.getBookFees() != null) entity.setBookFees(dto.getBookFees());
        if (dto.getTuitionFees() != null) entity.setTuitionFees(dto.getTuitionFees());

        // Update additional fees
        if (dto.getAdditionalFeesList() != null) {
            List<AdditionalFee> additionalFees = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : dto.getAdditionalFeesList().entrySet()) {
                AdditionalFee fee = new AdditionalFee();
                fee.setName(entry.getKey());
                fee.setAmount(entry.getValue());
                fee.setCategory("OTHER");
                fee.setIsMandatory(true);
                additionalFees.add(fee);
            }
            entity.setAdditionalFeesList(additionalFees);
        }

        // Update payment details
        if (dto.getInitialAmount() != null) entity.setInitialAmount(dto.getInitialAmount());
        if (dto.getPaymentMode() != null) entity.setPaymentMode(dto.getPaymentMode());
        if (dto.getCashierName() != null) entity.setCashierName(dto.getCashierName());
        if (dto.getTransactionId() != null) entity.setTransactionId(dto.getTransactionId());
        if (dto.getAcademicYear() != null) entity.setAcademicYear(dto.getAcademicYear());

        // Update installments
        if (dto.getInstallmentsList() != null) {
            List<Installment> installments = new ArrayList<>();
            for (FeesRequestDto.InstallmentDto instDto : dto.getInstallmentsList()) {
                Installment inst = new Installment();
                inst.setInstallmentId(instDto.getInstallmentId());
                inst.setAmount(instDto.getAmount());
                inst.setAddonAmount(instDto.getAddonAmount() != null ? instDto.getAddonAmount() : 0);
                inst.setPaidDate(instDto.getPaidDate());
                inst.setStatus(instDto.getStatus() != null ? instDto.getStatus() : "PENDING");
                inst.setDueAmount(instDto.getDueAmount() != null ?
                        instDto.getDueAmount() : instDto.getAmount());
                inst.setDueDate(instDto.getDueDate());
                inst.setPaymentMode(instDto.getPaymentMode());
                inst.setTransactionReference(instDto.getTransactionReference());
                inst.setRemarks(instDto.getRemarks());
                installments.add(inst);
            }
            entity.setInstallmentsList(installments);
        }
    }

    // ============= 🧮 LEGACY CALCULATION METHOD (now using entity methods) =============

    /**
     * @deprecated Use entity.calculateTotalFees() and entity.calculateRemainingFees() instead
     */
    @Deprecated
    private void calculateTotals(FeesEntity entity) {
        entity.calculateTotalFees();
        entity.calculateRemainingFees();
    }
}