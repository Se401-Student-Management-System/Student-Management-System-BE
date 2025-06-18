package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.violation.ViolationRequest;
import com.example.studentmanagement.dto.violation.ViolationResponse;
import com.example.studentmanagement.model.Violation;
import com.example.studentmanagement.repository.ViolationRepository;
import com.example.studentmanagement.service.violation.ViolationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/violations")
public class ViolationController {
    @Autowired
    private ViolationService violationService;

    @Autowired
    private ViolationRepository violationRepository;

    @GetMapping("/list")
    public ResponseEntity<List<Violation>> getAllViolations() {
        List<Violation> violations = violationRepository.findAll();
        return ResponseEntity.ok(violations);
    }

    @PostMapping("/record-and-process")
    public ResponseEntity<ViolationResponse> recordAndProcessViolation(@Valid @RequestBody ViolationRequest request) {
        try {
            ViolationResponse response = violationService.recordAndProcessViolation(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ViolationResponse(
                    "Lỗi ghi nhận vi phạm: " + e.getMessage(), null, null, null, null, null, null, false, "Error"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ViolationResponse(
                    "Lỗi xử lý nghiệp vụ: " + e.getMessage(), null, null, null, null, null, null, false, "Error"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ViolationResponse(
                            "Lỗi hệ thống khi ghi nhận vi phạm: " + e.getMessage(), null, null, null, null, null, null,
                            false, "Error"));
        }
    }
}
