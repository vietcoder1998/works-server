package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentUnlocked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentUnlockedRepository extends JpaRepository<StudentUnlocked, Integer> {

    boolean existsByStudent_IdAndEmployerID(String studentID, String employerID);
}
