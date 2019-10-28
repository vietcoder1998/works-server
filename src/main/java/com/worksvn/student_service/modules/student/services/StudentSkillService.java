package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.modules.common.responses.SkillDto;
import com.worksvn.student_service.modules.common.services.SkillService;
import com.worksvn.student_service.modules.student.repositories.StudentSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StudentSkillService {
    @Autowired
    private StudentSkillRepository studentSkillRepository;
    @Autowired
    private SkillService skillService;

    public List<SkillDto> getStudentSkillDtos(String studentID) throws Exception {
        Set<Integer> studentSkillIDs = studentSkillRepository.getStudentSkillIDs(studentID);
        List<SkillDto> result;
        if (studentSkillIDs != null && studentSkillIDs.size() > 0) {
            return skillService.getSkillDtos(studentSkillIDs, null, null, 0, 0)
                    .getItems();
        } else {
            result = new ArrayList<>();
        }
        return result;
    }
}
