package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.student_service.modules.student.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public void checkSchoolStudentExist(String schoolID, String studentID) throws ResponseException {
        if (!studentRepository.existsByIdAndSchoolID(studentID, schoolID)) {
            throw new ResponseException(ResponseValue.SCHOOL_STUDENT_NOT_FOUND);
        }
    }
}
