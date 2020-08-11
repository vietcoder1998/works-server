package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.student.requests.NewItemPosition;
import com.worksvn.common.modules.student.requests.NewStudentLanguageSkillDto;
import com.worksvn.common.modules.student.responses.StudentLanguageSkillDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentLanguageSkillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@AuthorizationRequired
@Api(description = "Trình độ ngoại ngữ của ứng viên")
@RequestMapping("/api/students/languageSkills")
public class StudentLanguageSkillController extends BaseRESTController {
    @Autowired
    private StudentLanguageSkillService studentLanguageSkillService;

    @ApiOperation(value = "Xem danh sách")
    @Responses({
    })
    @GetMapping
    public PageDto<StudentLanguageSkillDto> getStudentLanguageSkills() throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentLanguageSkillService.getStudentLanguageSkillDtos(studentID);
    }

    @ApiOperation(value = "Thêm mới")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_NOT_FOUND)
    })
    @PostMapping
    public void addStudentLanguageSkill(@RequestBody @Valid NewStudentLanguageSkillDto newLanguageSkill) throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentLanguageSkillService.addStudentLanguageSkill(studentID, newLanguageSkill);
    }

    @ApiOperation(value = "Cập nhật")
    @Responses({
            @Response(responseValue = ResponseValue.LANGUAGE_SKILL_NOT_FOUND)
    })
    @PutMapping("/{id}")
    public void updateStudentLanguageSkill(@PathVariable("id") String languageSkillID,
                                             @RequestBody @Valid NewStudentLanguageSkillDto updateLanguageSkill) throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentLanguageSkillService.updateStudentLanguageSkill(studentID, languageSkillID, updateLanguageSkill);
    }

    @ApiOperation(value = "Cập nhật vị trí")
    @Responses({
    })
    @PutMapping("/position")
    public void updateStudentLanguageSkillPosition(@RequestBody @Valid List<NewItemPosition> newPositions) {
        String studentID = getAuthorizedUser().getId();
        studentLanguageSkillService.updateStudentLanguageSkillPosition(studentID, newPositions);
    }

    @ApiOperation(value = "Xóa danh sách")
    @Responses({
    })
    @DeleteMapping
    public void deleteStudentLanguageSkills(@RequestBody Set<String> languageIDs) {
        String studentID = getAuthorizedUser().getId();
        studentLanguageSkillService.deleteStudentLanguageSkills(studentID, languageIDs);
    }
}
