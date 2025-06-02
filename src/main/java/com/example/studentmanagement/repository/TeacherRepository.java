package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Teacher;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Query("""
                SELECT t FROM Teacher t
                JOIN FETCH t.account a
                JOIN FETCH a.role
            """)
    Page<Teacher> findAllWithAccount(Pageable pageable);

    @Query("""
                SELECT t FROM Teacher t
                JOIN FETCH t.account a
                JOIN FETCH a.role
                WHERE LOWER(a.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(t.id) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Teacher> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
