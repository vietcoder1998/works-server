package com.worksvn.student_service.modules.student.services;

import com.google.common.collect.Sets;
import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.StringConstants;
import com.worksvn.common.modules.employer.enums.JobHomePriority;
import com.worksvn.common.modules.employer.requests.*;
import com.worksvn.common.modules.employer.responses.JobDto;
import com.worksvn.common.modules.employer.responses.JobPreview;
import com.worksvn.common.modules.student.responses.StudentQueryActiveJobInfo;
import com.worksvn.student_service.modules.common.services.MajorJobNameService;
import com.worksvn.student_service.modules.employer.services.JobService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StudentJobService {
    @Autowired
    private JobService jobService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private MajorJobNameService majorJobNameService;

    public PageDto<JobPreview> getStudentHomeActiveJobs(String studentID, boolean bySchool,
                                                        ClientHomeActiveJobFilter filter, JobHomePriority priority,
                                                        List<String> sortBy, List<String> sortType,
                                                        Integer pageIndex, Integer pageSize) throws Exception {
        HomeActiveJobFilter homeFilter = new HomeActiveJobFilter();
        createActiveJobFilter(studentID, filter, homeFilter, bySchool);
        return jobService.getHomeActiveJobs(sortBy, sortType, pageIndex, pageSize, homeFilter, priority);
    }

    public PageDto<JobPreview> searchStudentActiveJobs(String studentID, boolean bySchool,
                                                       ClientSearchActiveJobFilter filter,
                                                       List<String> sortBy, List<String> sortType,
                                                       int pageIndex, int pageSize) throws Exception {
        SearchActiveJobFilter searchFilter = new SearchActiveJobFilter();
        createActiveJobFilter(studentID, filter, searchFilter, bySchool);
        return jobService.searchActiveJobs(sortBy, sortType, pageIndex, pageSize, searchFilter);
    }

    public PageDto<JobPreview> getStudentActiveJobs(String studentID, boolean bySchool,
                                                    ClientActiveJobFilter filter,
                                                    List<String> sortBy, List<String> sortType,
                                                    int pageIndex, int pageSize) throws Exception {
        ActiveJobFilter activeJobFilter = new ActiveJobFilter();
        createActiveJobFilter(studentID, filter, activeJobFilter, bySchool);
        return jobService.getActiveJobs(sortBy, sortType, pageIndex, pageSize, activeJobFilter);
    }

    private void createActiveJobFilter(String studentID, ActiveJobFilter sourceFilter,
                                       ActiveJobFilter targetFilter, boolean bySchool) throws Exception {
        if (sourceFilter != null) {
            BeanUtils.copyProperties(sourceFilter, targetFilter);
        }
        StudentQueryActiveJobInfo info = studentService.getQueryActiveJobInfo(studentID);
        if (info != null) {
            if (info.getMajorID() != null) {
                Set<Integer> jobNameIDs = majorJobNameService.getJobNameIDsByMajorIDs(Sets.newHashSet(info.getMajorID()));
                targetFilter.setJobNameIDs(jobNameIDs);
            }
            if (bySchool) {
                targetFilter.setSchoolID(info.getSchoolID());
                targetFilter.setSchoolIgnored(false);
            }
        }
        targetFilter.setUserID(studentID);
        targetFilter.setUserType(StringConstants.STUDENT);
    }

    public JobDto getStudentActiveJobDetail(String studentID, String jobID) throws Exception {
        StudentQueryActiveJobInfo info = studentService.getQueryActiveJobInfo(studentID);
        return jobService.getActiveJobDetail(jobID, studentID, StringConstants.STUDENT,
                info.getSchoolID(), false,
                info.getLat(), info.getLon());
    }
}
