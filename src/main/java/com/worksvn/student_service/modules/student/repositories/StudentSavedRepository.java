package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentSaved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface StudentSavedRepository extends JpaRepository<StudentSaved, String> {

    boolean existsByEmployerIDAndStudent_Id(String employerID, String studentID);

    @Modifying
    @Transactional
    @Query("delete from StudentSaved ss where ss.employerID = ?1 and ss.student.id in ?2")
    void deleteEmployerSavedStudents(String employerID, Set<String> studentIDs);
}
