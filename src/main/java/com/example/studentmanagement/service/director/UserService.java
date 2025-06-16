package com.example.studentmanagement.service.director;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.designpattern.factorymethod.UserFactory;
import com.example.studentmanagement.designpattern.factorymethod.StudentFactory;
import com.example.studentmanagement.designpattern.factorymethod.TeacherFactory;
import com.example.studentmanagement.designpattern.factorymethod.CashierFactory;
import com.example.studentmanagement.designpattern.factorymethod.SupervisorFactory;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.model.Supervisor;
import com.example.studentmanagement.model.UserEntity;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.TeacherRepository;
import com.example.studentmanagement.repository.CashierRepository;
import com.example.studentmanagement.repository.SupervisorRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final AccountService accountService;
    private final StudentRepository studentRepo;
    private final TeacherRepository teacherRepo;
    private final CashierRepository cashierRepo;
    private final SupervisorRepository supervisorRepo;
    private final Map<String, UserFactory> factoryMap;

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
        this.factoryMap = new HashMap<>();
        factoryMap.put("Student", studentFactory);
        factoryMap.put("Teacher", teacherFactory);
        factoryMap.put("Cashier", cashierFactory);
        factoryMap.put("Supervisor", supervisorFactory);
    }

    public UserEntity createUser(UserRequest request) {
        if (request.getEntity() == null) {
            throw new IllegalArgumentException("Entity type is required");
        }

        Account account = accountService.createAccount(request);
        UserFactory factory = factoryMap.get(request.getEntity());
        if (factory == null) {
            throw new IllegalArgumentException("Unknown entity type: " + request.getEntity());
        }
        Object entity = factory.create(request, account);

        switch (request.getEntity()) {
            case "Student":
                Student student = (Student) entity;
                student.setId(generateStudentId());
                return studentRepo.save(student);
            case "Teacher":
                Teacher teacher = (Teacher) entity;
                teacher.setId(generateTeacherId());
                return teacherRepo.save(teacher);
            case "Cashier":
                Cashier cashier = (Cashier) entity;
                cashier.setId(generateCashierId());
                return cashierRepo.save(cashier);
            case "Supervisor":
                Supervisor supervisor = (Supervisor) entity;
                supervisor.setId(generateSupervisorId());
                return supervisorRepo.save(supervisor);
            default:
                throw new IllegalArgumentException("Unknown entity type: " + request.getEntity());
        }
    }

    private String generateStudentId() {
        long count = studentRepo.count();
        return String.format("HS%03d", count + 1);
    }

    private String generateTeacherId() {
        long count = teacherRepo.count();
        return String.format("GV%03d", count + 1);
    }

    private String generateCashierId() {
        long count = cashierRepo.count();
        return String.format("CS%03d", count + 1);
    }

    private String generateSupervisorId() {
        long count = supervisorRepo.count();
        return String.format("SP%03d", count + 1);
    }
}