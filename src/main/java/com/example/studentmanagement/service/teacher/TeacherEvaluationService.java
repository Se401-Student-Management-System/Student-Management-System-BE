package com.example.studentmanagement.service.teacher;

import com.example.studentmanagement.dto.teacher.StudentEvaluationDTO;
import com.example.studentmanagement.model.*;
import com.example.studentmanagement.repository.AssignmentRepository;
import com.example.studentmanagement.repository.ScoreRepository;
import com.example.studentmanagement.repository.StudentClassRepository;
import com.example.studentmanagement.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherEvaluationService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public Map<String, Object> getStudentListForEvaluation(String selectedYear, String selectedSemester, String selectedClass) {
        Teacher teacher = getCurrentTeacher();
        String teacherId = teacher.getId();

        // Lấy danh sách năm học được phân công
        List<String> yearList = assignmentRepository.findByTeacherId(teacherId).stream()
                .map(Assignment::getAcademicYear)
                .distinct()
                .sorted(Collections.reverseOrder()) // Sắp xếp giảm dần
                .collect(Collectors.toList());
        if (yearList.isEmpty()) {
            throw new IllegalArgumentException("Bạn chưa được phân công lớp giảng dạy nào.");
        }

        // Mặc định năm học nếu không có
        selectedYear = (selectedYear != null && !selectedYear.isEmpty()) ? selectedYear : yearList.get(0);
        Integer semesterNumber = parseSemester(selectedSemester);

        // Lấy danh sách lớp học dựa trên giáo viên, năm học và học kỳ
        List<Assignment> assignments = assignmentRepository.findByTeacherIdAndSemesterAndAcademicYear(teacherId, semesterNumber, selectedYear);
        List<String> classList = assignments.stream()
                .filter(a -> a.getRole().name().equals("Subject_Teacher"))
                .map(a -> a.getClazz().getClassName())
                .distinct()
                .collect(Collectors.toList());
        if (classList.isEmpty()) {
            throw new IllegalArgumentException("Bạn chưa được phân công dạy trong học kỳ " + semesterNumber + " năm học " + selectedYear + ".");
        }

        // Mặc định lớp học nếu không có
        selectedClass = (selectedClass != null && !selectedClass.isEmpty()) ? selectedClass : classList.get(0);

        // Lấy danh sách học sinh và điểm số với phương thức mới
        List<StudentEvaluationDTO> studentList = getTeacherComments(teacherId, selectedClass, semesterNumber, selectedYear);

        return createResponse(selectedYear, selectedSemester, selectedClass, studentList);
    }

    private Integer parseSemester(String selectedSemester) {
        return (selectedSemester != null && !selectedSemester.isEmpty())
                ? Integer.parseInt(selectedSemester.replaceAll("[^0-9]", ""))
                : 1; // Mặc định học kỳ 1
    }

    private List<StudentEvaluationDTO> getTeacherComments(String teacherId, String className, Integer semester, String academicYear) {
        List<Score> scores = scoreRepository.findScoresForEvaluation(teacherId, className, semester, academicYear);
        return scores.stream().map(score -> {
            StudentEvaluationDTO dto = new StudentEvaluationDTO();
            dto.setStudentId(score.getStudent().getId());
            dto.setFullName(score.getStudent().getAccount().getFullName());
            dto.setSubjectName(score.getSubject().getSubjectName());
            dto.setScore15m1(score.getScore15m1());
            dto.setScore15m2(score.getScore15m2());
            dto.setScore1h1(score.getScore1h1());
            dto.setScore1h2(score.getScore1h2());
            dto.setFinalScore(score.getFinalScore());
            dto.setComments(score.getComments());
            dto.setAverageScore(calculateAverageScore(score));
            return dto;
        }).collect(Collectors.toList());
    }

    private Float calculateAverageScore(Score score) {
        List<Float> validScores = new ArrayList<>();
        if (score.getScore15m1() != null) validScores.add(score.getScore15m1());
        if (score.getScore15m2() != null) validScores.add(score.getScore15m2());
        if (score.getScore1h1() != null) validScores.add(score.getScore1h1());
        if (score.getScore1h2() != null) validScores.add(score.getScore1h2());
        if (score.getFinalScore() != null) validScores.add(score.getFinalScore());

        return validScores.isEmpty() ? null : validScores.stream().reduce(0f, Float::sum) / validScores.size();
    }

    private Map<String, Object> createResponse(String selectedYear, String selectedSemester, String selectedClass, List<StudentEvaluationDTO> studentList) {
        Map<String, Object> response = new HashMap<>();
        response.put("selectedYear", selectedYear != null ? selectedYear : "");
        response.put("selectedSemester", selectedSemester != null ? selectedSemester : "Học kỳ 1");
        response.put("selectedClass", selectedClass != null ? selectedClass : "");
        response.put("studentList", studentList);
        return response;
    }

    private Teacher getCurrentTeacher() {
        // Giả định lấy từ session hoặc token (mặc định "GV001" như ví dụ)
        Optional<Teacher> teacher = teacherRepository.findById("GV001");
        return teacher.orElseThrow(() -> new IllegalArgumentException("Giáo viên GV001 không tồn tại trong database"));
    }
}