package com.worksvn.student_service.modules.student.models.entities;

import com.worksvn.common.modules.student.responses.StudentAverageRatingDto;
import com.worksvn.student_service.modules.student.repositories.StudentAverageRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentAverageRatingService {
    @Autowired
    private StudentAverageRatingRepository studentAverageRatingRepository;

    public StudentAverageRatingDto getStudentAverageRatingDto(String studentID) {
        StudentAverageRatingDto averageRating = studentAverageRatingRepository
                .getStudentAverageRatingDto(studentID);
        if (averageRating == null) {
            averageRating = new StudentAverageRatingDto();
        }
        return averageRating;
    }
}
