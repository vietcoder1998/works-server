package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.student_service.modules.student.repositories.StudentUnlockedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentUnlockService {
    @Autowired
    private StudentUnlockedRepository studentUnlockedRepository;

    public boolean checkStudentUnlockedByEmployer(String studentID, String employerID) throws ResponseException {
        return studentUnlockedRepository.existsByStudent_IdAndEmployerID(studentID, employerID);
    }
}
