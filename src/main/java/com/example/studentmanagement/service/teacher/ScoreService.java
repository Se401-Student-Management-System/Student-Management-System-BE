package com.example.studentmanagement.service.teacher;

import com.example.studentmanagement.designpattern.observer.ScorePublisher;
import com.example.studentmanagement.dto.teacher.ScoreRequest;
import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.Subject;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.repository.ScoreRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ScorePublisher scorePublisher;

    public List<Score> enterScores(ScoreRequest scoreRequest, Teacher teacher) {
        if (scoreRequest.getScores() == null || scoreRequest.getScores().isEmpty()) {
            throw new IllegalArgumentException("Không có dữ liệu điểm để cập nhật");
        }

        Subject subject = subjectRepository.findById(scoreRequest.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Môn học không tồn tại"));
        List<Score> savedScores = new ArrayList<>();
        List<ScoreRequest.ScoreDetail> updatedDetails = new ArrayList<>();

        for (ScoreRequest.ScoreDetail scoreDetail : scoreRequest.getScores()) {
            Student student = studentRepository.findById(scoreDetail.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("Học sinh không tồn tại: " + scoreDetail.getStudentId()));

            validateScore(scoreDetail);

            Score score = new Score();
            score.setStudent(student);
            score.setTeacher(teacher);
            score.setSubject(subject);
            score.setSemester(scoreRequest.getSemester());
            score.setAcademicYear(scoreRequest.getAcademicYear());

            Optional<Score> existingScore = scoreRepository.findByStudentIdAndSubjectIdAndSemesterAndAcademicYear(
                    student.getId(), subject.getId(), scoreRequest.getSemester(), scoreRequest.getAcademicYear()
            );
            Score savedScore = existingScore.map(s -> {
                boolean hasChanges = updateScoreIfChanged(s, scoreDetail);
                return hasChanges ? scoreRepository.save(s) : s;
            }).orElseGet(() -> {
                updateScoreIfChanged(score, scoreDetail);
                return scoreRepository.save(score);
            });

            savedScores.add(savedScore);
            updatedDetails.add(scoreDetail); // Lưu chi tiết cập nhật để truyền sang ScorePublisher
        }

        // Gọi publishScoreUpdates với danh sách chi tiết cập nhật
        if (!savedScores.isEmpty()) {
            scorePublisher.publishScoreUpdates(savedScores, teacher, updatedDetails);
        }
        return savedScores;
    }

    private void validateScore(ScoreRequest.ScoreDetail scoreDetail) {
        if (scoreDetail.getScore15m1() != null && (scoreDetail.getScore15m1() < 0 || scoreDetail.getScore15m1() > 10)) {
            throw new IllegalArgumentException("Điểm 15 phút lần 1 không hợp lệ cho học sinh " + scoreDetail.getStudentId());
        }
        if (scoreDetail.getScore15m2() != null && (scoreDetail.getScore15m2() < 0 || scoreDetail.getScore15m2() > 10)) {
            throw new IllegalArgumentException("Điểm 15 phút lần 2 không hợp lệ");
        }
        if (scoreDetail.getScore1h1() != null && (scoreDetail.getScore1h1() < 0 || scoreDetail.getScore1h1() > 10)) {
            throw new IllegalArgumentException("Điểm 1 tiết lần 1 không hợp lệ");
        }
        if (scoreDetail.getScore1h2() != null && (scoreDetail.getScore1h2() < 0 || scoreDetail.getScore1h2() > 10)) {
            throw new IllegalArgumentException("Điểm 1 tiết lần 2 không hợp lệ");
        }
        if (scoreDetail.getFinalScore() != null && (scoreDetail.getFinalScore() < 0 || scoreDetail.getFinalScore() > 10)) {
            throw new IllegalArgumentException("Điểm cuối kỳ không hợp lệ");
        }
    }

    private boolean updateScoreIfChanged(Score score, ScoreRequest.ScoreDetail scoreDetail) {
        boolean hasChanges = false;
        if (scoreDetail.getScore15m1() != null && !scoreDetail.getScore15m1().equals(score.getScore15m1())) {
            score.setScore15m1(scoreDetail.getScore15m1());
            hasChanges = true;
        }
        if (scoreDetail.getScore15m2() != null && !scoreDetail.getScore15m2().equals(score.getScore15m2())) {
            score.setScore15m2(scoreDetail.getScore15m2());
            hasChanges = true;
        }
        if (scoreDetail.getScore1h1() != null && !scoreDetail.getScore1h1().equals(score.getScore1h1())) {
            score.setScore1h1(scoreDetail.getScore1h1());
            hasChanges = true;
        }
        if (scoreDetail.getScore1h2() != null && !scoreDetail.getScore1h2().equals(score.getScore1h2())) {
            score.setScore1h2(scoreDetail.getScore1h2());
            hasChanges = true;
        }
        if (scoreDetail.getFinalScore() != null && !scoreDetail.getFinalScore().equals(score.getFinalScore())) {
            score.setFinalScore(scoreDetail.getFinalScore());
            hasChanges = true;
        }
        return hasChanges;
    }
}