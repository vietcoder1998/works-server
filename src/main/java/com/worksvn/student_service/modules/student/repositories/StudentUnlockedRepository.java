package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentUnlocked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface StudentUnlockedRepository extends JpaRepository<StudentUnlocked, Integer> {
    @Transactional
    boolean existsByStudent_IdAndEmployerID(String studentID, String employerID);

    StudentUnlocked findFirstByStudent_IdAndEmployerID(String studentID, String employerID);
}
