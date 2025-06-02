package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupervisorRepository extends JpaRepository<Supervisor, String> {
}

