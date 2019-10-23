package com.worksvn.student_service.modules.student.models.entities;

import com.worksvn.common.modules.common.enums.Gender;
import com.worksvn.common.modules.student.requests.NewStudentRegistrationDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Student {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    private String id;
    @Column(name = "school_id")
    private String schoolID;
    @Column(name = "major_id")
    private int majorID;
    @Column(name = "region_id")
    private String regionID;
    @Column(name = "student_code")
    private String studentCode;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "identity_card")
    private String identityCard;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "cover_url")
    private String coverUrl;
    @Column(name = "description")
    private String description;
    @Column(name = "identity_card_front_image_url")
    private String identityCardFrontImageUrl;
    @Column(name = "identity_card_back_image_url")
    private String identityCardBackImageUrl;
    @Column(name = "is_profile_verified")
    private boolean isProfileVerified = false;
    @Column(name = "is_looking_for_job")
    private boolean isLookingForJob = true;
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lon")
    private Double lon;
    @Column(name = "lon")

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "student")
    private List<StudentSkill> skills = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "student")
    @OrderBy("startedDate DESC")
    private List<StudentExperience> experiences = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "student")
    private List<StudentLanguageSkill> languageSkills = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "student")
    private StudentUnlocked unlocked;

    public Student(String id, String schoolID, NewStudentRegistrationDto registrationDto) {
        this.id = id;
        this.schoolID = schoolID;
        this.firstName = registrationDto.getFirstName();
        this.lastName = registrationDto.getLastName();
    }
}
