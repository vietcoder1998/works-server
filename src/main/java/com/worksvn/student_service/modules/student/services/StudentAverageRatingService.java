package com.worksvn.student_service.modules.student.services;

import com.worksvn.common.modules.student.responses.StudentAverageRatingDto;
import com.worksvn.student_service.modules.student.repositories.StudentAverageRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentAverageRatingService {
    @Autowired
    public StudentAverageRatingRepository studentAverageRatingRepository;

    public StudentAverageRatingDto getStudentAverageRatingDto(String studentID) {
        StudentAverageRatingDto result = studentAverageRatingRepository.getStudentAverageRatingDto(studentID);
        if (result == null) {
            result = new StudentAverageRatingDto();
        }
        return result;
    }
}
