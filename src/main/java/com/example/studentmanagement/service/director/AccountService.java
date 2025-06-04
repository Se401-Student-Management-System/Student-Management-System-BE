package com.example.studentmanagement.service.director;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Role;
import com.example.studentmanagement.repository.AccountRepository;
import com.example.studentmanagement.repository.RoleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AccountService {

    private final AccountRepository accountRepo;
    private final RoleRepository roleRepo;

    public AccountService(AccountRepository accountRepo, RoleRepository roleRepo) {
        this.accountRepo = accountRepo;
        this.roleRepo = roleRepo;
    }

    public Account save(Account account) {
        return accountRepo.save(account);
    }

    public Account createAccount(UserRequest request) {
        if (request.getRoleId() == null) {
            throw new IllegalArgumentException("Role không được để trống");
        }

        Integer roleId;
        try {
            roleId = Integer.parseInt(request.getRoleId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Role ID không hợp lệ: " + request.getRoleId());
        }
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại: " + request.getRoleId()));

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        account.setEmail(request.getEmail());
        account.setFullName(request.getFullName());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setAddress(request.getAddress());

        try {
            account.setGender(Account.Gender.valueOf(request.getGender()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Giới tính không hợp lệ: " + request.getGender());
        }

        try {
            account.setBirthDate(java.time.LocalDate.parse(request.getBirthDate()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ: " + request.getBirthDate());
        }

        account.setRole(role);

        return accountRepo.save(account);
    }
}