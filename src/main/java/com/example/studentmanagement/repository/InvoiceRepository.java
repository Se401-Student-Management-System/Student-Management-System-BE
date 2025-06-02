package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findByAcademicYear(String academicYear);
    List<Invoice> findByStatus(String status);
    List<Invoice> findByAcademicYearAndStatus(String academicYear, String status);
}