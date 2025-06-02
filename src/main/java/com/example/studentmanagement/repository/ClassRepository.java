package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Class, Integer> {
}

