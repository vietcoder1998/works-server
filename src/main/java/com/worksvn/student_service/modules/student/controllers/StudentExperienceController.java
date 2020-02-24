package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.student.requests.NewStudentExperienceDto;
import com.worksvn.common.modules.student.responses.StudentExperienceDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.models.entities.StudentExperience;
import com.worksvn.student_service.modules.student.services.StudentExperienceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@AuthorizationRequired
@Api(description = "Kinh nghiệm của ứng viên")
@RequestMapping("/api/students/experiences")
public class StudentExperienceController extends BaseRESTController {
    @Autowired
    private StudentExperienceService studentExperienceService;

    @ApiOperation(value = "Xem danh sách")
    @Responses({
    })
    @GetMapping
    public PageDto<StudentExperienceDto> getStudentExperiences(@RequestParam(value = "sortBy", defaultValue = StudentExperience.STARTED_DATE + "," + StudentExperience.FINISHED_DATE) List<String> sortBy,
                                                               @RequestParam(value = "sortType", defaultValue = "desc, asc") List<String> sortType) {
        String studentID = getAuthorizedUser().getId();
        return studentExperienceService.getStudentExperienceDtos(studentID, sortBy, sortType);
    }

    @ApiOperation(value = "Thêm mới")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND)
    })
    @PostMapping
    public void addStudentExperience(@RequestBody @Valid NewStudentExperienceDto newExperience)
            throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentExperienceService.createNewStudentExperience(studentID, newExperience);
    }

    @ApiOperation(value = "Cập nhật")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_EXPERIENCE_NOT_FOUND)
    })
    @PutMapping("/{id}")
    public void updateStudentExperience(@PathVariable("id") String experienceID,
                                        @RequestBody @Valid NewStudentExperienceDto newExperience)
            throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentExperienceService.updateStudentExperience(studentID, experienceID, newExperience);
    }

    @ApiOperation(value = "Xóa")
    @Responses({
    })
    @DeleteMapping("/{id}")
    public void deleteStudentExperience(@PathVariable("id") String experienceID) {
        String studentID = getAuthorizedUser().getId();
        studentExperienceService.deleteStudentEducation(studentID, experienceID);
    }

    @ApiOperation(value = "Xóa danh sách")
    @Responses({
    })
    @DeleteMapping
    public void deleteStudentExperiences(@RequestBody Set<String> experienceIDs) {
        String studentID = getAuthorizedUser().getId();
        studentExperienceService.deleteStudentEducations(studentID, experienceIDs);
    }
}
