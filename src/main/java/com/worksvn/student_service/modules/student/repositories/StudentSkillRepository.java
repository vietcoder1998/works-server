package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface StudentSkillRepository extends JpaRepository<StudentSkill, Integer> {

    Set<StudentSkill> findAllByStudent_Id(String studentID);

    @Query("select sk.skillID from StudentSkill sk " +
            "where sk.student.id = ?1")
    Set<Integer> getStudentSkillIDs(String studentID);

    @Modifying
    @Transactional
    @Query("delete from StudentSkill sk where sk.student.id = ?1 and sk.skillID in ?2")
    void deleteAllStudentSkills(String candidateID, Set<Integer> skillIDs);
}
