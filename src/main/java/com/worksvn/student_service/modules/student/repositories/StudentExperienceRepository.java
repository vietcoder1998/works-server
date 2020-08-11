package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface StudentExperienceRepository extends JpaRepository<StudentExperience, String> {

    StudentExperience findFirstByIdAndStudent_Id(String id, String studentID);

    @Modifying
    @Transactional
    @Query("update StudentExperience se set se.position = ?3 where se.student.id = ?1 and se.id = ?2")
    void updateStudentExperiencePosition(String studentID, String id, Integer position);

    List<StudentExperience> findAllByJobNameID(Integer jobNameID);

    @Modifying
    @Transactional
    void deleteAllByStudent_IdAndId(String studentID, String id);

    @Modifying
    @Transactional
    void deleteAllByStudent_IdAndIdIn(String studentID, Set<String> ids);
}
