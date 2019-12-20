package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.student.responses.StudentContactInfo;
import com.worksvn.student_service.modules.employer.services.EmployerService;
import com.worksvn.student_service.modules.student.models.entities.Student;
import com.worksvn.student_service.modules.student.models.entities.StudentUnlocked;
import com.worksvn.student_service.modules.student.repositories.StudentUnlockedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentUnlockService {
    @Autowired
    private StudentUnlockedRepository studentUnlockedRepository;

    @Autowired
    private StudentService studentService;
    @Autowired
    private EmployerService employerService;

    public boolean checkStudentUnlockedByEmployer(String studentID, String employerID) throws ResponseException {
        return studentUnlockedRepository.existsByStudent_IdAndEmployerID(studentID, employerID);
    }

    public StudentContactInfo unlockStudentByEmployer(String studentID, String employerID) throws Exception {
        if (studentUnlockedRepository.existsByStudent_IdAndEmployerID(studentID, employerID)) {
            throw new ResponseException(ResponseValue.STUDENT_UNLOCKED);
        }
        employerService.checkEmployerExist(employerID);
        Student student = studentService.getStudent(studentID);
        StudentUnlocked studentUnlocked = new StudentUnlocked(student, employerID);
        studentUnlockedRepository.save(studentUnlocked);
        return new StudentContactInfo(student.getEmail(), student.getPhone());
    }
}
