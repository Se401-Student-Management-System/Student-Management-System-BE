package com.example.studentmanagement.converter;

import com.example.studentmanagement.enums.TeacherPosition;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TeacherPositionConverter implements AttributeConverter<TeacherPosition, String> {

    @Override
    public String convertToDatabaseColumn(TeacherPosition position) {
        return position != null ? position.getLabel() : null;
    }

    @Override
    public TeacherPosition convertToEntityAttribute(String dbValue) {
        return dbValue != null ? TeacherPosition.fromLabel(dbValue) : null;
    }
}

