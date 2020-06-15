package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.StringConstants;
import com.worksvn.common.modules.common.responses.LatLon;
import com.worksvn.common.modules.employer.enums.JobHomePriority;
import com.worksvn.common.modules.employer.requests.*;
import com.worksvn.common.modules.employer.responses.JobDto;
import com.worksvn.common.modules.employer.responses.JobPreview;
import com.worksvn.student_service.modules.employer.services.SchoolEventJobService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentSchoolEventJobService {
    @Autowired
    private SchoolEventJobService schoolEventJobService;
    @Autowired
    private StudentService studentService;

    public PageDto<JobPreview> getHomeSchoolEventActiveJobs(String studentID, String eventID,
                                                            List<String> sortBy, List<String> sortType,
                                                            Integer pageIndex, Integer pageSize,
                                                            ClientHomeActiveJobFilter filter, JobHomePriority priority) throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        HomeActiveSchoolEventJobFilter eventJobFilter = new HomeActiveSchoolEventJobFilter();
        if (filter != null) {
            BeanUtils.copyProperties(filter, eventJobFilter);
        }
        eventJobFilter.setSchoolID(schoolID);
        eventJobFilter.setSchoolEventID(eventID);
        return schoolEventJobService.getHomeSchoolEventActiveJobs(sortBy, sortType, pageIndex, pageSize, eventJobFilter, priority);
    }

    public PageDto<JobPreview> searchSchoolEventActiveJobs(String studentID, String eventID,
                                                           List<String> sortBy, List<String> sortType,
                                                           Integer pageIndex, Integer pageSize,
                                                           ClientSearchActiveJobFilter filter) throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        SearchActiveSchoolEventJobFilter eventJobFilter = new SearchActiveSchoolEventJobFilter();
        if (filter != null) {
            BeanUtils.copyProperties(filter, eventJobFilter);
        }
        eventJobFilter.setSchoolID(schoolID);
        eventJobFilter.setSchoolEventID(eventID);
        return schoolEventJobService.searchSchoolEventActiveJobs(sortBy, sortType, pageIndex, pageSize, eventJobFilter);
    }

    public PageDto<JobPreview> queryActiveEmployerSchoolEventJobs(String studentID, String eventID,
                                                                  ClientActiveJobFilter filter,
                                                                  List<String> sortBy, List<String> sortType,
                                                                  Integer pageIndex, Integer pageSize) throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        ActiveJobFilter activeJobFilter = new ActiveJobFilter();
        if (filter != null) {
            BeanUtils.copyProperties(filter, activeJobFilter);
        }
        activeJobFilter.setSchoolID(schoolID);
        activeJobFilter.setUserID(studentID);
        activeJobFilter.setUserType(StringConstants.STUDENT);
        return schoolEventJobService
                .queryActiveEmployerSchoolEventJobs(eventID, activeJobFilter, sortBy, sortType, pageIndex, pageSize);
    }

    public JobDto getActiveEmployerSchoolEventJobDetail(String studentID, String eventID, String jobID) throws Exception {
        LatLon location = studentService.getLatLon(studentID);
        if (location == null) {
            location = new LatLon();
        }
        String schoolID = studentService.getStudentSchoolID(studentID);
        return schoolEventJobService
                .getActiveEmployerSchoolEventJobDetail(schoolID, eventID, jobID,
                        studentID, StringConstants.STUDENT, location.getLat(), location.getLon());
    }

    public JobDto getEmployerSchoolEventJobDetail(String studentID, String eventID, String jobID) throws Exception {
        LatLon location = studentService.getLatLon(studentID);
        if (location == null) {
            location = new LatLon();
        }
        String schoolID = studentService.getStudentSchoolID(studentID);
        return schoolEventJobService
                .getEmployerSchoolEventJobDetail(schoolID, eventID, jobID,
                        studentID, StringConstants.STUDENT, location.getLat(), location.getLon());
    }
}
