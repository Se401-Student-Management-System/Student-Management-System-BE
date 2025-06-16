package com.example.studentmanagement.designpattern.decorator;

import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasicEvaluationProcessor implements EvaluationComponent {

    private final ScoreRepository scoreRepository;

    @Autowired
    public BasicEvaluationProcessor(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    public void addComment(Score score, Teacher teacher, String comment) {
        // Kiểm tra quyền của giáo viên
        teacher.checkIfTeaching(score.getStudent());

        // Thêm nhận xét vào Score
        if (comment != null && !comment.trim().isEmpty()) {
            score.setComments(comment.trim());
        } else {
            throw new IllegalArgumentException("Nhận xét không được để trống");
        }

        // Lưu vào cơ sở dữ liệu
        scoreRepository.save(score);
    }
}