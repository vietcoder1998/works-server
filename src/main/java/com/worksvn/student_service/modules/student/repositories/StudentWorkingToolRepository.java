package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentWorkingTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface StudentWorkingToolRepository extends JpaRepository<StudentWorkingTool, Integer> {

    Set<StudentWorkingTool> findAllByStudentID(String studentID);

    @Query("select swt.workingToolID from StudentWorkingTool swt " +
            "where swt.studentID = ?1")
    Set<Integer> getStudentWorkingToolIDs(String studentID);

    @Modifying
    @Transactional
    @Query("delete from StudentWorkingTool swt where swt.studentID = ?1 and swt.workingToolID in ?2")
    void deleteAllStudentWorkingTools(String studentID, Set<Integer> workingToolIDs);
}
