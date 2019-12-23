package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.constants.ResponseValue;
import com.worksvn.common.exceptions.ResponseException;
import com.worksvn.common.modules.student.requests.NewStudentLanguageSkillDto;
import com.worksvn.common.modules.student.responses.StudentLanguageSkillDto;
import com.worksvn.common.services.internal_service.DistributedDataService;
import com.worksvn.common.utils.jpa.JPAQueryBuilder;
import com.worksvn.common.utils.jpa.JPAQueryExecutor;
import com.worksvn.student_service.modules.common.services.LanguageService;
import com.worksvn.student_service.modules.student.models.entities.Student;
import com.worksvn.student_service.modules.student.models.entities.StudentLanguageSkill;
import com.worksvn.student_service.modules.student.repositories.StudentLanguageSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StudentLanguageSkillService {
    @Autowired
    private StudentLanguageSkillRepository studentLanguageSkillRepository;
    @Autowired
    private JPAQueryExecutor queryExecutor;

    @Autowired
    private StudentService studentService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private DistributedDataService distributedDataService;


    public PageDto<StudentLanguageSkillDto> getStudentLanguageSkillDtos(String studentID) throws Exception {
        JPAQueryBuilder<StudentLanguageSkillDto> queryBuilder = new JPAQueryBuilder<>();
        queryBuilder.selectAsObject(StudentLanguageSkillDto.class,
                "slk.id", "slk.level", "slk.languageID",
                "slk.certificate", "slk.score")
                .from(StudentLanguageSkill.class, "slk");

        JPAQueryBuilder<StudentLanguageSkillDto>.Condition whereCondition = queryBuilder.newCondition();
        whereCondition.and().paramCondition("slk.student.id", "=", studentID);

        queryBuilder.where(whereCondition);

        PageDto<StudentLanguageSkillDto> result = queryExecutor.executePaginationQuery(queryBuilder);
        distributedDataService.completeCollection(result.getItems(), null);
        return result;
    }

    public void addStudentLanguageSkill(String studentID, NewStudentLanguageSkillDto newLanguage) throws Exception {
        Student student = studentService.getStudent(studentID);
        // check language exist
        languageService.getLanguageDto(newLanguage.getLanguageID());

        StudentLanguageSkill newLanguageSkill = new StudentLanguageSkill(student, newLanguage);
        studentLanguageSkillRepository.save(newLanguageSkill);
    }

    public void updateStudentLanguageSkill(String studentID, String languageSkillID,
                                           NewStudentLanguageSkillDto updateLanguage) throws Exception {
        StudentLanguageSkill studentLanguageSkill = studentLanguageSkillRepository
                .findFirstByIdAndStudent_Id(languageSkillID, studentID);
        if (studentLanguageSkill == null) {
            throw new ResponseException(ResponseValue.LANGUAGE_SKILL_NOT_FOUND);
        }
        // check language exist
        languageService.getLanguageDto(updateLanguage.getLanguageID());
        studentLanguageSkill.update(updateLanguage);
        studentLanguageSkillRepository.save(studentLanguageSkill);
    }

    public void deleteStudentLanguageSkill(String studentID, String languageSkillID) {
        studentLanguageSkillRepository.deleteAllByIdAndStudent_Id(languageSkillID, studentID);
    }

    public void deleteStudentLanguageSkills(String studentID, Set<String> languageSkillIDs) {
        if (languageSkillIDs != null && !languageSkillIDs.isEmpty()) {
            studentLanguageSkillRepository.deleteAllByIdInAndStudent_Id(languageSkillIDs, studentID);
        }
    }
}
