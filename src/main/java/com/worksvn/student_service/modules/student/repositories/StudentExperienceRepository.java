package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.common.modules.student.responses.StudentExperienceDto;
import com.worksvn.student_service.modules.student.models.entities.StudentExperience;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface StudentExperienceRepository extends JpaRepository<StudentExperience, String> {

    StudentExperience findFirstByIdAndStudent_Id(String id, String studentID);

    @Query("select new com.worksvn.common.modules.student.responses.StudentExperienceDto" +
            "(se.id, se.jobName, se.companyName, se.startedDate, se.finishedDate, se.description) " +
            "from StudentExperience se " +
            "where se.student.id = ?1")
    List<StudentExperienceDto> getStudentExperienceDtos(String studentID, Sort sort);

    @Modifying
    @Transactional
    void deleteAllByStudent_IdAndId(String studentID, String id);

    @Modifying
    @Transactional
    void deleteAllByStudent_IdAndIdIn(String studentID, Set<String> ids);
}
