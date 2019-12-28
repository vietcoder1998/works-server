package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.services.excel.import_excel.ImportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportExcelStudentService {
    @Autowired
    private StudentService studentService;
    @Autowired
    private ImportExcelService importExcelService;


}
