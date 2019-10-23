package com.worksvn.student_service.modules.student.models.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student_unlocked")
@Data
@NoArgsConstructor
@Getter
@Setter
public class StudentUnlocked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
    @Column(name = "employer_id")
    private String employerID;
    @Column(name = "created_date")
    private Date createdDate;

    @PrePersist
    void onPrePersist() {
        this.createdDate = new Date();
    }

    public StudentUnlocked(Student student, String employerID) {
        this();
        this.student = student;
        this.employerID = employerID;
    }
}
