package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.school.requests.SchoolEventFilter;
import com.worksvn.common.modules.school.responses.SchoolEventDto;
import com.worksvn.common.modules.school.responses.SchoolEventPreviewDto;
import com.worksvn.student_service.modules.school.services.SchoolEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentSchoolEventService {
    @Autowired
    private StudentService studentService;
    @Autowired
    private SchoolEventService schoolEventService;

    public PageDto<SchoolEventPreviewDto> getActiveStudentSchoolEvents(String studentID,
                                                                       List<String> sortBy, List<String> sortType,
                                                                       Integer pageIndex, Integer pageSize)
            throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        SchoolEventFilter filter = new SchoolEventFilter();
        filter.setSchoolID(schoolID);
        filter.setStarted(true);
        filter.setFinished(false);
        return schoolEventService.getSchoolEvents(filter, sortBy, sortType, pageIndex, pageSize);
    }

    public SchoolEventDto getActiveStudentSchoolEvent(String studentID, String eventID) throws Exception {
        String schoolID = studentService.getStudentSchoolID(studentID);
        return schoolEventService.getSchoolEvent(schoolID, eventID, false, false);
    }
}
