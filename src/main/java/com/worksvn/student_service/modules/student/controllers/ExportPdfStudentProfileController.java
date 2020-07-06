package com.worksvn.student_service.modules.student.controllers;

import com.itextpdf.text.DocumentException;
import com.worksvn.common.annotations.auth.AuthorizationRequired;
import com.worksvn.common.annotations.swagger.Responses;
import com.worksvn.common.modules.common.responses.DownloadUrlDto;
import com.worksvn.student_service.base.controllers.BaseRESTController;
import com.worksvn.student_service.modules.student.services.ExportPdfStudentProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AuthorizationRequired
@Api(description = "Nhận xét bài viết hệ thống cho ứng viên")
@RequestMapping("/api/students/profile")
public class ExportPdfStudentProfileController extends BaseRESTController {
    @Autowired
    private ExportPdfStudentProfileService exportPdfStudentProfileService;

    @ApiOperation(value = "Xuất Pdf hồ sơ ứng viên")
    @Responses({
    })
    @PostMapping("/export/pdf")
    public DownloadUrlDto exportPdfStudentProfile(@RequestParam("template") String template) throws IOException, DocumentException {
        String studentID = getAuthorizedUser().getId();
        return exportPdfStudentProfileService.exportPdfStudentProfile(studentID, template);
    }
}
