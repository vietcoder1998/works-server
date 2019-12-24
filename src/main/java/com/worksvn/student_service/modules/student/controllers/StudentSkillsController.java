package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.responses.SkillDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentSkillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AuthorizationRequired
@Api(description = "Kỹ năng của ứng viên")
@RequestMapping("/api/students/skills")
public class StudentSkillsController extends BaseRESTController {
    @Autowired
    private StudentSkillService studentSkillService;

    @ApiOperation(value = "Xem danh sách")
    @Responses({
    })
    @GetMapping
    public PageDto<SkillDto> getStudentSkills(@RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                              @RequestParam(value = "sortType", required = false) List<String> sortType) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentSkillService.getStudentSkillDtos(studentID, sortBy, sortType);
    }

    @ApiOperation(value = "Cập nhật danh sách")
    @Responses({
    })
    @PutMapping
    public void updateStudentSkills(@RequestBody Set<Integer> skillIDs) throws Exception {
        String studentID = getAuthorizedUser().getId();
        studentSkillService.updateStudentSkills(studentID, skillIDs);
    }
}
