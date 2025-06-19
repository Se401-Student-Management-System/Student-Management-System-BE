package com.example.studentmanagement.service.student;

import com.example.studentmanagement.converter.StudentConverter;
import com.example.studentmanagement.designpattern.iterator.IStudentIterator;
import com.example.studentmanagement.designpattern.state.StudentStateFactory;
import com.example.studentmanagement.dto.behavior.BehaviorEvaluationResponse;
import com.example.studentmanagement.dto.student.AssignClassRequest;
import com.example.studentmanagement.dto.student.StudentDTO;
import com.example.studentmanagement.dto.student.UpdateStudentRequest;
import com.example.studentmanagement.dto.behavior.BehaviorEvalutionRequest;
import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Behavior;
import com.example.studentmanagement.model.Class;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.StudentClass;
import com.example.studentmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private BehaviorRepository behaviorRepository;

    // BEGIN - STATE
    @Transactional
    public StudentDTO performWarn(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        student.performWarn(); // Kích hoạt hành động "cảnh cáo"
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

    // API XẾP LỚP: Chuyển từ PENDING -> ACTIVE
    @Transactional
    public StudentDTO assignStudentToClass(AssignClassRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Student not found with ID: " + request.getStudentId()));

        Class clazz = classRepository.findByClassName(request.getClassName())
                .orElseThrow(
                        () -> new IllegalArgumentException("Class not found with Name: " + request.getClassName()));

        // Kiểm tra xem học sinh đã được gán vào lớp này chưa
        if (studentClassRepository.findByStudentAndClazzAndAcademicYear(student, clazz, request.getAcademicYear())
                .isPresent()) {
            throw new IllegalArgumentException("Student " + student.getId() + " is already enrolled in class "
                    + clazz.getClassName() + " for academic year " + request.getAcademicYear());
        }

        StudentClass studentClass = new StudentClass();
        studentClass.setStudent(student);
        studentClass.setClazz(clazz);
        studentClass.setAcademicYear(request.getAcademicYear());
        studentClassRepository.save(studentClass);

        // Kích hoạt enroll (chuyển PENDING -> ACTIVE)
        student.performEnroll();
        Student savedStudent = studentRepository.save(student);

        return enrichStudentDTOWithClassInfo(studentConverter.toDto(savedStudent), savedStudent);
    }

    // API ĐÁNH GIÁ HẠNH KIỂM: Nếu dưới 50 điểm, chuyển sang WARNING
    @Transactional
    public BehaviorEvaluationResponse evaluateStudentBehavior(BehaviorEvalutionRequest request) { // Thay đổi kiểu trả
                                                                                                  // về
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Student not found with ID: " + request.getStudentId()));

        Optional<Behavior> behaviorOptional = behaviorRepository.findByStudentAndAcademicYearAndSemester(
                student, request.getAcademicYear(), request.getSemester());

        Behavior behavior;
        String message = "Đã cập nhật điểm hạnh kiểm.";
        if (behaviorOptional.isPresent()) {
            behavior = behaviorOptional.get();
        } else {
            behavior = new Behavior();
            behavior.setStudent(student);
            behavior.setAcademicYear(request.getAcademicYear());
            behavior.setSemester(request.getSemester());
            behavior.setBehaviorScore(100); // Điểm mặc định khi tạo mới
            behavior.setStatus("Good"); // Trạng thái mặc định
            message = "Đã tạo bản ghi hạnh kiểm mới và cập nhật điểm.";
        }

        behavior.setBehaviorScore(request.getBehaviorScore());
        behavior.setStatus(getBehaviorStatus(request.getBehaviorScore()));
        behaviorRepository.save(behavior);

        String initialStudentStatus = student.getStatus().name();
        if (request.getBehaviorScore() < 50) {
            student.performWarn(); // Kích hoạt hành động warn
            if (!student.getStatus().name().equals(initialStudentStatus)) {
                message += " Học sinh đã chuyển trạng thái sang " + student.getStatus().name()
                        + " do điểm hạnh kiểm thấp.";
            }
        }

        Student savedStudent = studentRepository.save(student);

        return new BehaviorEvaluationResponse(
                savedStudent.getId(),
                savedStudent.getAccount().getFullName(),
                request.getAcademicYear(),
                request.getSemester(),
                request.getBehaviorScore(),
                behavior.getStatus(), // Trạng thái hạnh kiểm của Behavior
                savedStudent.getStatus().name(), // Trạng thái hiện tại của Student
                message);
    }

    private String getBehaviorStatus(Integer score) {
        if (score >= 90)
            return "Good";
        if (score >= 70)
            return "Fair";
        if (score >= 50)
            return "Average";
        return "Poor";
    }

    // API UPDATE STUDENT
    @Transactional
    public StudentDTO updateStudent(String studentId, UpdateStudentRequest request) { // Thay StudentDTO bằng
                                                                                      // UpdateStudentRequest
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        studentConverter.toEntity(request, existingStudent);

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            StudyStatus targetStatusEnum;
            try {
                targetStatusEnum = StudyStatus.valueOf(request.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status provided: '" + request.getStatus()
                        + "'. Valid statuses are: "
                        + Arrays.stream(StudyStatus.values()).map(Enum::name).collect(Collectors.joining(", ")) + ".",
                        e);
            }
            existingStudent.setCurrentState(StudentStateFactory.getState(targetStatusEnum, existingStudent));
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        return enrichStudentDTOWithClassInfo(studentConverter.toDto(updatedStudent), updatedStudent);
    }
    // END - STATE

    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByStatusUsingIterator(String statusName) {
        StudyStatus targetStatusEnum;
        try {
            targetStatusEnum = StudyStatus.valueOf(statusName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status provided: '" + statusName + "'. Valid statuses are: "
                    + Arrays.stream(StudyStatus.values()).map(Enum::name).collect(Collectors.joining(", ")) + ".", e);
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
            boolean hasScore = student.getScores().stream()
                    .anyMatch(score -> String.valueOf(score.getSubject().getId()).equals(subjectId)
                            && score.getSemester() == semester
                            && score.getAcademicYear().equals(academicYear));
            if (hasScore) {
                StudentDTO dto = studentConverter.toDto(student);
                // Lấy lớp học mới nhất của sinh viên (nếu có)
                List<StudentClass> studentClasses = studentClassRepository
                        .findByStudentOrderByAcademicYearDesc(student);
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
    public List<StudentDTO> findStudentsByClassAndSubject(String className, String subjectId, String academicYear,
            int semester) {
        List<StudentClass> studentClasses = studentClassRepository.findByClassNameAndAcademicYear(className,
                academicYear);
        List<StudentDTO> result = new ArrayList<>();
        for (StudentClass sc : studentClasses) {
            Student student = sc.getStudent();
            boolean hasScore = student.getScores().stream()
                    .anyMatch(score -> String.valueOf(score.getSubject().getId()).equals(subjectId)
                            && score.getSemester() == semester
                            && score.getAcademicYear().equals(academicYear));
            if (hasScore) {
                StudentDTO dto = studentConverter.toDto(student);
                dto.setClassName(className);
                result.add(dto);
            }
        }
        return result;
    }

    public StudentDTO getStudentById(String id) {
        // Tìm kiếm sinh viên theo ID
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return enrichStudentDTOWithClassInfo(studentConverter.toDto(student), student);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + id);
        }
    }
}
