package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.BoardOfDirectors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardOfDirectorsRepository extends JpaRepository<BoardOfDirectors, String> {
}