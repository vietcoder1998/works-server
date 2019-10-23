package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.modules.auth.requests.ResetPasswordDto;
import com.worksvn.student_service.modules.auth.services.UserAccountSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentAccountSecurityService {
    @Autowired
    private UserAccountSecurityService userAccountSecurityService;
    @Autowired
    private StudentService studentService;

    public void resetStudentAccountPassword(String schoolID, String studentID, ResetPasswordDto resetPasswordDto) throws Exception {
        studentService.checkSchoolStudentExist(schoolID, studentID);
        userAccountSecurityService.resetUserPassword(studentID, resetPasswordDto);
    }
}
