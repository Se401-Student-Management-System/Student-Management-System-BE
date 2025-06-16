package com.example.studentmanagement.service.teacher;

import com.example.studentmanagement.designpattern.decorator.BasicEvaluationProcessor;
import com.example.studentmanagement.designpattern.decorator.EvaluationComponent;
import com.example.studentmanagement.designpattern.decorator.NotificationDecorator;
import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final NotificationDecorator evaluationComponent;
    private final ScoreRepository scoreRepository;

    @Autowired
    public TeacherService(BasicEvaluationProcessor basicProcessor, 
                         NotificationDecorator notificationDecorator,
                         ScoreRepository scoreRepository) {
        // Bọc BasicEvaluationProcessor trong NotificationDecorator
        this.evaluationComponent = new NotificationDecorator(basicProcessor, 
            notificationDecorator.getMailSender());
        this.scoreRepository = scoreRepository;
    }

    public void addComments(List<CommentRequest> commentRequests, Teacher teacher) {
        for (CommentRequest request : commentRequests) {
            Score score = scoreRepository.findByStudentIdAndSubjectIdAndSemesterAndAcademicYear(
                request.getStudentId(),
                request.getSubjectId(),
                request.getSemester(),
                request.getAcademicYear()
            ).orElseThrow(() -> new IllegalArgumentException(
                "Không tìm thấy điểm cho học sinh " + request.getStudentId() + 
                " môn " + request.getSubjectId() + 
                " học kỳ " + request.getSemester() + 
                " năm học " + request.getAcademicYear()
            ));

            evaluationComponent.addComment(score, teacher, request.getComment());
        }

        // Gửi email xác nhận cho giáo viên sau khi xử lý tất cả
        evaluationComponent.sendTeacherEmail();
    }

    // DTO nội bộ cho yêu cầu nhận xét
    public static class CommentRequest {
        private String studentId;
        private Integer subjectId;
        private Integer semester;
        private String academicYear;
        private String comment;

        // Getters and setters
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public Integer getSubjectId() { return subjectId; }
        public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }
        public Integer getSemester() { return semester; }
        public void setSemester(Integer semester) { this.semester = semester; }
        public String getAcademicYear() { return academicYear; }
        public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}