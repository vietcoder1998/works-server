package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface StudentSkillRepository extends JpaRepository<StudentSkill, Integer> {

    Set<StudentSkill> findAllByStudentID(String studentID);

    @Query("select sk.skillID from StudentSkill sk " +
            "where sk.studentID = ?1")
    Set<Integer> getStudentSkillIDs(String studentID);

    @Modifying
    @Transactional
    @Query("delete from StudentSkill sk where sk.studentID = ?1 and sk.skillID in ?2")
    void deleteAllStudentSkills(String studentID, Set<Integer> skillIDs);
}
