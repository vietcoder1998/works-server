package com.worksvn.student_service.modules.student.repositories;

import com.worksvn.common.modules.student.responses.StudentAverageRatingDto;
import com.worksvn.student_service.modules.student.models.entities.StudentAverageRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentAverageRatingRepository extends JpaRepository<StudentAverageRating, String> {

    @Query("select new com.worksvn.common.modules.student.responses.StudentAverageRatingDto" +
            "(sar.attitudeRating, sar.skillRating, sar.jobAccomplishmentRating, sar.ratingCount) " +
            "from StudentAverageRating sar " +
            "where sar.id = ?1")
    StudentAverageRatingDto getStudentAverageRatingDto(String id);
}
