package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.student_service.modules.student.models.entities.StudentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StudentNotificationRepository extends JpaRepository<StudentNotification, String> {

    @Modifying
    @Transactional
    @Query("update StudentNotification sn set sn.seen = ?3 " +
            "where sn.studentID = ?1 and sn.id = ?2")
    void seenStudentNotification(String studentID, String notificationID, boolean seen);
}
