package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.student.responses.StudentSavedPreview;
import com.worksvn.common.services.internal_service.DistributedDataService;
import com.worksvn.common.utils.jpa.JPAQueryBuilder;
import com.worksvn.common.utils.jpa.JPAQueryExecutor;
import com.worksvn.student_service.constants.NumberConstants;
import com.worksvn.student_service.modules.employer.services.EmployerService;
import com.worksvn.student_service.modules.student.models.entities.Student;
import com.worksvn.student_service.modules.student.models.entities.StudentAverageRating;
import com.worksvn.student_service.modules.student.models.entities.StudentSaved;
import com.worksvn.student_service.modules.student.models.entities.StudentUnlocked;
import com.worksvn.student_service.modules.student.repositories.StudentSavedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class StudentSavedService {
    @Autowired
    private StudentSavedRepository studentSavedRepository;
    @Autowired
    private JPAQueryExecutor queryExecutor;

    @Autowired
    private EmployerService employerService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private DistributedDataService distributedDataService;

    public PageDto<StudentSavedPreview> getStudentSaves(String employerID,
                                                        List<String> sortBy, List<String> sortType,
                                                        Integer pageIndex, Integer pageSize) throws Exception {
        JPAQueryBuilder<StudentSavedPreview> queryBuilder = new JPAQueryBuilder<>();
        queryBuilder.selectAsObject(StudentSavedPreview.class,
                "ss.id", "ss.createdDate",
                "s.id", "s.firstName", "s.lastName", "s.birthday",
                "s.avatarUrl", "s.email", "s.phone", "s.gender",
                "s.regionID", "s.address", "s.lat", "s.lon",
                "s.profileVerified", "s.lookingForJob", "s.completePercent",
                "sar.attitudeRating", "sar.skillRating", "sar.jobAccomplishmentRating", "sar.ratingCount",
                "su.id", "s.schoolID", "s.majorID",
                "s.schoolYearStart", "s.schoolYearEnd", "s.studentCode",
                "s.createdDate")
                .from(StudentSaved.class, "ss")
                .join(JPAQueryBuilder.JoinType.INNER_JOIN, "ss.student", "s")
                .joinOn(JPAQueryBuilder.JoinType.LEFT_JOIN, StudentAverageRating.class, "sar",
                        queryBuilder.newCondition().condition("sar.studentID", "=", "s.id"))
                .joinOn(JPAQueryBuilder.JoinType.LEFT_JOIN, StudentUnlocked.class, "su",
                        queryBuilder.newCondition().condition("su.student.id", "=", "s.id")
                                .and().paramCondition("su.employerID", "=", employerID));

        JPAQueryBuilder.Condition whereCondition = queryBuilder.newCondition();
        if (employerID != null && !employerID.isEmpty()) {
            whereCondition.and().paramCondition("ss.employerID", "=", employerID);
        }

        queryBuilder.where(whereCondition)
                .orderBy(sortBy, sortType)
                .setPagination(pageIndex, pageSize, NumberConstants.MAX_PAGE_SIZE);

        PageDto<StudentSavedPreview> result = queryExecutor.executePaginationQuery(queryBuilder);
        distributedDataService.completeContainerCollection(result.getItems(), new HashMap<>());
        return result;
    }

    public void saveStudent(String employerID, String studentID) throws Exception {
        if (studentSavedRepository.existsByEmployerIDAndStudent_Id(employerID, studentID)) {
            throw new ResponseException(ResponseValue.STUDENT_SAVED);
        }
        employerService.checkEmployerExist(employerID);
        Student student = studentService.getStudent(studentID);
        studentSavedRepository.save(new StudentSaved(employerID, student));
    }

    public void deleteStudentSaves(String employerID, Set<String> studentIDs) {
        if (studentIDs != null && !studentIDs.isEmpty()) {
            studentSavedRepository.deleteEmployerSavedStudents(employerID, studentIDs);
        }
    }

    public boolean checkStudentSaved(String studentID, String employerID) {
        return studentSavedRepository.existsByEmployerIDAndStudent_Id(employerID, studentID);
    }
}
