package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.student.requests.NewStudentRegistrationDto;
import com.worksvn.student_service.modules.auth.services.UserService;
import com.worksvn.student_service.modules.school.services.SchoolService;
import com.worksvn.student_service.modules.student.models.entities.Student;
import com.worksvn.student_service.modules.student.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentRegistrationService {
    @Autowired
    private UserService userService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private StudentRepository studentRepository;

    @Transactional(rollbackFor = Exception.class)
    public void registerNewStudent(String schoolID, NewStudentRegistrationDto registrationDto) throws Exception {
        String schoolShortName = schoolService.getSchoolShortName(schoolID);
        String username = schoolShortName + registrationDto.getUsername();
        String userID = userService.registerNewUserByUsername(username, registrationDto.getPassword());
        if (studentRepository.existsById(userID)) {
            throw new ResponseException(ResponseValue.STUDENT_EXISTS);
        }
        studentRepository.save(new Student(userID, schoolID, registrationDto));
    }
}
