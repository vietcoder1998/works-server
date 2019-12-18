package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.responses.LatLon;
import com.worksvn.common.modules.employer.enums.JobSavedUserType;
import com.worksvn.common.modules.employer.requests.JobSavedFilter;
import com.worksvn.common.modules.employer.responses.JobSavedPreview;
import com.worksvn.student_service.modules.employer.services.JobSavedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StudentJobSavedService {
    @Autowired
    private JobSavedService jobSavedService;
    @Autowired
    private StudentService studentService;

    public PageDto<JobSavedPreview> getStudentJobSavedPreviews(String studentID,
                                                               List<String> sortBy, List<String> sortType,
                                                               Integer pageIndex, Integer pageSize) throws Exception {
        LatLon location = studentService.getLatLon(studentID);
        if (location == null) {
            location = new LatLon();
        }
        JobSavedFilter filter = new JobSavedFilter();
        filter.setUserID(studentID);
        filter.setUserType(JobSavedUserType.STUDENT);
        filter.setCenterLat(location.getLat());
        filter.setCenterLon(location.getLon());
        return jobSavedService.getJobSavePreviews(sortBy, sortType, pageIndex, pageSize, filter);
    }

    public void saveJob(String jobID, String studentID) throws Exception {
        jobSavedService.saveJob(studentID, JobSavedUserType.STUDENT, jobID);
    }

    public void deleteJobSaves(String studentID, Set<String> ids) throws Exception {
        jobSavedService.deleteJobSaves(studentID, JobSavedUserType.STUDENT, ids);
    }
}
