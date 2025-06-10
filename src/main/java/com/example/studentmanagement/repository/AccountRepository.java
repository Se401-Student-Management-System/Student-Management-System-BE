package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<Account> findByUsername(String username);
}