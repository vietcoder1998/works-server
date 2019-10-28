package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface StudentSkillRepository extends JpaRepository<StudentSkill, Integer> {

    @Query("select sk.skillID from StudentSkill sk " +
            "where sk.student.id = ?1")
    Set<Integer> getStudentSkillIDs(String studentID);
}
