package com.worksvn.student_service.modules.student.models.entities;

import com.worksvn.common.modules.student.enums.StudentRatingUserType;
import com.worksvn.common.modules.student.requests.NewStudentRatingDto;
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
    public static final String LAST_MODIFIED = "lastModified";

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    private String id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "user_id")
    private String userID;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private StudentRatingUserType userType;
    @Column(name = "name")
    private String name;
    @Column(name = "avatar_url")
    private String avatarUrl;

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
    @Column(name = "last_modified")
    private Date lastModified;

    public StudentRating(Student student) {
        setCreatedDate(new Date());
        this.student = student;
    }

    public void update(NewStudentRatingDto updateRating) {
        this.userID = updateRating.getUserInfo().getId();
        this.userType = updateRating.getUserInfo().getUserType();
        this.name = updateRating.getUserInfo().getName();
        this.avatarUrl = updateRating.getUserInfo().getAvatarUrl();
        this.attitudeRating = updateRating.getAttitudeRating();
        this.skillRating = updateRating.getSkillRating();
        this.jobAccomplishmentRating = updateRating.getJobAccomplishmentRating();
        this.comment = updateRating.getComment();
        this.lastModified = new Date();
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        this.lastModified = createdDate;
    }
}
