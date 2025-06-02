package com.example.studentmanagement.converter;

import com.example.studentmanagement.enums.TeachingStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TeachingStatusConverter implements AttributeConverter<TeachingStatus, String> {

    @Override
    public String convertToDatabaseColumn(TeachingStatus status) {
        return status != null ? status.getLabel() : null;
    }

    @Override
    public TeachingStatus convertToEntityAttribute(String dbValue) {
        return dbValue != null ? TeachingStatus.fromLabel(dbValue) : null;
    }
}