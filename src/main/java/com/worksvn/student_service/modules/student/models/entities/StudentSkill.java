package com.worksvn.student_service.modules.student.models.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "student_skill")
@Data
@NoArgsConstructor
@Getter
@Setter
public class StudentSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
    @Column(name = "skill_id")
    private int skillID;

    public StudentSkill(Student student, int skillID) {
        this.student = student;
        this.skillID = skillID;
    }
}
