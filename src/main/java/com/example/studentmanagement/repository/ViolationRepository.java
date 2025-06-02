package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Violation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViolationRepository extends JpaRepository<Violation, Integer> {
}
