package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.AuthenticationContext;
import com.example.studentmanagement.service.AuthenticationService;
import com.example.studentmanagement.designpattern.CoR.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthenticationContext context = authenticationService.authenticate(request);
            return ResponseEntity.ok(context.getAccount());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}