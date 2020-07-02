package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.modules.common.responses.DownloadUrlDto;
import com.worksvn.common.services.thymeleaf.HtmlTemplateBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ExportPdfStudentProfileService {
    @Autowired
    private HtmlTemplateBindingService htmlTemplateBindingService;

//    public DownloadUrlDto exportPdfStudentProfile() {
//        String result = htmlTemplateBindingService.bindTemplate("cv_template_01", new HashMap<>());
//it s
//    }
}
