package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.common.modules.student.enums.StudentRatingUserType;
import com.worksvn.common.modules.student.responses.StudentRatingDto;
import com.worksvn.student_service.modules.student.models.entities.StudentRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRatingRepository extends JpaRepository<StudentRating, String> {

    StudentRating findFirstByStudent_IdAndUserIDAndUserType(String studentID, String userID,
                                                            StudentRatingUserType userType);

    @Query("select new com.worksvn.common.modules.student.responses.StudentRatingDto" +
            "(sr.attitudeRating, sr.skillRating, sr.jobAccomplishmentRating, " +
            "sr.comment, sr.createdDate, sr.lastModified) " +
            "from StudentRating sr " +
            "where sr.student.id = ?1 and sr.userID = ?2 and sr.userType = ?3")
    StudentRatingDto getStudentRatingDto(String studentID, String userID,
                                         StudentRatingUserType userType);
}
