package com.example.studentmanagement.service.director;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.designpattern.factorymethod.*;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Role;
import com.example.studentmanagement.repository.AccountRepository;
import com.example.studentmanagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final AccountRepository accountRepo;
    private final RoleRepository roleRepo;

    public UserService(AccountRepository accountRepo, RoleRepository roleRepo) {
        this.accountRepo = accountRepo;
        this.roleRepo = roleRepo;
    }

    public Object createUser(UserRequest request) {
        Role role = roleRepo.findByRoleName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        account.setEmail(request.getEmail());
        account.setFullName(request.getFullName());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setAddress(request.getAddress());
        account.setGender(Account.Gender.valueOf(request.getGender()));
        account.setRole(role);

        Account savedAccount = accountRepo.save(account);

        UserFactory factory = getFactory(request.getEntity());
        return factory.create(request, savedAccount);
    }

    public Account saveEntity(Account account) {
        return accountRepo.save(account);
    }

    public UserFactory getFactory(String type) {
        return switch (type) {
            case "Cashier" -> new CashierFactory();
            case "Student" -> new StudentFactory();
            case "Teacher" -> new TeacherFactory();
            case "Supervisor" -> new SupervisorFactory();
            default -> throw new IllegalArgumentException("Loại người dùng không hợp lệ");
        };
    }
}