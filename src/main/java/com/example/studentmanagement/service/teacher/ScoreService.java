package com.example.studentmanagement.service.teacher;

import com.example.studentmanagement.designpattern.observer.ScorePublisher;
import com.example.studentmanagement.dto.teacher.ScoreInputDetailDTO;
import com.example.studentmanagement.dto.teacher.ScoreInputInfo;
import com.example.studentmanagement.dto.teacher.ScoreRequest;
import com.example.studentmanagement.model.Assignment;
import com.example.studentmanagement.model.Class;
import com.example.studentmanagement.model.Score;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.StudentClass;
import com.example.studentmanagement.model.Subject;
import com.example.studentmanagement.model.Teacher;
import com.example.studentmanagement.repository.AssignmentRepository;
import com.example.studentmanagement.repository.ClassRepository;
import com.example.studentmanagement.repository.ScoreRepository;
import com.example.studentmanagement.repository.StudentClassRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.SubjectRepository;
import com.example.studentmanagement.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private TeacherRepository teacherRepository;

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
                    student.getId(), subject.getId(), scoreRequest.getSemester(), scoreRequest.getAcademicYear());
            Score savedScore = existingScore.map(s -> {
                boolean hasChanges = updateScoreIfChanged(s, scoreDetail);
                return hasChanges ? scoreRepository.save(s) : s;
            }).orElseGet(() -> {
                updateScoreIfChanged(score, scoreDetail);
                return scoreRepository.save(score);
            });

            savedScores.add(savedScore);
            updatedDetails.add(scoreDetail);
        }

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

    public List<Object[]> getSubjectsByTeacher(String teacherId, String year, int semester) {
        return assignmentRepository.findSubjectsByTeacher(teacherId, year, semester);
    }

    public Map<String, Object> getScoreInputInfo(String teacherId, Integer semester, String year) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Giáo viên không tồn tại: " + teacherId));

        // Chỉ lấy các lớp mà giáo viên được phân công giảng dạy
        List<Assignment> assignments = assignmentRepository.findByTeacherIdAndSemesterAndAcademicYear(teacherId, semester, year)
                .stream()
                .filter(a -> a.getRole().name().equals("Subject_Teacher"))
                .collect(Collectors.toList());

        if (assignments.isEmpty()) {
            return Map.of(
                    "selectedYear", year != null ? year : "",
                    "selectedSemester", semester != null ? "Học kỳ " + semester : "",
                    "enterList", new ArrayList<>()
            );
        }

        // Sử dụng Set để loại bỏ trùng lặp lớp dựa trên classId
        Set<Integer> assignedClassIds = assignments.stream()
                .map(Assignment::getClazz) // Lấy entity Class
                .map(Class::getId) // Lấy id của lớp
                .collect(Collectors.toSet());

        List<ScoreInputInfo> enterList = new ArrayList<>();
        for (Integer classId : assignedClassIds) {
            // Lấy tất cả môn học được phân công cho lớp này
            List<Assignment> classAssignments = assignments.stream()
                    .filter(a -> a.getClazz().getId().equals(classId))
                    .collect(Collectors.toList());

            for (Assignment assignment : classAssignments) {
                Class clazz = assignment.getClazz();
                Subject subject = assignment.getSubject();

                ScoreInputInfo info = new ScoreInputInfo();
                info.setTenLop(clazz.getClassName());
                info.setTenMH(subject.getSubjectName());

                String[] examNames = {"score15m1", "score15m2", "score1h1", "score1h2", "finalScore"};
                Map<String, String> status = checkScoreStatus(assignment, semester, year, examNames);

                info.setDiem15P_1(status.get("score15m1"));
                info.setDiem15P_2(status.get("score15m2"));
                info.setDiem1Tiet_1(status.get("score1h1"));
                info.setDiem1Tiet_2(status.get("score1h2"));
                info.setDiemCK(status.get("finalScore"));

                enterList.add(info);
            }
        }

        return Map.of(
                "selectedYear", year,
                "selectedSemester", "Học kỳ " + semester,
                "enterList", enterList
        );
    }

    private Map<String, String> checkScoreStatus(Assignment assignment, Integer semester, String academicYear, String[] examNames) {
        Map<String, String> status = new HashMap<>();

        Class clazz = assignment.getClazz();
        Subject subject = assignment.getSubject();

        List<StudentClass> studentClasses = studentClassRepository.findByClassIdAndAcademicYear(clazz.getId(), academicYear);
        if (studentClasses.isEmpty()) {
            Arrays.stream(examNames).forEach(name -> status.put(name, ""));
            return status;
        }

        for (String examName : examNames) {
            long missingScores = studentClasses.stream()
                    .map(StudentClass::getStudent)
                    .map(Student::getId)
                    .map(studentId -> scoreRepository.findByStudentIdAndSubjectIdAndSemesterAndAcademicYear(
                    studentId, subject.getId(), semester, academicYear))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(score -> {
                        switch (examName) {
                            case "score15m1":
                                return score.getScore15m1() == null;
                            case "score15m2":
                                return score.getScore15m2() == null;
                            case "score1h1":
                                return score.getScore1h1() == null;
                            case "score1h2":
                                return score.getScore1h2() == null;
                            case "finalScore":
                                return score.getFinalScore() == null;
                            default:
                                return false;
                        }
                    })
                    .count();

            status.put(examName, missingScores == 0 ? "Đã hoàn thành" : "");
        }

        return status;
    }

    public List<ScoreInputDetailDTO> getScoreInputDetail(String teacherId, String className, String subjectName,
            Integer semester, String academicYear) {
        List<Score> scores = scoreRepository.findScoreInputDetail(teacherId, className, subjectName, semester, academicYear);

        List<ScoreInputDetailDTO> result = new ArrayList<>();
        for (Score score : scores) {
            ScoreInputDetailDTO dto = new ScoreInputDetailDTO();
            dto.setStudentId(score.getStudent().getId());
            dto.setFullName(score.getStudent().getAccount().getFullName());
            dto.setScore15m1(score.getScore15m1());
            dto.setScore15m2(score.getScore15m2());
            dto.setScore1h1(score.getScore1h1());
            dto.setScore1h2(score.getScore1h2());
            dto.setFinalScore(score.getFinalScore());
            dto.setAverageScore(calculateAverageScore(score));
            result.add(dto);
        }

        return result;
    }

    private Float calculateAverageScore(Score score) {
        List<Float> validScores = new ArrayList<>();
        if (score.getScore15m1() != null) {
            validScores.add(score.getScore15m1());
        }
        if (score.getScore15m2() != null) {
            validScores.add(score.getScore15m2());
        }
        if (score.getScore1h1() != null) {
            validScores.add(score.getScore1h1());
        }
        if (score.getScore1h2() != null) {
            validScores.add(score.getScore1h2());
        }
        if (score.getFinalScore() != null) {
            validScores.add(score.getFinalScore());
        }

        if (validScores.isEmpty()) {
            return null;
        }
        float sum = 0;
        for (Float s : validScores) {
            sum += s;
        }
        return sum / validScores.size();
    }
}
