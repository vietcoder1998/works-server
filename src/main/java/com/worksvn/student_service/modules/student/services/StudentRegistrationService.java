package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.components.communication.ISRestCommunicator;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.auth.responses.FBUserInfoDto;
import com.worksvn.common.modules.common.responses.RegionDto;
import com.worksvn.common.modules.student.requests.NewStudentRegistrationDto;
import com.worksvn.common.modules.student.requests.StudentFacebookRegistrationDto;
import com.worksvn.common.utils.core.FileUtils;
import com.worksvn.common.utils.core.TextUtils;
import com.worksvn.student_service.components.communication.APIs;
import com.worksvn.student_service.modules.auth.services.UserService;
import com.worksvn.student_service.modules.school.services.SchoolService;
import com.worksvn.student_service.modules.student.models.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class StudentRegistrationService {
    @Autowired
    private UserService userService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ISRestCommunicator restCommunicator;

    @Transactional(rollbackFor = Exception.class)
    public void registerNewStudent(String schoolID, NewStudentRegistrationDto registration,
                                   boolean activated) throws Exception {
        String username;
        String userID;
        String email = registration.getEmail();
        if (registration.getUsername() != null && !registration.getUsername().isEmpty()) {
            String schoolShortName = schoolService.getSchoolShortName(schoolID);
            username = schoolShortName.toLowerCase() + registration.getUsername();
            userID = userService.registerNewUserByUsername(username, registration.getPassword(), email, activated);
        } else if (email != null && !email.isEmpty()) {
            schoolService.checkSchoolExist(schoolID);
            userID = userService.registerNewUserByEmail(email, registration.getPassword(), activated);
        } else {
            throw new ResponseException(ResponseValue.INVALID_USERNAME);
        }
        if (studentService.isStudentExist(userID)) {
            throw new ResponseException(ResponseValue.STUDENT_EXISTS);
        }
        studentService.saveStudent(new Student(userID, schoolID, registration));
    }

    public void registerNewStudentByFacebook(String schoolID, StudentFacebookRegistrationDto registration) throws Exception {
        schoolService.checkSchoolExist(schoolID);
        String userID = userService.registerNewUserByFacebook(registration.getFbAccessToken());
        if (studentService.isStudentExist(userID)) {
            throw new ResponseException(ResponseValue.STUDENT_EXISTS);
        }
        Student student = new Student(userID, schoolID, registration);
        copyFacebookInfo(student, registration.getFbAccessToken());
        studentService.saveStudent(student);
    }

    private void copyFacebookInfo(Student student, String fbAccessToken) throws Exception {
        FBUserInfoDto fbUserInfo = restCommunicator
                .exchangeForSuccess(APIs.FACEBOOK_getFBUserInfo(fbAccessToken));

        if (!TextUtils.isEmpty(fbUserInfo.getAvatarUrl())) {
            byte[] imageBytes = FileUtils.readFromUrl(fbUserInfo.getAvatarUrl());
            String avatarUrl = studentService.uploadStudentAvatar(student.getId(), imageBytes, "image/png");
            student.setAvatarUrl(avatarUrl);
        }

        if (fbUserInfo.getBirthday() > 0) {
            Date birthday = new Date(fbUserInfo.getBirthday());
            student.setBirthday(birthday);
        }

        RegionDto region = fbUserInfo.getRegion();
        if (region != null) {
            student.setRegionID(region.getId());
        }
    }
}
