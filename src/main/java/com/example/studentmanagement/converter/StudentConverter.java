package com.example.studentmanagement.converter;

import com.example.studentmanagement.dto.student.StudentDTO;
import com.example.studentmanagement.dto.student.UpdateStudentRequest;
import com.example.studentmanagement.enums.StudentStatus;
import com.example.studentmanagement.enums.StudyStatus;
import com.example.studentmanagement.model.Account;
import com.example.studentmanagement.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentConverter {
    public StudentDTO toDto(Student entity) {
        StudentDTO dto = new StudentDTO();
        dto.setId(entity.getId());
        if (entity.getAccount() != null) {
            Account account = entity.getAccount();
            dto.setUsername(account.getUsername());
            dto.setFullName(account.getFullName());
            dto.setEmail(account.getEmail());
            dto.setPhoneNumber(account.getPhoneNumber());
            dto.setAddress(account.getAddress());
            dto.setGender(account.getGender() != null ? account.getGender().name() : null);
            dto.setBirthDate(account.getBirthDate());
            if (account.getRole() != null) {
                dto.setRoleName(account.getRole().getRoleName());
            }
        }
        dto.setEthnicity(entity.getEthnicity());
        dto.setBirthPlace(entity.getBirthPlace());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);

        return dto;
    }

    public Student toEntity(StudentDTO dto) {
        Student entity = new Student();
        entity.setId(dto.getId());
        entity.setEthnicity(dto.getEthnicity());
        entity.setBirthPlace(dto.getBirthPlace());
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            try {
                entity.setStatus(StudyStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status provided in DTO: '" + dto.getStatus()
                        + "'. Valid statuses are: ACTIVE, PENDING, WARNING, INACTIVE.", e);
            }
        } else {
            entity.setStatus(StudyStatus.PENDING);
        }
        return entity;
    }

    public Student toEntity(StudentDTO dto, Student existingEntity) {
        if (dto.getFullName() != null && existingEntity.getAccount() != null) {
            existingEntity.getAccount().setFullName(dto.getFullName());
        }
        if (dto.getEmail() != null && existingEntity.getAccount() != null) {
            existingEntity.getAccount().setEmail(dto.getEmail());
        }
        if (dto.getEthnicity() != null) {
            existingEntity.setEthnicity(dto.getEthnicity());
        }
        if (dto.getBirthPlace() != null) {
            existingEntity.setBirthPlace(dto.getBirthPlace());
        }
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            try {
                existingEntity.setStatus(StudyStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status provided in DTO: '" + dto.getStatus()
                        + "'. Valid statuses are: ACTIVE, PENDING, WARNING, INACTIVE.", e);
            }
        }
        return existingEntity;
    }

    public Student toEntity(UpdateStudentRequest request, Student existingEntity) {// Cập nhật các trường Account (nếu
                                                                                   // request có và account tồn tại)
        if (existingEntity.getAccount() != null) {
            if (request.getFullName() != null) {
                existingEntity.getAccount().setFullName(request.getFullName());
            }
            if (request.getEmail() != null) {
                existingEntity.getAccount().setEmail(request.getEmail());
            }
            if (request.getPhoneNumber() != null) {
                existingEntity.getAccount().setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getAddress() != null) {
                existingEntity.getAccount().setAddress(request.getAddress());
            }
            if (request.getGender() != null) {
                try {
                    existingEntity.getAccount().setGender(Account.Gender.valueOf(request.getGender()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "Invalid gender value: " + request.getGender() + ". Must be 'Nam' or 'Nữ'.", e);
                }
            }
            if (request.getBirthDate() != null) {
                existingEntity.getAccount().setBirthDate(request.getBirthDate());
            }
        }

        // Cập nhật các trường Student
        if (request.getEthnicity() != null) {
            existingEntity.setEthnicity(request.getEthnicity());
        }
        if (request.getBirthPlace() != null) {
            existingEntity.setBirthPlace(request.getBirthPlace());
        }

        return existingEntity;
    }
}
