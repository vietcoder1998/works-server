package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.base.models.PageDto;
import com.worksvn.common.modules.common.responses.WorkingToolDto;
import com.worksvn.student_service.modules.common.services.WorkingToolService;
import com.worksvn.student_service.modules.student.models.entities.StudentWorkingTool;
import com.worksvn.student_service.modules.student.repositories.StudentWorkingToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentWorkingToolService {
    @Autowired
    private StudentWorkingToolRepository studentWorkingToolRepository;
    @Autowired
    private WorkingToolService workingToolService;
    @Autowired
    private StudentService studentService;

    public PageDto<WorkingToolDto> getStudentWorkingToolDtos(String studentID,
                                                             List<String> sortBy, List<String> sortType) throws Exception {
        Set<Integer> studentWorkingToolIDs = studentWorkingToolRepository.getStudentWorkingToolIDs(studentID);
        PageDto<WorkingToolDto> result;
        if (studentWorkingToolIDs != null && studentWorkingToolIDs.size() > 0) {
            return workingToolService.queryWorkingToolDtos(sortBy, sortType, null, null, studentWorkingToolIDs);
        } else {
            result = new PageDto<>();
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStudentWorkingTools(String studentID, Set<Integer> workingToolIDs) throws Exception {
        studentService.checkStudentExist(studentID);
        Set<Integer> existWorkingToolIDs = workingToolService.getExistIDs(workingToolIDs);
        Set<StudentWorkingTool> studentWorkingTools = studentWorkingToolRepository.findAllByStudentID(studentID);
        Map<Integer, StudentWorkingTool> mapStudentWorkingTools = studentWorkingTools.stream()
                .collect(Collectors.toMap(StudentWorkingTool::getWorkingToolID, item -> item));
        studentWorkingTools.clear();
        for (Integer workingToolID : existWorkingToolIDs) {
            StudentWorkingTool studentWorkingTool = mapStudentWorkingTools.get(workingToolID);
            if (studentWorkingTool == null) {
                studentWorkingTool = new StudentWorkingTool(studentID, workingToolID);
            } else {
                mapStudentWorkingTools.remove(workingToolID);
            }
            studentWorkingTools.add(studentWorkingTool);
        }
        studentWorkingToolRepository.saveAll(studentWorkingTools);
        Set<Integer> removedStudentWorkingToolIDs = mapStudentWorkingTools.keySet();
        if (!removedStudentWorkingToolIDs.isEmpty()) {
            studentWorkingToolRepository.deleteAllStudentWorkingTools(studentID, mapStudentWorkingTools.keySet());
        }
    }
}
