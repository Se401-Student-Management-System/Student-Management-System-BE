package com.example.studentmanagement.controller;

import com.example.studentmanagement.designpattern.builder.SchoolRecordDirector;
import com.example.studentmanagement.designpattern.facade.StudentManagementFacade;
import com.example.studentmanagement.dto.director.SchoolRecord;
import com.example.studentmanagement.dto.director.StudentPaymentDTO;
import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.model.Supervisor;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.model.UserEntity;
import com.example.studentmanagement.service.director.StudentPaymentService;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.service.director.UserService;
import com.example.studentmanagement.service.director.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/director")
public class BoardOfDirectorsController {

    private final SchoolRecordDirector director;
    private final StudentManagementFacade facade;
    private final UserService userService;
    private final AccountService accountService;
    @Autowired
    private StudentPaymentService studentPaymentService;

    public BoardOfDirectorsController(SchoolRecordDirector director, StudentManagementFacade facade,
                                     UserService userService, AccountService accountService) {
        this.director = director;
        this.facade = facade;
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/school-record")
    public ResponseEntity<SchoolRecord> getSchoolRecord(
            @RequestParam String studentId,
            @RequestParam int semester,
            @RequestParam String academicYear,
            @RequestParam(required = false) String comment) {
        SchoolRecord schoolRecord = director.constructSchoolRecord(studentId, semester, academicYear, comment);
        return ResponseEntity.ok(schoolRecord);
    }

    @GetMapping("/class-school-records")
    public ResponseEntity<List<SchoolRecord>> getClassSchoolRecords(
            @RequestParam String className,
            @RequestParam int semester,
            @RequestParam String academicYear) {
        List<SchoolRecord> records = director.constructSchoolRecordsForClass(className, semester, academicYear);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/statics-grade")
    public Map<String, Object> getGradeStatistics(
            @RequestParam(required = false, defaultValue = "10") int grade,
            @RequestParam(required = false, defaultValue = "1") int semester,
            @RequestParam(required = false, defaultValue = "2024-2025") String academicYear) {
        return facade.getGradeOverview(grade, semester, academicYear);
    }

    @GetMapping("/statics-student")
    public Map<String, Object> getStudentStatistics(
            @RequestParam(required = false, defaultValue = "2024-2025") String academicYear) {
        return facade.getStudentOverview(academicYear);
    }

    @GetMapping("/statics-conduct")
    public Map<String, Object> getConductStatistics(
            @RequestParam(required = false, defaultValue = "1") int semester,
            @RequestParam(required = false, defaultValue = "2024-2025") String academicYear) {
        return facade.getConductOverview(semester, academicYear);
    }

    @GetMapping("/student-payment")
    public ResponseEntity<Map<String, Object>> getStudentPayment(
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) String className) {
        // Lấy danh sách năm học và lớp
        List<String> yearList = studentPaymentService.getYearList();
        List<String> classList = studentPaymentService.getClassList();

        // Mặc định: lấy năm học và lớp đầu tiên nếu không cung cấp
        String selectedYear = academicYear != null && !academicYear.isEmpty() ? academicYear : yearList.get(0);
        String selectedClass = className != null && !className.isEmpty() ? className : classList.get(0);

        // Lấy thông tin hóa đơn
        List<StudentPaymentDTO> tuitionList = studentPaymentService.getInvoiceInfo(selectedClass, selectedYear);

        // Tạo response
        Map<String, Object> response = new HashMap<>();
        response.put("tuitionList", tuitionList);
        response.put("yearList", yearList);
        response.put("classList", classList);
        response.put("selectedYear", selectedYear);
        response.put("selectedClass", selectedClass);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new user", description = "Create a new user based on role and entity type")
    @ApiResponse(responseCode = "200", description = "Successfully created",
                 content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = Object.class)))
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        try {
            UserEntity entity = userService.createUser(request);
            // Chuyển đổi sang JSON phù hợp với từng entity (có thể dùng DTO)
            Map<String, Object> response = new HashMap<>();
            response.put("id", entity instanceof Student ? ((Student) entity).getId() : 
                               entity instanceof Teacher ? ((Teacher) entity).getId() : 
                               entity instanceof Cashier ? ((Cashier) entity).getId() : 
                               entity instanceof Supervisor ? ((Supervisor) entity).getId() : null);
            response.put("account", entity.getAccount());
            if (entity instanceof Student) {
                Student student = (Student) entity;
                response.put("birthPlace", student.getBirthPlace());
                response.put("ethnicity", student.getEthnicity());
            } else if (entity instanceof Teacher) {
                Teacher teacher = (Teacher) entity;
                response.put("position", teacher.getPosition());
            } else if (entity instanceof Cashier) {
                Cashier cashier = (Cashier) entity;
                response.put("status", cashier.getStatus());
            } else if (entity instanceof Supervisor) {
                Supervisor supervisor = (Supervisor) entity;
                response.put("status", supervisor.getStatus());
            }
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal error: " + e.getMessage());
        }
    }
}