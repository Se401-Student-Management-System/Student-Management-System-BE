package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.AuthenticationContext;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.service.AuthenticationService;
import com.example.studentmanagement.designpattern.CoR.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthenticationContext context = authenticationService.authenticate(request);
            Account account = context.getAccount();
            Map<String, Object> result = new HashMap<>();
            result.put("accountId", account.getId());
            result.put("username", account.getUsername());
            result.put("role", account.getRole().getRoleName());
            result.put("fullName", account.getFullName());
            if (account.getRole().getRoleName().equalsIgnoreCase("Student") && account.getStudent() != null) {
                result.put("userId", account.getStudent().getId());
            } else if (account.getRole().getRoleName().equalsIgnoreCase("Teacher") && account.getTeacher() != null) {
                result.put("userId", account.getTeacher().getId());
            }
            return ResponseEntity.ok(result);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}