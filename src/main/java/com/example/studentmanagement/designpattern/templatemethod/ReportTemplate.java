package com.example.studentmanagement.designpattern.templatemethod;

import java.util.Map;

public abstract class ReportTemplate<T> {

    // Template method
    public final T generateReport(Map<String, Object> params) {
        validateParams(params);
        Object rawData = fetchData(params);
        T result = processData(rawData, params);
        afterProcess(result, params);
        return result;
    }

    protected abstract void validateParams(Map<String, Object> params);

    protected abstract Object fetchData(Map<String, Object> params);

    protected abstract T processData(Object rawData, Map<String, Object> params);

    protected void afterProcess(T result, Map<String, Object> params) {
        // Hook method, có thể override nếu cần
    }
}
