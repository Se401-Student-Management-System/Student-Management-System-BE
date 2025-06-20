
import com.example.studentmanagement.designpattern.templatemethod.SupervisorBehaviorSummaryReport;
import com.example.studentmanagement.designpattern.templatemethod.TeacherGradeReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import com.example.studentmanagement.service.director.StudentConductStatics;



@RequestMapping("/supervisor")
public class SupervisorController {
    @Autowired
    private TeacherGradeReport teacherGradeReport;

    @Autowired
    private StudentConductStatics studentConductStatics;

    @Autowired
    private SupervisorBehaviorSummaryReport supervisorBehaviorSummaryReport;

    @GetMapping("/teacher-grade")
    public ResponseEntity<?> getTeacherGradeReport(
            @RequestParam int grade,
            @RequestParam int semester,
            @RequestParam String academicYear
    ) {
        Map<String, Object> params = Map.of(
                "grade", grade,
                "semester", semester,
                "academicYear", academicYear
        );
        Object result = teacherGradeReport.generateReport(params);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student-behavior-summary")
    public ResponseEntity<?> getStudentBehaviorSummary(
            @RequestParam int grade,
            @RequestParam int semester,
            @RequestParam String academicYear
    ) {
        Map<String, Object> params = Map.of(
                "grade", grade,
                "semester", semester,
                "academicYear", academicYear
        );
        Object result = supervisorBehaviorSummaryReport.generateReport(params);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/supervisor-conduct")
    public ResponseEntity<List<Map<String, Object>>> getSupervisorConductReport(
            @RequestParam int semester,
            @RequestParam String academicYear,
        
            @RequestParam int grade
    ) {
        List<Map<String, Object>> list = studentConductStatics.getViolationList(semester, academicYear, grade);
        return ResponseEntity.ok(list);
    }
    
}

    
        