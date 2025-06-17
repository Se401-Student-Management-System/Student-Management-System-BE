package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Class;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Class, Integer> {
    Optional<Class> findByClassName(String className);
}
