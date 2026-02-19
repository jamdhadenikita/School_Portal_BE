package com.sc.controller;

import com.sc.dto.request.FeesRequestDto;
import com.sc.dto.response.FeesResponseDto;
import com.sc.service.FeesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fees")
public class FeesController {

    private static final Logger logger = LoggerFactory.getLogger(FeesController.class);

    @Autowired
    private FeesService feesService;

    // ============= 🎯 CREATE FEES =============

    @PostMapping
    public ResponseEntity<?> createFees(@RequestBody FeesRequestDto requestDto) {
        logger.info("REST request to create fees for student ID: {}", requestDto.getStudentId());

        try {
            FeesResponseDto response = feesService.createFees(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating fees: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to create fees: " + e.getMessage()));
        }
    }

    // ============= 🔍 GET FEES BY ID =============

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeesById(@PathVariable Long id) {
        logger.info("REST request to get fees by ID: {}", id);

        try {
            FeesResponseDto response = feesService.getFeesById(id);
            if (response != null) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Fees not found with ID: " + id));
        } catch (Exception e) {
            logger.error("Error fetching fees by ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch fees: " + e.getMessage()));
        }
    }

    // ============= 🔍 GET FEES BY STUDENT ID =============

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getFeesByStudentId(@PathVariable Long studentId) {
        logger.info("REST request to get fees by student ID: {}", studentId);

        try {
            FeesResponseDto response = feesService.getFeesByStudentId(studentId);
            if (response != null) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Fees not found for student ID: " + studentId));
        } catch (Exception e) {
            logger.error("Error fetching fees for student ID {}: {}", studentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch fees: " + e.getMessage()));
        }
    }

    // ============= 📋 GET ALL FEES =============

    @GetMapping
    public ResponseEntity<?> getAllFees() {
        logger.info("REST request to get all fees");

        try {
            List<FeesResponseDto> responses = feesService.getAllFees();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error fetching all fees: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch fees list: " + e.getMessage()));
        }
    }

    // ============= ✏️ UPDATE FEES =============

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFees(@PathVariable Long id,
                                        @RequestBody FeesRequestDto requestDto) {
        logger.info("REST request to update fees with ID: {}", id);

        try {
            FeesResponseDto response = feesService.updateFees(id, requestDto);
            if (response != null) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Fees not found with ID: " + id));
        } catch (Exception e) {
            logger.error("Error updating fees with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to update fees: " + e.getMessage()));
        }
    }

    // ============= 🗑️ DELETE FEES =============

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFees(@PathVariable Long id) {
        logger.info("REST request to delete fees with ID: {}", id);

        try {
            feesService.deleteFees(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting fees with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Failed to delete fees: " + e.getMessage()));
        }
    }

    // ============= 🆕 ADDITIONAL BUSINESS ENDPOINTS =============

    /**
     * Get all pending fees
     */
    @GetMapping("/pending")
    public ResponseEntity<?> getAllPendingFees() {
        logger.info("REST request to get all pending fees");

        try {
            List<FeesResponseDto> responses = feesService.getAllPendingFees();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error fetching pending fees: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch pending fees: " + e.getMessage()));
        }
    }

    /**
     * Get all paid fees
     */
    @GetMapping("/paid")
    public ResponseEntity<?> getAllPaidFees() {
        logger.info("REST request to get all paid fees");

        try {
            List<FeesResponseDto> responses = feesService.getAllPaidFees();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error fetching paid fees: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch paid fees: " + e.getMessage()));
        }
    }

    /**
     * Get fees by academic year
     */
    @GetMapping("/academic-year/{academicYear}")
    public ResponseEntity<?> getFeesByAcademicYear(@PathVariable String academicYear) {
        logger.info("REST request to get fees for academic year: {}", academicYear);

        try {
            List<FeesResponseDto> responses = feesService.getFeesByAcademicYear(academicYear);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error fetching fees for academic year {}: {}", academicYear, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch fees: " + e.getMessage()));
        }
    }

    /**
     * Process installment payment
     */
    @PostMapping("/{feesId}/installments/{installmentId}/pay")
    public ResponseEntity<?> processInstallmentPayment(
            @PathVariable Long feesId,
            @PathVariable Long installmentId,
            @RequestParam String paymentMode,
            @RequestParam(required = false) String transactionRef) {

        logger.info("REST request to process payment for Fees ID: {}, Installment ID: {}",
                feesId, installmentId);

        try {
            FeesResponseDto response = feesService.processInstallmentPayment(
                    feesId, installmentId, paymentMode, transactionRef);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing installment payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to process payment: " + e.getMessage()));
        }
    }

    /**
     * Check if fees exists for student
     */
    @GetMapping("/student/{studentId}/exists")
    public ResponseEntity<?> checkFeesExists(@PathVariable Long studentId) {
        logger.info("REST request to check if fees exists for student ID: {}", studentId);

        try {
            FeesResponseDto response = feesService.getFeesByStudentId(studentId);
            boolean exists = response != null;
            return ResponseEntity.ok(new CheckResponse(exists,
                    exists ? "Fees found" : "No fees found for student ID: " + studentId));
        } catch (Exception e) {
            logger.error("Error checking fees existence: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to check fees: " + e.getMessage()));
        }
    }

    /**
     * Get fees summary statistics
     */
    @GetMapping("/summary")
    public ResponseEntity<?> getFeesSummary(@RequestParam(required = false) String academicYear) {
        logger.info("REST request to get fees summary for academic year: {}", academicYear);

        try {
            // You can add this method to your service
            // FeesSummaryDto summary = feesService.getFeesSummary(academicYear);
            // return ResponseEntity.ok(summary);

            return ResponseEntity.ok("Summary endpoint - To be implemented");
        } catch (Exception e) {
            logger.error("Error fetching fees summary: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch summary: " + e.getMessage()));
        }
    }

    // ============= 🎯 INNER CLASSES FOR RESPONSES =============

    /**
     * Error Response DTO
     */
    static class ErrorResponse {
        private String message;
        private long timestamp;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    /**
     * Check Response DTO
     */
    static class CheckResponse {
        private boolean exists;
        private String message;
        private long timestamp;

        public CheckResponse(boolean exists, String message) {
            this.exists = exists;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExists() { return exists; }
        public void setExists(boolean exists) { this.exists = exists; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}