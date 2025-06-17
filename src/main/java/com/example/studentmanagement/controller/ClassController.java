package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Class;
import com.example.studentmanagement.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    private ClassRepository classRepository;

    @GetMapping("/list")
    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }
}
