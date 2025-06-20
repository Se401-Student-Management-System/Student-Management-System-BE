package com.example.studentmanagement.service.violation;

import com.example.studentmanagement.designpattern.strategy.*;
import com.example.studentmanagement.dto.violation.ViolationRequest;
import com.example.studentmanagement.dto.violation.ViolationResponse;
import com.example.studentmanagement.model.*;
import com.example.studentmanagement.model.Class;
import com.example.studentmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ViolationService {
    @Autowired
    private ViolationRepository violationRepository;
    @Autowired
    private ViolationTypeRepository violationTypeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private SupervisorRepository supervisorRepository;
    @Autowired
    private ViolationProcessor violationProcessor;
    @Autowired
    private MinorViolationStrategy minorViolationStrategy;
    @Autowired
    private ModerateViolationStrategy moderateViolationStrategy;
    @Autowired
    private MajorViolationStrategy majorViolationStrategy;

    @Transactional
    public ViolationResponse recordAndProcessViolation(ViolationRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Student not found with ID: " + request.getStudentId()));
        Class clazz = classRepository.findByClassName(request.getClassName())
                .orElseThrow(
                        () -> new IllegalArgumentException("Class not found with name: " + request.getClassName()));
        ViolationType violationType = violationTypeRepository.findById(request.getViolationTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Violation Type not found with ID: " + request.getViolationTypeId()));

        Supervisor supervisor = null;
        if (request.getSupervisorId() != null) {
            supervisor = supervisorRepository.findById(String.valueOf(request.getSupervisorId()))
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Supervisor not found with ID: " + request.getSupervisorId()));
        }

        Violation violation = new Violation();
        violation.setStudent(student);
        violation.setClazz(clazz);
        violation.setSupervisor(supervisor);
        violation.setViolationType(violationType);
        violation.setViolationDate(request.getViolationDate() != null ? request.getViolationDate() : LocalDate.now());
        violation.setAcademicYear(request.getAcademicYear());
        violation.setSemester(request.getSemester());

        Violation savedViolation = violationRepository.save(violation);

        ViolationHandlingStrategy strategyToUse;
        Double deductedPoints = violationType.getDeductedPoints();

        if (deductedPoints != null) {
            // Logic chọn chiến lược
            if (deductedPoints >= 30.0) {
                strategyToUse = majorViolationStrategy;
            } else if (deductedPoints >= 10.0) {
                strategyToUse = moderateViolationStrategy;
            } else {
                strategyToUse = minorViolationStrategy;
            }
        } else {
            strategyToUse = minorViolationStrategy;
        }

        violationProcessor.setStrategy(strategyToUse);
        ViolationResponse response = violationProcessor.processViolation(student, savedViolation);

        response.setMessage(
                "Vi phạm ID " + savedViolation.getId() + " đã được ghi nhận và xử lý. " + response.getMessage());
        return response;
    }
}
