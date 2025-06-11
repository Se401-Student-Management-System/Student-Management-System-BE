package com.example.studentmanagement.service;

import com.example.studentmanagement.designpattern.proxy.GradeBookProxy;
import com.example.studentmanagement.designpattern.proxy.GradeInterface;
import com.example.studentmanagement.dto.GradeResponse;
import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GradeBookProxy gradeBookProxy;

    public List<GradeResponse> getGrades(String userId, String role, String studentId, int semester, String academicYear) {
        GradeInterface user;
        Student targetStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh"));

        if ("STUDENT".equalsIgnoreCase(role)) {
            user = studentRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh đang đăng nhập"));
        } else if ("TEACHER".equalsIgnoreCase(role)) {
            user = teacherRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy giáo viên đang đăng nhập"));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vai trò không được phép truy cập điểm số.");
        }

        List<Score> scores = gradeBookProxy.getGrades(user, targetStudent, semester, academicYear);

        return scores.stream().map(score -> new GradeResponse(
                score.getSubject().getName(),
                score.getScore15m1(),
                score.getScore15m2(),
                score.getScore1h1(),
                score.getScore1h2(),
                score.getFinalScore(),
                score.getSemester(),
                score.getAcademicYear(),
                score.getComments()
        )).collect(Collectors.toList());
    }
}