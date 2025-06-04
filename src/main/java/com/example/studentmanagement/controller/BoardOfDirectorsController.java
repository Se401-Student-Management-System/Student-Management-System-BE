package com.example.studentmanagement.controller;

import com.example.studentmanagement.designpattern.builder.SchoolRecordDirector;
import com.example.studentmanagement.designpattern.facade.StudentManagementFacade;
import com.example.studentmanagement.dto.director.SchoolRecord;
import com.example.studentmanagement.dto.director.StudentPaymentDTO;
import com.example.studentmanagement.service.director.StudentPaymentService;
import com.example.studentmanagement.designpattern.factorymethod.UserFactory;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.service.director.UserService;
import com.example.studentmanagement.service.director.AccountService;

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

    public BoardOfDirectorsController(SchoolRecordDirector director, StudentManagementFacade facade, UserService userService, AccountService accountService) {
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

     @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        try {
            // B1: Tạo tài khoản (Account)
            Account account = accountService.createAccount(request);

            // B2: Lấy factory tương ứng với entityType
            UserFactory factory = userService.getFactory(request.getEntity());

            // B3: Tạo entity (Cashier, Student,...)
            factory.create(request, account);

            // B4: Lưu entity
            Object savedEntity = userService.saveEntity(account);

            return ResponseEntity.ok(savedEntity);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal error: " + e.getMessage());
        }
    }
}