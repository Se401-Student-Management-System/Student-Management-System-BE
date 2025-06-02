package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.ViolationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViolationTypeRepository extends JpaRepository<ViolationType, Integer> {
}
