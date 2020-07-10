package com.worksvn.student_service.modules.student.models.entities;

import com.worksvn.common.annotations.common.ProfileRequired;
import com.worksvn.common.modules.common.enums.Gender;
import com.worksvn.common.modules.common.responses.RegionAddress;
import com.worksvn.common.modules.student.requests.UpdateStudentInfoDto;
import com.worksvn.common.modules.student.requests.NewStudentRegistrationDto;
import com.worksvn.common.utils.core.ProfileUtils;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Student {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "school_id")
    private String schoolID;
    @ProfileRequired
    @Column(name = "major_id")
    private Integer majorID;
    @Column(name = "school_year_start")
    private Integer schoolYearStart;
    @Column(name = "school_year_end")
    private Integer schoolYearEnd;
    @Column(name = "region_id")
    private Integer regionID;
    @Column(name = "student_code")
    private String studentCode;
    @ProfileRequired
    @Column(name = "first_name")
    private String firstName;
    @ProfileRequired
    @Column(name = "last_name")
    private String lastName;
    @ProfileRequired
    @Column(name = "email")
    private String email;
    @ProfileRequired
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "identity_card")
    private String identityCard;
    @ProfileRequired
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @ProfileRequired
    @Column(name = "birthday")
    private Date birthday;
    @ProfileRequired
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "cover_url")
    private String coverUrl;
    @ProfileRequired
    @Column(name = "description")
    private String description;
    @Column(name = "identity_card_front_image_url")
    private String identityCardFrontImageUrl;
    @Column(name = "identity_card_back_image_url")
    private String identityCardBackImageUrl;
    @Column(name = "is_profile_verified")
    private Boolean profileVerified = false;
    @Column(name = "is_looking_for_job")
    private Boolean lookingForJob = true;
    @ProfileRequired(score = 0.5)
    @Column(name = "lat")
    private Double lat;
    @ProfileRequired(score = 0.5)
    @Column(name = "lon")
    private Double lon;
    @Column(name = "complete_percent")
    private int completePercent;
    @Column(name = "created_date")
    private Date createdDate = new Date();

    public Student(String id, String schoolID, NewStudentRegistrationDto registrationDto) {
        this.id = id;
        this.schoolID = schoolID;
        this.firstName = registrationDto.getFirstName();
        this.lastName = registrationDto.getLastName();
        try {
            this.gender = Gender.valueOf(registrationDto.getGender());
        } catch (Exception e) {
            this.gender = null;
        }
        this.email = registrationDto.getEmail();
        this.phone = registrationDto.getPhone();
        this.majorID = registrationDto.getMajorID();
        calculateCompletePercent();
    }

    public void update(UpdateStudentInfoDto updateInfo,
                       RegionAddress regionAddress) {
        this.firstName = updateInfo.getFirstName();
        this.lastName = updateInfo.getLastName();
        this.gender = Gender.valueOf(updateInfo.getGender());
        this.email = updateInfo.getEmail();
        this.phone = updateInfo.getPhone();
        if (updateInfo.getBirthday() > 0) {
            this.birthday = new Date(updateInfo.getBirthday());
        }
        this.identityCard = updateInfo.getIdentityCard();
        if (regionAddress != null) {
            this.lat = regionAddress.getLat();
            this.lon = regionAddress.getLon();
            this.address = regionAddress.getAddress();
            if (regionAddress.getRegion() != null) {
                this.regionID = regionAddress.getRegion().getId();
            }
        }
        this.studentCode = updateInfo.getStudentCode();
        calculateCompletePercent();
    }

    public void calculateCompletePercent() {
        this.completePercent = ProfileUtils.calculateCompletePercent(this);
    }
}
