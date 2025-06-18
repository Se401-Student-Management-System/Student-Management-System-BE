package com.example.studentmanagement.service.violation;

import com.example.studentmanagement.model.ViolationType;
import com.example.studentmanagement.repository.ViolationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViolationTypeService {
    @Autowired
    private ViolationTypeRepository violationTypeRepository;

    public ViolationType addViolationType(ViolationType violationType) {
        return violationTypeRepository.save(violationType);
    }

    public List<ViolationType> getAllViolationTypes() {
        return violationTypeRepository.findAll();
    }
}
