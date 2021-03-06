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
    @Column(name = "student_id")
    private String studentID;
    @Column(name = "skill_id")
    private int skillID;

    public StudentSkill(String studentID, int skillID) {
        this.studentID = studentID;
        this.skillID = skillID;
    }
}
