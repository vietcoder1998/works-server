package com.worksvn.student_service.modules.student.models.entities;

import com.worksvn.common.modules.student.requests.NewStudentLanguageSkillDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student_language_skill")
@Data
@NoArgsConstructor
@Getter
@Setter
public class StudentLanguageSkill {
    public static final String CREATED_DATE = "createdDate";

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "language_id")
    private int languageID;

    @Column(name = "level")
    private String level;
    @Column(name = "certificate")
    private String certificate;
    @Column(name = "score")
    private Double score;
    @Column(name = "created_date")
    private Date createdDate = new Date();
    @Column(name = "position")
    private Integer position;

    public StudentLanguageSkill(Student student,
                                NewStudentLanguageSkillDto newLanguageSkillDto) {
        this.student = student;
        update(newLanguageSkillDto);
    }

    public void update(NewStudentLanguageSkillDto newLanguageSkillDto) {
        this.languageID = newLanguageSkillDto.getLanguageID();
        this.level = newLanguageSkillDto.getLevel();
        this.certificate = newLanguageSkillDto.getCertificate();
        this.score = newLanguageSkillDto.getScore();
    }
}
