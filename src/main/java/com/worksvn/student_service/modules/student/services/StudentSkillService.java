package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.responses.SkillDto;
import com.worksvn.student_service.modules.common.services.SkillService;
import com.worksvn.student_service.modules.student.models.entities.Student;
import com.worksvn.student_service.modules.student.models.entities.StudentSkill;
import com.worksvn.student_service.modules.student.repositories.StudentSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentSkillService {
    @Autowired
    private StudentSkillRepository studentSkillRepository;
    @Autowired
    private SkillService skillService;
    @Autowired
    private StudentService studentService;

    public PageDto<SkillDto> getStudentSkillDtos(String studentID,
                                                 List<String> sortBy, List<String> sortType) throws Exception {
        Set<Integer> studentSkillIDs = studentSkillRepository.getStudentSkillIDs(studentID);
        PageDto<SkillDto> result;
        if (studentSkillIDs != null && studentSkillIDs.size() > 0) {
            return skillService.querySkillDtos(sortBy, sortType, null, null, studentSkillIDs);
        } else {
            result = new PageDto<>();
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStudentSkills(String studentID, Set<Integer> skillIDs) throws Exception {
        Student student = studentService.getStudent(studentID);
        Set<Integer> existSkillIDs = skillService.getExistIDs(skillIDs);
        Set<StudentSkill> studentSkills = studentSkillRepository.findAllByStudent_Id(studentID);
        Map<Integer, StudentSkill> mapStudentSkills = studentSkills.stream()
                .collect(Collectors.toMap(StudentSkill::getSkillID, item -> item));
        studentSkills.clear();
        for (Integer skillID : existSkillIDs) {
            StudentSkill studentSkill = mapStudentSkills.get(skillID);
            if (studentSkill == null) {
                studentSkill = new StudentSkill(student, skillID);
            } else {
                mapStudentSkills.remove(skillID);
            }
            studentSkills.add(studentSkill);
        }
        studentSkillRepository.saveAll(studentSkills);
        Set<Integer> removedStudentSkillIDs = mapStudentSkills.keySet();
        if (!removedStudentSkillIDs.isEmpty()) {
            studentSkillRepository.deleteAllStudentSkills(studentID, mapStudentSkills.keySet());
        }
    }
}
