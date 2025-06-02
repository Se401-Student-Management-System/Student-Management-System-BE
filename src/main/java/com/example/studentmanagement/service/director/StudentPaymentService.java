package com.example.studentmanagement.service.director;

import com.example.studentmanagement.dto.director.StudentPaymentDTO;
import com.example.studentmanagement.model.Class;
import com.example.studentmanagement.model.Invoice;
import com.example.studentmanagement.repository.ClassRepository;
import com.example.studentmanagement.repository.InvoiceRepository;
import com.example.studentmanagement.repository.StudentClassRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentPaymentService {

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Lấy danh sách năm học
    public List<String> getYearList() {
        String sql = "SELECT DISTINCT sc.academic_year FROM student_class sc ORDER BY sc.academic_year DESC";
        @SuppressWarnings("unchecked")
        List<String> results = entityManager.createNativeQuery(sql).getResultList();
        return results;
    }

    // Lấy danh sách tên lớp
    public List<String> getClassList() {
        return classRepository.findAll().stream()
                .map(Class::getClassName)
                .sorted()
                .collect(Collectors.toList());
    }

    // Lấy thông tin hóa đơn theo tên lớp và năm học
    public List<StudentPaymentDTO> getInvoiceInfo(String className, String academicYear) {
        // Lấy classId từ className
        Class clazz = classRepository.findAll().stream()
                .filter(c -> c.getClassName().equals(className))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Class not found: " + className));

        String sql = "SELECT i.student_id, a.full_name, c.class_name, i.academic_year, " +
                     "i.total_fee, i.paid_amount, i.outstanding_amount, i.status " +
                     "FROM invoice i " +
                     "JOIN student s ON i.student_id = s.id " +
                     "JOIN account a ON s.account_id = a.id " +
                     "JOIN student_class sc ON s.id = sc.student_id AND sc.academic_year = :academicYear " +
                     "JOIN class c ON sc.class_id = c.id " +
                     "WHERE sc.class_id = :classId AND i.academic_year = :academicYear";
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("classId", clazz.getId())
                .setParameter("academicYear", academicYear)
                .getResultList();

        List<StudentPaymentDTO> tuitionList = new ArrayList<>();
        for (Object[] row : results) {
            StudentPaymentDTO dto = new StudentPaymentDTO();
            dto.setStudentId((String) row[0]);
            dto.setFullName((String) row[1]);
            dto.setClassName((String) row[2]);
            dto.setAcademicYear((String) row[3]);
            dto.setTotalFee(row[4] != null ? ((Number) row[4]).floatValue() : null);
            dto.setPaidAmount(row[5] != null ? ((Number) row[5]).floatValue() : null);
            dto.setOutstandingAmount(row[6] != null ? ((Number) row[6]).floatValue() : null);
            dto.setStatus((String) row[7]);
            tuitionList.add(dto);
        }

        return tuitionList;
    }
}