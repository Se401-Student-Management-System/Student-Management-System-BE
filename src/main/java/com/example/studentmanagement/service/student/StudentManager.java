package com.example.studentmanagement.service.student;

import com.example.studentmanagement.designpattern.iterator.IStudentIterator;
import com.example.studentmanagement.designpattern.iterator.StudentCollection;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentManager {
    private StudentCollection studentCollection;
    @Autowired
    private StudentRepository studentRepository;

    @PostConstruct
    public void init() {
        // Lấy tất cả sinh viên từ database
        List<Student> allStudents = studentRepository.findAll();
        // Khởi tạo StudentCollection với danh sách sinh viên này
        this.studentCollection = new StudentCollection(allStudents);
    }


//  PHUONG THUC PRINT GIONG NHU CLASS DIAGRAM ?
    public void printAllStudents() {
        IStudentIterator iterator = this.studentCollection.createIterator();
        System.out.println("\n--- Printing All Students (using Iterator from StudentManager) ---");
        while (iterator.hasMore()) {
            Student student = iterator.getNext();
            // Đảm bảo không null và có account để lấy fullName
            if (student != null && student.getAccount() != null) {
                System.out.println("ID: " + student.getId() + ", Name: " + student.getAccount().getFullName() + ", Status: " + student.getStatus());
            } else if (student != null) {
                System.out.println("ID: " + student.getId() + ", Name: (No Account), Status: " + student.getStatus());
            }
        }
        System.out.println("--- End of Student List ---");
    }

    public IStudentIterator getStudentIterator() {
        List<Student> currentStudents = studentRepository.findAll();
        this.studentCollection = new StudentCollection(currentStudents);
        return this.studentCollection.createIterator();
    }
}
