package com.worksvn.student_service.modules.student.models.entities;

import com.worksvn.common.modules.student.requests.NewStudentExperienceDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student_experience")
@Data
@NoArgsConstructor
@Getter
@Setter
public class StudentExperience {
    public static final String STUDENT = "student";
    public static final String STARTED_DATE = "startedDate";
    public static final String FINISHED_DATE = "finishedDate";

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "job_name")
    private String jobName;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "started_date")
    private Date startedDate;
    @Column(name = "finished_date")
    private Date finishedDate;
    @Column(name = "description")
    private String description;
    @Column(name = "created_date")
    private Date createdDate = new Date();

    public StudentExperience(Student student, NewStudentExperienceDto newEducation) {
        this.student = student;
        update(newEducation);
    }

    public void update(NewStudentExperienceDto newEducation) {
        this.jobName = newEducation.getJobName();
        this.companyName = newEducation.getCompanyName();
        this.description = newEducation.getDescription();
        if (newEducation.getStartedDate() > 0) {
            this.startedDate = new Date(newEducation.getStartedDate());
        }
        if (newEducation.getFinishedDate() > 0) {
            this.finishedDate = new Date(newEducation.getFinishedDate());
        }
    }
}
