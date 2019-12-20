package com.worksvn.student_service.modules.student.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student_saved")
@NoArgsConstructor
@Getter
@Setter
public class StudentSaved {
    public static final String CREATED_DATE = "createdDate";

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "employer_id")
    private String employerID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "created_date")
    private Date createdDate = new Date();

    public StudentSaved(String employerID, Student student) {
        this.employerID = employerID;
        this.student = student;
    }
}
