package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.school.requests.SchoolEventEmployerFilter;
import com.worksvn.common.modules.school.responses.SchoolEventEmployerDto;
import com.worksvn.student_service.modules.employer.services.SchoolEventEmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentSchoolEventEmployerService {
    @Autowired
    private SchoolEventEmployerService schoolEventEmployerService;
    @Autowired
    private StudentService studentService;

    public PageDto<SchoolEventEmployerDto> querySchoolEventEmployers(String studentID, String eventID,
                                                                     SchoolEventEmployerFilter filter,
                                                                     List<String> sortBy, List<String> sortType,
                                                                     int pageIndex, int pageSize) throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        return schoolEventEmployerService.querySchoolEventEmployers(schoolID, eventID, filter,
                sortBy, sortType, pageIndex, pageSize);
    }

    public SchoolEventEmployerDto getSchoolEventEmployer(String studentID, String eventID, String employerID) throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        return schoolEventEmployerService.getSchoolEventEmployer(schoolID, eventID, employerID);
    }
}
