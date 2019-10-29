package com.worksvn.student_service.modules.student.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.modules.common.responses.BranchDto;
import com.worksvn.common.modules.common.responses.MajorDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.StudentSchoolMajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AuthorizationRequired
@Api(description = "Nghành/Chuyên nghành đào tạo của trường")
@RequestMapping("/api/students/schools")
public class StudentSchoolMajorController extends BaseRESTController {
    @Autowired
    private StudentSchoolMajorService studentSchoolMajorService;

    @ApiOperation(value = "Xem danh sách các ngành đào tạo")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_SCHOOL_NOT_FOUND)
    })
    @GetMapping("/branches")
    public PageDto<BranchDto> getStudentSchoolBranches(@RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                       @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                       @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                                       @RequestParam(value = "pageSize", defaultValue = "0") int pageSize) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentSchoolMajorService.getStudentSchoolBranches(studentID, sortBy, sortType, pageIndex, pageSize);
    }

    @ApiOperation(value = "Xem danh sách các chuyên ngành đào tạo")
    @Responses({
            @Response(responseValue = ResponseValue.STUDENT_SCHOOL_NOT_FOUND)
    })
    @GetMapping("/majors")
    public PageDto<MajorDto> getStudentSchoolMajors(@RequestParam(value = "branchID", required = false) Integer branchID,
                                                    @RequestParam(value = "sortBy", required = false) List<String> sortBy,
                                                    @RequestParam(value = "sortType", required = false) List<String> sortType,
                                                    @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                                    @RequestParam(value = "pageSize", defaultValue = "0") int pageSize) throws Exception {
        String studentID = getAuthorizedUser().getId();
        return studentSchoolMajorService.getStudentSchoolMajors(studentID, branchID, sortBy, sortType, pageIndex, pageSize);
    }
}
