package com.example.studentmanagement.designpattern.observer;

import com.example.studentmanagement.dto.teacher.ScoreRequest;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScorePublisher {

    private List<Subscriber> subscribers = new ArrayList<>();

    @Autowired
    public ScorePublisher(List<Subscriber> subscribers) {
        this.subscribers.addAll(subscribers);
    }

    public void addSubscriber(Subscriber subscriber) {
        if (subscriber != null && !subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifyScoreUpdate(Score score, Account studentAccount, ScoreRequest.ScoreDetail updatedDetail) {
        String message = formatScoreMessage(score, updatedDetail);
        if (!message.isEmpty()) {
            for (Subscriber subscriber : subscribers) {
                subscriber.update("SCORE_UPDATED", studentAccount, message);
            }
        }
    }

    public void notifyBatchUpdateComplete(List<Score> scores, Account teacherAccount) {
        if (scores.isEmpty()) return;
        String message = "Đã cập nhật điểm cho " + scores.size() + " học sinh trong học kỳ " +
                scores.get(0).getSemester() + ", năm học " + scores.get(0).getAcademicYear() + ".\n" +
                "Ngày cập nhật: " + LocalDate.now() + "\n";
        for (Subscriber subscriber : subscribers) {
            subscriber.update("SCORE_BATCH_UPDATED", teacherAccount, message);
        }
    }

    public void publishScoreUpdates(List<Score> scores, Teacher teacher, List<ScoreRequest.ScoreDetail> updatedDetails) {
        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            ScoreRequest.ScoreDetail detail = updatedDetails.get(i);
            notifyScoreUpdate(score, score.getStudent().getAccount(), detail);
        }
        if (!scores.isEmpty()) {
            notifyBatchUpdateComplete(scores, teacher.getAccount());
        }
    }

    private String formatScoreMessage(Score score, ScoreRequest.ScoreDetail updatedDetail) {
        StringBuilder message = new StringBuilder();
        message.append("Mã học sinh: ").append(score.getStudent().getId()).append("\n");
        message.append("Họ và tên: ").append(score.getStudent().getAccount().getFullName()).append("\n");
        message.append("Môn học: ").append(score.getSubject().getSubjectName()).append(" (Mã môn: ").append(score.getSubject().getId()).append(")\n");

        if (updatedDetail.getScore15m1() != null) {
            message.append("- Điểm 15 phút lần 1: ").append(updatedDetail.getScore15m1()).append("\n");
        }
        if (updatedDetail.getScore15m2() != null) {
            message.append("- Điểm 15 phút lần 2: ").append(updatedDetail.getScore15m2()).append("\n");
        }
        if (updatedDetail.getScore1h1() != null) {
            message.append("- Điểm 1 tiết lần 1: ").append(updatedDetail.getScore1h1()).append("\n");
        }
        if (updatedDetail.getScore1h2() != null) {
            message.append("- Điểm 1 tiết lần 2: ").append(updatedDetail.getScore1h2()).append("\n");
        }
        if (updatedDetail.getFinalScore() != null) {
            message.append("- Điểm cuối kỳ: ").append(updatedDetail.getFinalScore()).append("\n");
        }
        message.append("Giáo viên: ").append(score.getTeacher().getAccount().getFullName()).append("\n");
        message.append("Ngày cập nhật: ").append(LocalDate.now()).append("\n");

        return message.toString().trim(); // Loại bỏ dòng trống cuối cùng
    }
}