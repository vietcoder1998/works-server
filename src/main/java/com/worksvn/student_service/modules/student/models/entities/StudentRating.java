package com.worksvn.student_service.modules.student.models.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student_rating")
@Data
@NoArgsConstructor
@Getter
@Setter
public class StudentRating {
    public static final String CREATED_DATE = "createdDate";

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    private String id;
    @Column(name = "employer_id")
    private String employerID;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
    @Column(name = "attitude_rating")
    private int attitudeRating;
    @Column(name = "skill_rating")
    private int skillRating;
    @Column(name = "job_accomplishment_rating")
    private int jobAccomplishmentRating;
    @Column(name = "comment")
    private String comment;
    @Column(name = "created_date")
    private Date createdDate;

    @PrePersist
    void onPrePersist() {
        this.createdDate = new Date();
    }
}
