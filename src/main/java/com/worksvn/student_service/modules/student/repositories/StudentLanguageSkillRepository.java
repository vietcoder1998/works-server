package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentLanguageSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface StudentLanguageSkillRepository extends JpaRepository<StudentLanguageSkill, String> {
    StudentLanguageSkill findFirstByIdAndStudent_Id(String id, String studentID);

    @Modifying
    @Transactional
    @Query("update StudentLanguageSkill lk set lk.position = ?3 where lk.student.id = ?1 and lk.id = ?2")
    void updateStudentLanguageSkillPosition(String studentID, String id, int position);

    @Modifying
    @Transactional
    void deleteAllByIdAndStudent_Id(String id, String studentID);

    @Modifying
    @Transactional
    void deleteAllByIdInAndStudent_Id(Set<String> ids, String studentID);
}
