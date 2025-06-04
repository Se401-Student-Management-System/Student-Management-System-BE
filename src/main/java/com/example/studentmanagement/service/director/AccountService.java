package com.example.studentmanagement.service.director;

import com.example.studentmanagement.dto.director.UserRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Role;
import com.example.studentmanagement.repository.AccountRepository;
import com.example.studentmanagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

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
        account.setBirthDate(java.time.LocalDate.parse(request.getBirthDate()));
        account.setRole(role);

        return accountRepo.save(account);
    }
}