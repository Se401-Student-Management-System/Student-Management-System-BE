
package com.example.studentmanagement.designpattern.templatemethod;

import com.example.studentmanagement.dto.GradeResponse;
import com.example.studentmanagement.service.GradeService;
import com.example.studentmanagement.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class TeacherGradeReport extends ReportTemplate<Object> {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private StudentService studentService;

    @Override
    protected Object fetchData(Map<String, Object> params) {
        // Nếu có đủ các tham số lấy điểm
        if (params.containsKey("userId") && params.containsKey("role") && params.containsKey("studentId")
                && params.containsKey("semester") && params.containsKey("academicYear")) {
            String userId = (String) params.get("userId");
            String role = (String) params.get("role");
            String studentId = (String) params.get("studentId");
            int semester = (int) params.get("semester");
            String academicYear = (String) params.get("academicYear");
            return gradeService.getGrades(userId, role, studentId, semester, academicYear);
        }
        // Nếu có đủ các tham số lấy danh sách học sinh theo lớp và môn
        if (params.containsKey("className") && params.containsKey("subjectId")) {
            // Trả về List<StudentDTO>
            String className = (String) params.get("className");
            String subjectId = (String) params.get("subjectId");
            String academicYear = (String) params.get("academicYear");
            int semester = (int) params.get("semester");
            return studentService.findStudentsByClassAndSubject(className, subjectId, academicYear, semester);
        }
        throw new IllegalArgumentException("Thiếu tham số cho báo cáo");
    }

    @Override
    protected Object processData(Object rawData, Map<String, Object> params) {
        return rawData;
    }

    @Override
    protected void validateParams(Map<String, Object> params) {
        // Nếu lấy điểm học sinh
        if (params.containsKey("userId") && params.containsKey("role") && params.containsKey("studentId")) {
            if (!params.containsKey("semester") || !params.containsKey("academicYear")) {
                throw new IllegalArgumentException("Thiếu tham số báo cáo điểm");
            }
            return;
        }
        // Nếu lấy danh sách học sinh theo lớp và môn
        if (params.containsKey("className") && params.containsKey("subjectId")) {
            if (!params.containsKey("semester") || !params.containsKey("academicYear")) {
                throw new IllegalArgumentException("Thiếu tham số báo cáo điểm");
            }
            return;
        }
        throw new IllegalArgumentException("Thiếu tham số báo cáo điểm");
    }
}
