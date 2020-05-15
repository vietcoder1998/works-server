package com.worksvn.student_service.modules.internal.controllers;

import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Response;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.services.excel.import_excel.models.ImportErrorLog;
import com.worksvn.common.services.excel.import_excel.models.ImportExcelConfig;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.ImportExcelStudentRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AuthorizationRequired
@PreAuthorize("#oauth2.hasScope('INTERNAL_SERVICE')")
@Api(description = "(Import Excel) Đăng ký tài khoản sinh viên")
@RequestMapping("/api/internal/students/excel/import")
public class ImportExcelStudentRegistrationController extends BaseRESTController {
    @Autowired
    private ImportExcelStudentRegistrationService importExcelStudentRegistrationService;

    @ApiOperation(value = "Import excel tài khoản sinh viên",
            notes = "Username, password, last name, first name, gender, " +
                    "school year start, school year end, major, phone, email")
    @Responses({
            @Response(responseValue = ResponseValue.CONTENT_TYPE_NOT_ALLOWED)
    })
    @PostMapping("/schools/{id}/students/registration")
    public List<ImportErrorLog> importExcel(
            @PathVariable("id") String schoolID,
            @RequestParam(value = "insertNotFoundMajor", defaultValue = "false") boolean insertNotFoundMajor,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute ImportExcelConfig config) throws Exception {
        return importExcelStudentRegistrationService.importExcel(schoolID, file, insertNotFoundMajor, config);
    }

    @PostMapping("/schools/{id}/students/address")
    public List<ImportErrorLog> importExcelAddress(
            @PathVariable("id") String schoolID,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute ImportExcelConfig config) throws Exception {
        return importExcelStudentRegistrationService.importExcelAddress(schoolID, file, config);
    }
}
