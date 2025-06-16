package com.example.studentmanagement.service.student;

import com.example.studentmanagement.converter.StudentConverter;
import com.example.studentmanagement.designpattern.iterator.IStudentIterator;
import com.example.studentmanagement.dto.student.StudentDTO;
import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.StudentClass;
import com.example.studentmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StudentConverter studentConverter;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private StudentClassRepository studentClassRepository;
    @Autowired
    private StudentManager studentManager;

    @Transactional
    public StudentDTO performStudy(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        student.performStudy();  // Kích hoạt hành động "học"
        Student savedStudent = studentRepository.save(student);
        return enrichStudentDTOWithClassInfo(studentConverter.toDto(savedStudent), savedStudent);
    }

    @Transactional
    public StudentDTO performSuspend(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        student.performSuspend(); // Kích hoạt hành động "bảo lưu"
        Student savedStudent = studentRepository.save(student);
        return enrichStudentDTOWithClassInfo(studentConverter.toDto(savedStudent), savedStudent);
    }

    @Transactional
    public StudentDTO performWarn(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        student.performWarn(); // Kích hoạt hành động "cảnh cáo"
        Student savedStudent = studentRepository.save(student);
        return enrichStudentDTOWithClassInfo(studentConverter.toDto(savedStudent), savedStudent);
    }

    @Transactional
    public StudentDTO performActivate(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        student.performActivate(); // Kích hoạt hành động "kích hoạt"
        Student savedStudent = studentRepository.save(student);
        return enrichStudentDTOWithClassInfo(studentConverter.toDto(savedStudent), savedStudent);
    }

    @Transactional
    public StudentDTO performEnroll(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        student.performEnroll(); // Kích hoạt hành động "ghi danh"
        Student savedStudent = studentRepository.save(student);
        return enrichStudentDTOWithClassInfo(studentConverter.toDto(savedStudent), savedStudent);
    }

    @Transactional
    public StudentDTO performLeave(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        student.performLeave(); // Kích hoạt hành động "nghỉ học"
        Student savedStudent = studentRepository.save(student);
        return enrichStudentDTOWithClassInfo(studentConverter.toDto(savedStudent), savedStudent);
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByStatusUsingIterator(String statusName) {
        StudyStatus targetStatusEnum;
        try {
            targetStatusEnum = StudyStatus.valueOf(statusName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status provided: '" + statusName + "'. Valid statuses are: " + Arrays.stream(StudyStatus.values()).map(Enum::name).collect(Collectors.joining(", ")) + ".", e);
        }

        List<StudentDTO> filteredStudentDTOs = new ArrayList<>();
        IStudentIterator iterator = studentManager.getStudentIterator();

        while (iterator.hasMore()) {
            Student student = iterator.getNext();
            if (student != null && student.getStatus() != null &&
                    student.getStatus().equals(targetStatusEnum)) {
                filteredStudentDTOs.add(enrichStudentDTOWithClassInfo(studentConverter.toDto(student), student));
            }
        }

        return filteredStudentDTOs;
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudentsUsingIterator() {
        List<StudentDTO> allStudentDTOs = new ArrayList<>();
        IStudentIterator iterator = studentManager.getStudentIterator();

        while (iterator.hasMore()) {
            Student student = iterator.getNext();
            if (student != null) {
                allStudentDTOs.add(enrichStudentDTOWithClassInfo(studentConverter.toDto(student), student));
            }
        }
        return allStudentDTOs;
    }

    private StudentDTO enrichStudentDTOWithClassInfo(StudentDTO dto, Student student) {
        List<StudentClass> studentClasses = studentClassRepository.findByStudentOrderByAcademicYearDesc(student);

        if (!studentClasses.isEmpty()) {
            StudentClass latestClass = studentClasses.get(0);
            dto.setAcademicYear(latestClass.getAcademicYear());
            dto.setClassName(latestClass.getClazz().getClassName()); 
        }
        return dto;
    }
    
    @Transactional(readOnly = true)
    public List<StudentDTO> findStudentsBySubject(String subjectId, String academicYear, int semester) {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> result = new ArrayList<>();
        for (Student student : students) {
            boolean hasScore = student.getScores().stream().anyMatch(score ->
                String.valueOf(score.getSubject().getId()).equals(subjectId)
                && score.getSemester() == semester
                && score.getAcademicYear().equals(academicYear)
            );
            if (hasScore) {
                StudentDTO dto = studentConverter.toDto(student);
                // Lấy lớp học mới nhất của sinh viên (nếu có)
                List<StudentClass> studentClasses = studentClassRepository.findByStudentOrderByAcademicYearDesc(student);
                if (!studentClasses.isEmpty()) {
                    StudentClass latestClass = studentClasses.get(0);
                    dto.setClassName(latestClass.getClazz().getClassName());
                }
                result.add(dto);
            }
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> findStudentsByClassAndSubject(String className, String subjectId, String academicYear, int semester) {
        List<StudentClass> studentClasses = studentClassRepository.findByClassNameAndAcademicYear(className, academicYear);
        List<StudentDTO> result = new ArrayList<>();
        for (StudentClass sc : studentClasses) {
            Student student = sc.getStudent();
            boolean hasScore = student.getScores().stream().anyMatch(score ->
                String.valueOf(score.getSubject().getId()).equals(subjectId)
                && score.getSemester() == semester
                && score.getAcademicYear().equals(academicYear)
            );
            if (hasScore) {
                StudentDTO dto = studentConverter.toDto(student);
                dto.setClassName(className);
                result.add(dto);
            }
        }
        return result;
    }
}
