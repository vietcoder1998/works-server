package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.common.responses.JobNameDto;
import com.worksvn.common.modules.student.requests.NewStudentExperienceDto;
import com.worksvn.common.modules.student.requests.NewItemPosition;
import com.worksvn.common.modules.student.responses.StudentExperienceDto;
import com.worksvn.common.utils.jpa.JPAQueryBuilder;
import com.worksvn.common.utils.jpa.JPAQueryExecutor;
import com.worksvn.student_service.modules.common.services.JobNameService;
import com.worksvn.student_service.modules.student.models.entities.Student;
import com.worksvn.student_service.modules.student.models.entities.StudentExperience;
import com.worksvn.student_service.modules.student.repositories.StudentExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class StudentExperienceService {
    @Autowired
    private StudentExperienceRepository experienceRepository;
    @Autowired
    private JPAQueryExecutor queryExecutor;

    @Autowired
    private StudentService studentService;
    @Autowired
    private JobNameService jobNameService;

    public PageDto<StudentExperienceDto> getStudentExperienceDtos(String studentID,
                                                                  List<String> sortBy, List<String> sortType) {
        JPAQueryBuilder<StudentExperienceDto> queryBuilder = new JPAQueryBuilder<>();
        queryBuilder.selectAsObject(StudentExperienceDto.class,
                "se.id", "se.jobName", "se.companyName",
                "se.startedDate", "se.finishedDate", "se.description",
                "se.position")
                .from(StudentExperience.class, "se");

        JPAQueryBuilder<StudentExperienceDto>.Condition whereCondition = queryBuilder.newCondition();
        whereCondition.and().paramCondition("se.student.id", "=", studentID);

        queryBuilder.where(whereCondition)
                .orderBy(sortBy, sortType);

        return queryExecutor.executePaginationQuery(queryBuilder);
    }

    public void createNewStudentExperience(String studentID,
                                           NewStudentExperienceDto newExperience)
            throws Exception {
        Student student = studentService.getStudent(studentID);
        Integer jobNameID = findMatchingJobNameIDByName(newExperience.getJobName());
        StudentExperience experience = new StudentExperience(student, newExperience, jobNameID);
        experienceRepository.save(experience);
    }

    public void updateStudentExperience(String studentID, String experienceID,
                                        NewStudentExperienceDto updateExperience)
            throws Exception {
        StudentExperience experience = experienceRepository.findFirstByIdAndStudent_Id(experienceID, studentID);
        if (experience == null) {
            throw new ResponseException(ResponseValue.STUDENT_EXPERIENCE_NOT_FOUND);
        }
        Integer jobNameID = findMatchingJobNameIDByName(updateExperience.getJobName());
        experience.update(updateExperience, jobNameID);
        experienceRepository.save(experience);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStudentExperiencePosition(String studentID, List<NewItemPosition> newPositions) {
        if (newPositions == null || newPositions.isEmpty()) {
            return;
        }
        for (NewItemPosition newPosition : newPositions) {
            experienceRepository.updateStudentExperiencePosition(studentID, newPosition.getId(), newPosition.getPosition());
        }
    }

    public void deleteStudentEducation(String studentID, String experienceID) {
        experienceRepository.deleteAllByStudent_IdAndId(studentID, experienceID);
    }

    public void deleteStudentEducations(String studentID, Set<String> experienceIDs) {
        if (experienceIDs != null && !experienceIDs.isEmpty()) {
            experienceRepository.deleteAllByStudent_IdAndIdIn(studentID, experienceIDs);
        }
    }

    private Integer findMatchingJobNameIDByName(String name) throws Exception {
        JobNameDto jobName = jobNameService.findJobNameByName(name);
        return jobName == null ? null : jobName.getId();
    }

    public void matchingAllStudentEducationJobName() throws Exception {
        List<StudentExperience> studentExperiences = experienceRepository.findAllByJobNameID(null);
        for (int i = studentExperiences.size() - 1; i >= 0; i--) {
            StudentExperience experience = studentExperiences.get(i);
            Integer matchingJobNameID = null;
            if (experience.getJobName() != null) {
                matchingJobNameID = findMatchingJobNameIDByName(experience.getJobName());
            }
            if (matchingJobNameID != null) {
                experience.setJobNameID(matchingJobNameID);
            } else {
                studentExperiences.remove(i);
            }
        }
        experienceRepository.saveAll(studentExperiences);
    }
}
