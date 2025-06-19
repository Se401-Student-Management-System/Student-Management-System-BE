package com.example.studentmanagement.designpattern.templatemethod;

import com.example.studentmanagement.service.supervisor.StudentBehaviorSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SupervisorBehaviorSummaryReport extends ReportTemplate<List<?>> {

    @Autowired
    private StudentBehaviorSummaryService studentBehaviorSummaryService;

    @Override
    protected void validateParams(Map<String, Object> params) {
        if (!params.containsKey("academicYear") || !params.containsKey("semester") || !params.containsKey("grade")) {
            throw new IllegalArgumentException("Thiếu tham số thống kê hạnh kiểm");
        }
    }

    @Override
    protected List<?> fetchData(Map<String, Object> params) {
        int grade = (int) params.get("grade");
        int semester = (int) params.get("semester");
        String academicYear = (String) params.get("academicYear");
        return studentBehaviorSummaryService.getStudentBehaviorSummary(grade, semester, academicYear);
    }

    @Override
    protected List<?> processData(Object rawData, Map<String, Object> params) {
        return (List<?>) rawData;
    }
}
