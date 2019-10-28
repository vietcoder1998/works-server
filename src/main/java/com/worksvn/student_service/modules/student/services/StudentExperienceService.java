package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.student.requests.NewStudentExperienceDto;
import com.worksvn.common.modules.student.responses.StudentExperienceDto;
import com.worksvn.common.utils.jpa.JPAQueryBuilder;
import com.worksvn.common.utils.jpa.JPAQueryExecutor;
import com.worksvn.student_service.modules.student.models.entities.Student;
import com.worksvn.student_service.modules.student.models.entities.StudentExperience;
import com.worksvn.student_service.modules.student.repositories.StudentExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<StudentExperienceDto> getListStudentExperienceDtos(String studentID,
                                                                   List<String> sortBy, List<String> sortType) {
        JPAQueryBuilder<StudentExperienceDto> queryBuilder = new JPAQueryBuilder<>();
        queryBuilder.selectAsObject(StudentExperienceDto.class,
                "se.id", "se.jobName", "se.companyName",
                "se.startedDate", "se.finishedDate", "se.description")
                .from(StudentExperience.class, "se");

        JPAQueryBuilder<StudentExperienceDto>.Condition whereCondition = queryBuilder.newCondition();
        whereCondition.and().paramCondition("se.student.id", "=", studentID);

        queryBuilder.where(whereCondition)
                .orderBy(sortBy, sortType);

        return queryExecutor.executeQuery(queryBuilder).getResultList();
    }

    public void createNewStudentExperience(String studentID,
                                           NewStudentExperienceDto newExperience)
            throws ResponseException {
        Student student = studentService.getStudent(studentID);
        StudentExperience experience = new StudentExperience(student, newExperience);
        experienceRepository.save(experience);
    }

    public void updateStudentExperience(String studentID, String experienceID,
                                        NewStudentExperienceDto updateExperience)
            throws ResponseException {
        StudentExperience experience = experienceRepository.findFirstByIdAndStudent_Id(experienceID, studentID);
        if (experience == null) {
            throw new ResponseException(ResponseValue.STUDENT_EXPERIENCE_NOT_FOUND);
        }
        experience.update(updateExperience);
        experienceRepository.save(experience);
    }

    public void deleteStudentEducations(String studentID, Set<String> experienceIDs) {
        if (experienceIDs != null && !experienceIDs.isEmpty()) {
            experienceRepository.deleteAllByStudent_IdAndIdIn(studentID, experienceIDs);
        }
    }
}
