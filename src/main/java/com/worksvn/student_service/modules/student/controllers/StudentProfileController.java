package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.common.responses.AvatarUrlDto;
import com.worksvn.common.modules.common.responses.CoverUrlDto;
import com.worksvn.common.modules.common.responses.IdentityCardImageDto;
import com.worksvn.common.modules.student.requests.UpdateStudentInfoDto;
import com.worksvn.common.modules.student.responses.StudentInfoDto;
import com.worksvn.common.modules.student.responses.StudentNavigationDto;
import com.worksvn.common.modules.student.responses.StudentProfileDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@AuthorizationRequired
@Api(description = "Hồ sơ sinh viên")
@RequestMapping("/api/students")
public class StudentProfileController extends BaseRESTController {
    @Autowired
    private StudentService studentService;

    @ApiOperation(value = "Xem thông tin cá nhân")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND)
    })
    @GetMapping("/personalInfo")
    public StudentInfoDto getCandidatePersonalInfo() throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentService.getStudentInfo(studentID);
    }

    @ApiOperation(value = "Xem thông tin tóm lược (dùng cho navigation drawer trên mobile app)")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND)
    })
    @GetMapping("/headerProfile")
    public StudentNavigationDto getCandidateNavigation() throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentService.getStudentNavigation(studentID);
    }

    @ApiOperation(value = "Xem hồ sơ đầy đủ")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND)
    })
    @GetMapping("/profile")
    public StudentProfileDto getProfile() throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentService.getStudentProfile(studentID, null, null);
    }

    @ApiOperation(value = "Cập nhật thông tin cá nhân",
            notes = "Link [PUBLIC][common][LocationController] Truy vấn tỉnh/thành phố và địa chỉ")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND)
    })
    @PutMapping("/personalInfo")
    public void updateStudentInfo(@RequestBody @Valid UpdateStudentInfoDto updateInfo) throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentService.updateStudentInfo(studentID, updateInfo);
    }

    @ApiOperation(value = "Cập nhật ảnh avatar")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND),
            @Response(responseValue = ResponseValue.CONTENT_TYPE_NOT_ALLOWED)
    })
    @PutMapping("/avatar")
    public AvatarUrlDto updateCandidateAvatar(@RequestParam(value = "avatar", required = false) MultipartFile avatarImage)
            throws IOException, ResponseException {
        String candidateID = getAuthorizedUser().getId();
        return studentService.updateStudentAvatar(candidateID, avatarImage);
    }

    @ApiOperation(value = "Cập nhật ảnh cover")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND),
            @Response(responseValue = ResponseValue.CONTENT_TYPE_NOT_ALLOWED)
    })
    @PutMapping("/cover")
    public CoverUrlDto updateCandidateCover(@RequestParam(value = "cover", required = false) MultipartFile coverImage)
            throws IOException, ResponseException {
        String candidateID = getAuthorizedUser().getId();
        return studentService.updateStudentCover(candidateID, coverImage);
    }

    @ApiOperation(value = "Cập nhật ảnh thẻ (mặt trước và sau)")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND),
            @Response(responseValue = ResponseValue.CONTENT_TYPE_NOT_ALLOWED)
    })
    @PutMapping("/cardImages")
    public IdentityCardImageDto updateCandidateCardImage(@RequestParam(value = "front", required = false) MultipartFile frontImage,
                                                         @RequestParam(value = "back", required = false) MultipartFile backImage)
            throws IOException, ResponseException {
        String studentID = getAuthorizedUser().getId();
        return studentService.updateStudentIdentityCardImageUrl(studentID, frontImage, backImage);
    }

    @ApiOperation(value = "Cập nhật trạng thái tìm việc")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND)
    })
    @PutMapping("/lookingForJob/{state}")
    public void updateCandidateLookingForJob(@PathVariable("state") boolean state) throws ResponseException {
        String studentID = getAuthorizedUser().getId();
        studentService.updateStudentLookingForJob(studentID, state);
    }

    @ApiOperation(value = "Cập nhật mô tả")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND)
    })
    @PutMapping("/description")
    public void updateCandidateDescription(@RequestBody String description) throws ResponseException {
        String studentID = getAuthorizedUser().getId();
        studentService.updateStudentDescription(studentID, description);
    }
}
