package com.example.studentmanagement.service.director;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.designpattern.factorymethod.*;
import com.example.studentmanagement.designpattern.factorymethod.UserFactory;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Role;
import com.example.studentmanagement.repository.AccountRepository;
import com.example.studentmanagement.repository.RoleRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.TeacherRepository;
import com.example.studentmanagement.repository.CashierRepository;
import com.example.studentmanagement.repository.SupervisorRepository;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Supervisor;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.model.Cashier;
import com.example.studentmanagement.enums.WorkingStatus;
import com.example.studentmanagement.enums.TeachingStatus;
import com.example.studentmanagement.enums.TeacherPosition;
import com.example.studentmanagement.enums.StudyStatus;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final RoleRepository roleRepo;
    private final AccountService accountService;
    private final StudentRepository studentRepo;
    private final TeacherRepository teacherRepo;
    private final CashierRepository cashierRepo;
    private final SupervisorRepository supervisorRepo;

    public UserService(RoleRepository roleRepo, AccountService accountService,
                       StudentRepository studentRepo, TeacherRepository teacherRepo,
                       CashierRepository cashierRepo, SupervisorRepository supervisorRepo) {
        this.roleRepo = roleRepo;
        this.accountService = accountService;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.cashierRepo = cashierRepo;
        this.supervisorRepo = supervisorRepo;
    }

    public Object createUser(UserRequest request) {
        Integer roleId = Integer.valueOf(request.getRoleId());
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));

        Account account = accountService.createAccount(request);

        switch (role.getRoleName()) {
            case "Student":
                Student student = new Student();
                student.setAccount(account);
                student.setStatus(StudyStatus.ACTIVE);
                student.setBirthPlace(account.getAddress());
                student.setEthnicity("Kinh");
                return studentRepo.save(student);

            case "Teacher":
                Teacher teacher = new Teacher();
                teacher.setAccount(account);
                teacher.setStatus(TeachingStatus.DANG_GIANG_DAY);
                teacher.setPosition(TeacherPosition.TEACHER);
                return teacherRepo.save(teacher);

            case "Cashier":
                Cashier cashier = new Cashier();
                cashier.setAccount(account);
                cashier.setStatus(WorkingStatus.Working);
                return cashierRepo.save(cashier);

            case "Supervisor":
                Supervisor supervisor = new Supervisor();
                supervisor.setAccount(account);
                supervisor.setStatus(WorkingStatus.Working);
                return supervisorRepo.save(supervisor);

            default:
                return account; // Trường hợp không xác định rõ
        }
    }

    public UserFactory getFactory(String entityType) {
        switch (entityType) {
            case "Student":
                return new StudentFactory();
            case "Teacher":
                return new TeacherFactory();
            case "Cashier":
                return new CashierFactory();
            case "Supervisor":
                return new SupervisorFactory();
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }
    }
}
