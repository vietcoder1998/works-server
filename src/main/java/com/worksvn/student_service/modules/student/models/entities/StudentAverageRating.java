package com.worksvn.student_service.modules.student.models.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student_average_rating")
@Data
@NoArgsConstructor
@Getter
@Setter
public class StudentAverageRating {

    @Id
    @Column(name = "student_id")
    private String studentID;
    @Column(name = "attitude_rating")
    private double attitudeRating;
    @Column(name = "skill_rating")
    private double skillRating;
    @Column(name = "job_accomplishment_rating")
    private double jobAccomplishmentRating;
    @Column(name = "rating_count")
    private int ratingCount;
}
