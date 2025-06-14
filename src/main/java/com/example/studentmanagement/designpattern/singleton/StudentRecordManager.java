package com.example.studentmanagement.designpattern.singleton;

import com.example.studentmanagement.model.Student;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StudentRecordManager {

    private static StudentRecordManager instance;
    private final Map<String, Student> studentMap = new ConcurrentHashMap<>();

    private StudentRecordManager() {
        // Không gọi initSampleData nữa
    }

    public static synchronized StudentRecordManager getInstance() {
        if (instance == null) {
            instance = new StudentRecordManager();
        }
        return instance;
    }

    public void addStudent(Student student) {
        studentMap.put(student.getId(), student);
    }

    public Student getStudentById(String id) {
        return studentMap.get(id);
    }

    // Có thể bổ sung update, delete nếu cần
}
