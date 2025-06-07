package com.example.studentmanagement.designpattern.proxy;

import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GradeBookProxy {

    private final ScoreRepository scoreRepository;

    public List<Score> getGrades(GradeInterface user, Student student, int semester, String academicYear) {
        List<Score> scores = scoreRepository.findByStudentAndSemesterAndAcademicYear(student, semester, academicYear);

        if (scores.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy điểm của học sinh.");
        }
        
        if (user instanceof Student studentUser) {
            if (!studentUser.getId().equals(student.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không được phép xem điểm của học sinh khác.");
            }
        } else if (user instanceof Teacher teacherUser) {
            boolean isTeaching = scores.stream()
                .anyMatch(score -> score.getTeacher().getId().equals(teacherUser.getId()));

            if (!isTeaching) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không dạy học sinh này, không thể xem điểm.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vai trò không được phép truy cập điểm số.");
        }

        return scores;
    }
}