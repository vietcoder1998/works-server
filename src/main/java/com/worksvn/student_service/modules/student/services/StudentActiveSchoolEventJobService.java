package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.StringConstants;
import com.worksvn.common.modules.common.responses.LatLon;
import com.worksvn.common.modules.employer.requests.ActiveJobFilter;
import com.worksvn.common.modules.employer.requests.ClientActiveJobFilter;
import com.worksvn.common.modules.employer.responses.JobDto;
import com.worksvn.common.modules.employer.responses.JobPreview;
import com.worksvn.student_service.modules.school.services.ActiveEmployerSchoolEventJobService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentActiveSchoolEventJobService {
    @Autowired
    private ActiveEmployerSchoolEventJobService activeEmployerSchoolEventJobService;
    @Autowired
    private StudentService studentService;

    public PageDto<JobPreview> queryActiveEmployerSchoolEventJobs(String studentID, String eventID,
                                                                  ClientActiveJobFilter filter,
                                                                  List<String> sortBy, List<String> sortType,
                                                                  Integer pageIndex, Integer pageSize) throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        ActiveJobFilter activeJobFilter = new ActiveJobFilter();
        if (filter != null) {
            BeanUtils.copyProperties(filter, activeJobFilter);
        }
        activeJobFilter.setUserID(studentID);
        activeJobFilter.setUserType(StringConstants.STUDENT);
        return activeEmployerSchoolEventJobService
                .queryActiveEmployerSchoolEventJobs(schoolID, eventID, filter, sortBy, sortType, pageIndex, pageSize);
    }

    public JobDto getActiveEmployerSchoolEventJobDetail(String studentID, String eventID, String jobID) throws Exception {
        LatLon location = studentService.getLatLon(studentID);
        if (location == null) {
            location = new LatLon();
        }
        String schoolID = studentService.getStudentSchoolID(studentID);
        return activeEmployerSchoolEventJobService
                .getActiveEmployerSchoolEventJobDetail(schoolID, eventID, jobID,
                        studentID, StringConstants.STUDENT, location.getLat(), location.getLon());
    }
}
