package com.example.studentmanagement.designpattern.templatemethod;

import com.example.studentmanagement.service.director.StudentConductStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SupervisorConductReport extends ReportTemplate<Object> {

    @Autowired
    private StudentConductStatics studentConductStatics;

    @Override
    protected void validateParams(Map<String, Object> params) {
        if (!params.containsKey("academicYear")) {
            throw new IllegalArgumentException("Thiếu tham số năm học");
        }
    }

    @Override
    protected Object fetchData(Map<String, Object> params) {
        int grade = (int) params.get("grade");
        int semester = (int) params.get("semester");
        String academicYear = (String) params.get("academicYear");
        // Lấy dữ liệu tổng hợp hạnh kiểm theo khối
        return studentConductStatics.getConductOverview(grade, semester, academicYear);
    }

    @Override
    protected Object processData(Object rawData, Map<String, Object> params) {
        // Có thể xử lý thêm nếu cần, ở đây trả về luôn
        return rawData;
    }
}
