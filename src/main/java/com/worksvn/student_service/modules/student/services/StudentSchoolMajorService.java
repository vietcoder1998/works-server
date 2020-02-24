package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.responses.BranchDto;
import com.worksvn.common.modules.common.responses.MajorDto;
import com.worksvn.student_service.modules.school.services.SchoolEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentSchoolMajorService {
    @Autowired
    private SchoolEducationService schoolMajorService;
    @Autowired
    private StudentService studentService;

    public PageDto<BranchDto> getStudentSchoolBranches(String studentID,
                                                       List<String> sortBy, List<String> sortType,
                                                       Integer pageIndex, Integer pageSize) throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        return schoolMajorService.getSchoolBranches(schoolID, sortBy, sortType, pageIndex, pageSize);
    }

    public PageDto<MajorDto> getStudentSchoolMajors(String studentID, Integer branchID,
                                                    List<String> sortBy, List<String> sortType,
                                                    Integer pageIndex, Integer pageSize) throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        return schoolMajorService.getSchoolMajors(schoolID, branchID, null, null,
                sortBy, sortType, pageIndex, pageSize);
    }
}
