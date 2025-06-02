package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TitleRepository extends JpaRepository<Title, Integer> {
    List<Title> findByMinAvgScoreLessThanEqualAndMinBehaviorScoreLessThanEqual(Double minAvgScore, Double minBehaviorScore);
}