package com.example.studentmanagement.service.director;

import com.example.studentmanagement.dto.director.StudentRequest;
import com.example.studentmanagement.dto.director.TeacherRequest;
import com.example.studentmanagement.dto.director.CashierRequest;
import com.example.studentmanagement.dto.director.SupervisorRequest;
import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.designpattern.factorymethod.CashierFactory;
import com.example.studentmanagement.designpattern.factorymethod.StudentFactory;
import com.example.studentmanagement.designpattern.factorymethod.SupervisorFactory;
import com.example.studentmanagement.designpattern.factorymethod.TeacherFactory;
import com.example.studentmanagement.designpattern.factorymethod.UserFactory;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.model.Supervisor;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.TeacherRepository;
import com.example.studentmanagement.repository.CashierRepository;
import com.example.studentmanagement.repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import com.example.studentmanagement.model.Role;
import java.util.UUID;

@Service
public class UserService {

    private final AccountService accountService;
    private final StudentRepository studentRepo;
    private final TeacherRepository teacherRepo;
    private final CashierRepository cashierRepo;
    private final SupervisorRepository supervisorRepo;

    // Inject các factory nếu cần dùng riêng
    private final StudentFactory studentFactory;
    private final TeacherFactory teacherFactory;
    private final CashierFactory cashierFactory;
    private final SupervisorFactory supervisorFactory;

    // Inject map các factory
    @Autowired
    private Map<String, UserFactory<?, ?>> factoryMap;

    @Autowired
    public UserService(AccountService accountService,
                       StudentRepository studentRepo,
                       TeacherRepository teacherRepo,
                       CashierRepository cashierRepo,
                       SupervisorRepository supervisorRepo,
                       StudentFactory studentFactory,
                       TeacherFactory teacherFactory,
                       CashierFactory cashierFactory,
                       SupervisorFactory supervisorFactory) {
        this.accountService = accountService;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.cashierRepo = cashierRepo;
        this.supervisorRepo = supervisorRepo;
        this.studentFactory = studentFactory;
        this.teacherFactory = teacherFactory;
        this.cashierFactory = cashierFactory;
        this.supervisorFactory = supervisorFactory;
        // KHÔNG khởi tạo factoryMap ở đây!
    }

    // public Object createUser(UserRequest request) {
    //     if (request.getEntity() == null) {
    //         throw new IllegalArgumentException("Entity type is required");
    //     }

    //     // Tạo và lưu account
    //     Account account = accountService.createAccount(request);

    //     // Tạo entity bằng factory
    //     UserFactory factory = factoryMap.get(request.getEntity());
    //     if (factory == null) {
    //         throw new IllegalArgumentException("Unknown entity type: " + request.getEntity());
    //     }
    //     Object entity = factory.create(request, account);

    //     // Gán ID thủ công và lưu entity vào bảng tương ứng
    //     switch (request.getEntity()) {
    //         case "Student":
    //             Student student = (Student) entity;
    //             student.setId(generateStudentId());
    //             return studentRepo.save(student);
    //         case "Teacher":
    //             Teacher teacher = (Teacher) entity;
    //             teacher.setId(generateTeacherId());
    //             return teacherRepo.save(teacher);
    //         case "Cashier":
    //             Cashier cashier = (Cashier) entity;
    //             cashier.setId(generateCashierId());
    //             return cashierRepo.save(cashier);
    //         case "Supervisor":
    //             Supervisor supervisor = (Supervisor) entity;
    //             supervisor.setId(generateSupervisorId());
    //             return supervisorRepo.save(supervisor);
    //         default:
    //             throw new IllegalArgumentException("Unknown entity type: " + request.getEntity());
    //     }
    // }

    private String generateStudentId() {
        long count = studentRepo.count();
        return String.format("HS%03d", count + 1); // HS001, HS002, ...
    }

    private String generateTeacherId() {
        long count = teacherRepo.count();
        return String.format("GV%03d", count + 1); // GV001, GV002, ...
    }

    private String generateCashierId() {
        long count = cashierRepo.count();
        return String.format("CS%03d", count + 1); // CS001, CS002, ...
    }

    private String generateSupervisorId() {
        long count = supervisorRepo.count();
        return String.format("SP%03d", count + 1); // SP001, SP002, ...
    }
    

public Student createStudent(StudentRequest request) {
    Account account = accountService.createAccount(request);
    StudentFactory studentFactory = (StudentFactory) factoryMap.get("Student");
    Student student = studentFactory.create(request, account);
    student.setId(generateStudentId());
    return studentRepo.save(student);
}

public Teacher createTeacher(TeacherRequest request) {
    Account account = accountService.createAccount(request);
    TeacherFactory teacherFactory = (TeacherFactory) factoryMap.get("Teacher");
    Teacher teacher = teacherFactory.create(request, account);
    teacher.setId(generateTeacherId());
    return teacherRepo.save(teacher);
}

public Cashier createCashier(CashierRequest request) {
    Account account = accountService.createAccount(request);
    CashierFactory cashierFactory = (CashierFactory) factoryMap.get("Cashier");
    Cashier cashier = cashierFactory.create(request, account);
    cashier.setId(generateCashierId());
    return cashierRepo.save(cashier);
}

public Supervisor createSupervisor(SupervisorRequest request) {
    Account account = accountService.createAccount(request);
    SupervisorFactory supervisorFactory = (SupervisorFactory) factoryMap.get("Supervisor");
    Supervisor supervisor = supervisorFactory.create(request, account);
    supervisor.setId(generateSupervisorId());
    return supervisorRepo.save(supervisor);
}
}