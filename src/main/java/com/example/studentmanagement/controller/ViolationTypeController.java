package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.ViolationType;
import com.example.studentmanagement.service.violation.ViolationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/violation-types")
public class ViolationTypeController {
    @Autowired
    private ViolationTypeService violationTypeService;

    @PostMapping("/add")
    public ResponseEntity<ViolationType> addViolationType(@RequestBody ViolationType violationType) {
        ViolationType saved = violationTypeService.addViolationType(violationType);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ViolationType>> getAllViolationTypes() {
        return ResponseEntity.ok(violationTypeService.getAllViolationTypes());
    }
}
