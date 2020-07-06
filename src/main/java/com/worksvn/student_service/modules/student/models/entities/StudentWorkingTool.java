package com.worksvn.student_service.modules.student.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "student_working_tool")
@NoArgsConstructor
@Getter
@Setter
public class StudentWorkingTool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "student_id")
    private String studentID;
    @Column(name = "working_tool_id")
    private int workingToolID;

    public StudentWorkingTool(String studentID, int workingToolID) {
        this.studentID = studentID;
        this.workingToolID = workingToolID;
    }
}
